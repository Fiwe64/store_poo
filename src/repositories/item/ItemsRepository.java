package repositories.item;

import java.sql.*;
import java.util.ArrayList;

import repositories.item.dto.ItemCreateDto;
import repositories.item.dto.ItemUpdateDto;

public class ItemsRepository {
  private final Connection conn;

  public ItemsRepository(Connection conn) {
    this.conn = conn;

    String sql = "CREATE TABLE IF NOT EXISTS items (" +
      "id INT AUTO_INCREMENT PRIMARY KEY," +
      "product_id INT NOT NULL," +
      "sale_id INT NOT NULL," +
      "quantity INT NOT NULL," +
      "price FLOAT(6, 2) NOT NULL" +
    ");";

    try (Statement tempStatement = this.conn.createStatement()) {
      tempStatement.execute(sql);
    } catch (SQLException err) {
      throw new RuntimeException("Não foi possível criar a tabela de itens: ", err);
    }
  }

  public Item create(ItemCreateDto data) {
    String sql = "INSERT INTO items (product_id, sale_id, quantity, price) VALUES (?, ?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, data.productId());
      stmt.setInt(2, data.saleId());
      stmt.setInt(3, data.quantity());
      stmt.setDouble(4, data.price());

      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Falha ao criar item, nenhuma linha afetada.");
      }

      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int id = generatedKeys.getInt(1);
          return new Item(id, data.productId(), data.saleId(), data.quantity(), data.price());
        } else {
          throw new SQLException("Falha ao criar item, ID não obtido.");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao criar item: " + e.getMessage(), e);
    }
  }

  public Item findUnique(int id) {
    String sql = "SELECT * FROM items WHERE id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return itemTransform(rs);
        }
        return null;
      }
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar item por ID: " + e.getMessage(), e);
    }
  }

  public Item[] findBySaleId(int saleId) {
    String sql = "SELECT * FROM items WHERE sale_id = ?";
    ArrayList<Item> items = new ArrayList<>();

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, saleId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          items.add(itemTransform(rs));
        }
        return items.toArray(new Item[0]);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar itens por venda: " + e.getMessage(), e);
    }
  }

  public Item update(int id, ItemUpdateDto data) {
    Item existingItem = findUnique(id);
    if (existingItem == null) return null;

    StringBuilder sql = new StringBuilder("UPDATE items SET ");
    ArrayList<Object> params = new ArrayList<>();
    boolean hasUpdates = false;

    if (data.saleId() != null) {
      sql.append("sale_id = ?, ");
      params.add(data.saleId());
      hasUpdates = true;
    }
    if (data.quantity() != null) {
      sql.append("quantity = ?, ");
      params.add(data.quantity());
      hasUpdates = true;
    }
    if (data.price() != null) {
      sql.append("price = ?, ");
      params.add(data.price());
      hasUpdates = true;
    }

    if (!hasUpdates) return existingItem;

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
      throw new RuntimeException("Erro ao atualizar item: " + e.getMessage(), e);
    }
  }

  public Item delete(int id) {
    Item item = findUnique(id);
    if (item == null) return null;

    String sql = "DELETE FROM items WHERE id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
      return item;
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar item: " + e.getMessage(), e);
    }
  }

  private Item itemTransform(ResultSet rs) throws SQLException {
    return new Item(
      rs.getInt("id"),
      rs.getInt("product_id"),
      rs.getInt("sale_id"),
      rs.getInt("quantity"),
      rs.getDouble("price")
    );
  }
}