package repositories.sale;

import java.sql.Timestamp;

public class Sale {
  public final int id;
  public final int userId;
  public final Timestamp createdAt;

  public Sale(int id, int userId, Timestamp createdAt) {
    this.id = id;
    this.userId = userId;
    this.createdAt = createdAt;
  }
}