package repositories.user;

import java.sql.*;
import java.util.ArrayList;

import repositories.user.dto.UserCreateDto;
import repositories.user.dto.UserUpdateDto;

public class UserRepository {
  private final Connection conn;

  public UserRepository(Connection conn) {
    this.conn = conn;

    String sql = "CREATE TABLE IF NOT EXISTS users (" +
      "id INT AUTO_INCREMENT PRIMARY KEY," +
      "name VARCHAR(255) NOT NULL," +
      "cpf CHAR(11) UNIQUE NOT NULL," +
      "role ENUM('administrator', 'cashier', 'client') NOT NULL," +
      "password VARCHAR(60) NOT NULL," +
      "address VARCHAR(255)," +
      "city VARCHAR(50)," +
      "state VARCHAR(2)," +
      "zip CHAR(8)" +
    ");";

    try (Statement tempStatement = this.conn.createStatement()) {
      tempStatement.execute(sql);
    } catch (SQLException err) {
      throw new RuntimeException("Não foi possível criar a tabela de usuários: ", err);
    }
  }

  public User create(UserCreateDto data) {
    String sql = "INSERT INTO users (name, cpf, role, password, address, city, state, zip) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, data.name());
      stmt.setString(2, data.cpf());
      stmt.setString(3, data.role().name());
      stmt.setString(4, data.password());
      stmt.setString(5, data.address());
      stmt.setString(6, data.city());
      stmt.setString(7, data.state());
      stmt.setString(8, data.zip());

      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Falha ao criar usuário, nenhuma linha afetada.");
      }

      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int id = generatedKeys.getInt(1);
          return new User(id, data.role(), data.cpf(), data.name(), data.password(),
                          data.address(), data.city(), data.state(), data.zip());
        } else {
          throw new SQLException("Falha ao criar usuário, ID não obtido.");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao criar usuário: " + e.getMessage(), e);
    }
  }

  public User findUnique(int id) {
    String sql = "SELECT * FROM users WHERE id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return userTransform(rs);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
    }
  }

  public User findUnique(String cpf) {
    String sql = "SELECT * FROM users WHERE cpf = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, cpf);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return userTransform(rs);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar usuário por CPF: " + e.getMessage(), e);
    }
  }

  public User[] findMany() {
    String sql = "SELECT * FROM users";
    ArrayList<User> users = new ArrayList<>();

    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        users.add(userTransform(rs));
      }
      return users.toArray(new User[0]);
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar todos os usuários: " + e.getMessage(), e);
    }
  }

  public User update(int id, UserUpdateDto data) {
    User existingUser = findUnique(id);
    if (existingUser == null) return null;

    StringBuilder sql = new StringBuilder("UPDATE users SET ");
    ArrayList<Object> params = new ArrayList<>();
    boolean hasUpdates = false;

    if (data.name() != null) {
      sql.append("name = ?, ");
      params.add(data.name());
      hasUpdates = true;
    }
    if (data.role() != null) {
      sql.append("role = ?, ");
      params.add(data.role().name());
      hasUpdates = true;
    }
    if (data.password() != null) {
      sql.append("password = ?, ");
      params.add(data.password());
      hasUpdates = true;
    }
    if (data.address() != null) {
      sql.append("address = ?, ");
      params.add(data.address());
      hasUpdates = true;
    }
    if (data.city() != null) {
      sql.append("city = ?, ");
      params.add(data.city());
      hasUpdates = true;
    }
    if (data.state() != null) {
      sql.append("state = ?, ");
      params.add(data.state());
      hasUpdates = true;
    }
    if (data.zip() != null) {
      sql.append("zip = ?, ");
      params.add(data.zip());
      hasUpdates = true;
    }

    if (!hasUpdates) return existingUser;

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
      throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
    }
  }

  public User update(String cpf, UserUpdateDto data) {
    User existingUser = findUnique(cpf);
    if (existingUser == null) return null;
    return update(existingUser.id, data);
  }

  public User delete(int id) {
    User user = findUnique(id);
    if (user == null) return null;

    String sql = "DELETE FROM users WHERE id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
      return user;
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage(), e);
    }
  }

  public User delete(String cpf) {
    User user = findUnique(cpf);
    if (user == null) return null;
    return delete(user.id);
  }

  private User userTransform(ResultSet rs) throws SQLException {
    return new User(
      rs.getInt("id"),
      UserRole.valueOf(rs.getString("role")),
      rs.getString("cpf"),
      rs.getString("name"),
      rs.getString("password"),
      rs.getString("address"),
      rs.getString("city"),
      rs.getString("state"),
      rs.getString("zip")
    );
  }
}