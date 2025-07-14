package repositories;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;
import repositories.product.ProductRepository;
import repositories.sale.SaleRepository;
import repositories.user.UserRepository;

public class ConnectionFactory implements AutoCloseable {
  private Connection conn;

  public final UserRepository user;
  public final ProductRepository product;
  public final SaleRepository sale;

  public ConnectionFactory(String database) {
    Dotenv dotenv = Dotenv.load();

    String MYSQL_URI = dotenv.get("MYSQL_URI");
    String MYSQL_USERNAME = dotenv.get("MYSQL_USERNAME");
    String MYSQL_PASSWORD = dotenv.get("MYSQL_PASSWORD");

    try (
      Connection tempConnection = DriverManager.getConnection(MYSQL_URI, MYSQL_USERNAME, MYSQL_PASSWORD);
      Statement tempStatement = tempConnection.createStatement()
    ) {
      tempStatement.execute("CREATE DATABASE IF NOT EXISTS " + database);
    } catch (SQLException err) {
      throw new RuntimeException("Falha de conexão com o banco de dados: ", err);
    }

    try {
      this.conn = DriverManager.getConnection(
        MYSQL_URI + "/" + database + "?useUnicode=true&characterEncoding=UTF-8",
        MYSQL_USERNAME,
        MYSQL_PASSWORD
      );
    } catch (SQLException err) {
      throw new RuntimeException("Falha de conexão com o banco de dados: ", err);
    }

    this.user = new UserRepository(this.conn);
    this.product = new ProductRepository(this.conn);
    this.sale = new SaleRepository(this.conn);
  }

  @Override
  public void close() throws Exception {
    try {
      if (this.conn != null) this.conn.close();
    } catch (SQLException err) {
      throw new RuntimeException(
        "Não foi possível encerrar a conexão: ", err);
    }
  }
  public Connection getConnection() {
    return this.conn;
  }

}
