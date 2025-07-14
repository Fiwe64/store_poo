package repositories.sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import repositories.item.ItemsRepository;
import repositories.product.ProductRepository;
import repositories.sale.dto.SaleCreateDto;
import repositories.sale.dto.SaleItem;

public class SaleRepository {
  private final Connection conn;
  private final ProductRepository productRepository;
  private final ItemsRepository itemsRepository;

  public SaleRepository(Connection conn) {
    this.conn = conn;
    this.productRepository = new ProductRepository(conn);
    this.itemsRepository = new ItemsRepository(conn);

    String sql = "CREATE TABLE IF NOT EXISTS sales (" +
      "id INT AUTO_INCREMENT PRIMARY KEY," +
      "user_id INT NOT NULL," +
      "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
    ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException err) {
      throw new RuntimeException("Não foi possível criar a tabela de vendas: ", err);
    }
  }

  public Sale create(SaleCreateDto data) {
    String sql = "INSERT INTO sales (user_id) VALUES (?)";

    try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      conn.setAutoCommit(false);

      stmt.setInt(1, data.userId());
      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Falha ao criar venda, nenhuma linha afetada.");
      }

      int saleId;
      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          saleId = generatedKeys.getInt(1);
        } else {
          throw new SQLException("Falha ao criar venda, ID não obtido.");
        }
      }

      for (SaleItem item : data.items()) {
        repositories.product.Product product = productRepository.findUnique(item.productId());
        if (product == null) {
          throw new SQLException("Produto não encontrado: " + item.productId());
        }

        if (product.stock < item.quantity()) {
          throw new SQLException("Estoque insuficiente para o produto: " + product.id);
        }

        productRepository.update(product.id,
          new repositories.product.dto.ProductUpdateDto(
            null,
            null,
            product.stock - item.quantity()
          )
        );

        itemsRepository.create(
          new repositories.item.dto.ItemCreateDto(
            item.productId(),
            saleId,
            item.quantity(),
            product.price
          )
        );
      }

      conn.commit();
      return findUnique(saleId);
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException ex) {
        throw new RuntimeException("Rollback falhou: " + ex.getMessage(), ex);
      }
      throw new RuntimeException("Erro ao criar venda: " + e.getMessage(), e);
    } finally {
      try {
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        // Ignorar erro ao restaurar autocommit
      }
    }
  }

  public Sale findUnique(int id) {
    String sql = "SELECT * FROM sales WHERE id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return saleTransform(rs);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar venda por ID: " + e.getMessage(), e);
    }
  }

  public repositories.item.Item[] findItemsBySaleId(int saleId) {
    return itemsRepository.findBySaleId(saleId);
  }


  public Sale[] findMany() {
    String sql = "SELECT * FROM sales";
    List<Sale> sales = new ArrayList<>();

    try (Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        sales.add(saleTransform(rs));
      }
      return sales.toArray(new Sale[0]);
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar todas as vendas: " + e.getMessage(), e);
    }
  }

  public Sale update(int saleId, List<SaleItem> newItems) {
    Sale sale = findUnique(saleId);
    if (sale == null) return null;

    try {
      conn.setAutoCommit(false);

      repositories.item.Item[] currentItems = itemsRepository.findBySaleId(saleId);

      for (repositories.item.Item item : currentItems) {
        repositories.product.Product product = productRepository.findUnique(item.productId);
        if (product != null) {
          productRepository.update(product.id,
            new repositories.product.dto.ProductUpdateDto(
              null,
              null,
              product.stock + item.quantity
            )
          );
        }
        itemsRepository.delete(item.id);
      }

      for (SaleItem item : newItems) {
        repositories.product.Product product = productRepository.findUnique(item.productId());
        if (product == null) {
          throw new SQLException("Produto não encontrado: " + item.productId());
        }

        if (product.stock < item.quantity()) {
          throw new SQLException("Estoque insuficiente para o produto: " + product.id);
        }

        productRepository.update(product.id,
          new repositories.product.dto.ProductUpdateDto(
            null,
            null,
            product.stock - item.quantity()
          )
        );

        itemsRepository.create(
          new repositories.item.dto.ItemCreateDto(
            item.productId(),
            saleId,
            item.quantity(),
            product.price
          )
        );
      }

      conn.commit();
      return findUnique(saleId);
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException ex) {
        throw new RuntimeException("Rollback falhou: " + ex.getMessage(), ex);
      }
      throw new RuntimeException("Erro ao atualizar venda: " + e.getMessage(), e);
    } finally {
      try {
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        // Ignorar erro ao restaurar autocommit
      }
    }
  }

  public Sale delete(int id) {
    Sale sale = findUnique(id);
    if (sale == null) return null;

    try {
      conn.setAutoCommit(false);

      repositories.item.Item[] items = itemsRepository.findBySaleId(id);
      for (repositories.item.Item item : items) {
        itemsRepository.delete(item.id);
      }

      String sql = "DELETE FROM sales WHERE id = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
      }

      conn.commit();
      return sale;
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException ex) {
        throw new RuntimeException("Rollback falhou: " + ex.getMessage(), ex);
      }
      throw new RuntimeException("Erro ao deletar venda: " + e.getMessage(), e);
    } finally {
      try {
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        // Ignorar erro
      }
    }
  }

  private Sale saleTransform(ResultSet rs) throws SQLException {
    return new Sale(
      rs.getInt("id"),
      rs.getInt("user_id"),
      rs.getTimestamp("created_at")
    );
  }
}
