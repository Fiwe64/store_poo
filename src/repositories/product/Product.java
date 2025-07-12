package repositories.product;

public class Product {
  public final int id;
  public final String description;
  public final double price;
  public final String code;
  public final int stock;

  public Product(
    int id,
    String code,
    String description,
    double price,
    int stock
  ) {
    this.id = id;
    this.description = description;
    this.price = price;
    this.code = code;
    this.stock = stock;
  }
}
