package repositories.product;

import java.sql.*;
import java.util.ArrayList;

import repositories.product.dto.ProductCreateDto;
import repositories.product.dto.ProductUpdateDto;

public class ProductRepository {
  private final Connection conn;

  public ProductRepository(Connection conn) {
    this.conn = conn;

    String sql = "CREATE TABLE IF NOT EXISTS products (" +
      "id INT AUTO_INCREMENT PRIMARY KEY," +
      "description VARCHAR(255) NOT NULL," +
      "price FLOAT(6, 2) NOT NULL," +
      "code CHAR(36) DEFAULT (UUID())," +
      "stock INT NOT NULL" +
    ");";

    try (Statement tempStatement = this.conn.createStatement()) {
      tempStatement.execute(sql);
    } catch (SQLException err) {
      throw new RuntimeException("Não foi possível criar a tabela de produtos: ", err);
    }
  }

  public Product create(ProductCreateDto data) {
    String sql = "INSERT INTO products (description, price, stock) VALUES (?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, data.description());
      stmt.setDouble(2, data.price());
      stmt.setInt(3, data.stock());

      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Falha ao criar produto, nenhuma linha afetada.");
      }

      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int id = generatedKeys.getInt(1);
          return findUnique(id);
        } else {
          throw new SQLException("Falha ao criar produto, ID não obtido.");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao criar produto: " + e.getMessage(), e);
    }
  }

  public Product findUnique(int id) {
    String sql = "SELECT * FROM products WHERE id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return productTransform(rs);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar produto por ID: " + e.getMessage(), e);
    }
  }

  public Product findUnique(String code) {
    String sql = "SELECT * FROM products WHERE code = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, code);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return productTransform(rs);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar produto por código: " + e.getMessage(), e);
    }
  }

  public Product[] findMany() {
    String sql = "SELECT * FROM products";
    ArrayList<Product> products = new ArrayList<>();

    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        products.add(productTransform(rs));
      }
      return products.toArray(new Product[0]);
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar todos os produtos: " + e.getMessage(), e);
    }
  }

  public Product update(int id, ProductUpdateDto data) {
    Product existingProduct = findUnique(id);
    if (existingProduct == null) return null;

    StringBuilder sql = new StringBuilder("UPDATE products SET ");
    ArrayList<Object> params = new ArrayList<>();
    boolean hasUpdates = false;

    if (data.description() != null) {
      sql.append("description = ?, ");
      params.add(data.description());
      hasUpdates = true;
    }
    if (data.price() != null) {
      sql.append("price = ?, ");
      params.add(data.price());
      hasUpdates = true;
    }
    if (data.stock() != null) {
      sql.append("stock = ?, ");
      params.add(data.stock());
      hasUpdates = true;
    }

    if (!hasUpdates) return existingProduct;

    sql.setLength(sql.length() - 2);
    sql.append(" WHERE id = ?");
    params.add(id);

    try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
      for (int i = 0; i < params.size(); i++) {
        stmt.setObject(i + 1, params.get(i));
      }
      stmt.executeUpdate();
      return findUnique(id);
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
    }
  }

  public Product update(String code, ProductUpdateDto data) {
    Product existingProduct = findUnique(code);
    if (existingProduct == null) {
        return null;
    }
    return update(existingProduct.id, data);
  }

  public Product delete(int id) {
    Product product = findUnique(id);
    if (product == null) return null;

    String sql = "DELETE FROM products WHERE id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
      return product;
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar produto: " + e.getMessage(), e);
    }
  }

  private Product productTransform(ResultSet rs) throws SQLException {
    return new Product(
      rs.getInt("id"),
      rs.getString("code"),
      rs.getString("description"),
      rs.getDouble("price"),
      rs.getInt("stock")
    );
  }
}