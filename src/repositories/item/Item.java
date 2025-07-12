package repositories.item;

public class Item {
  public final int id;
  public final int productId;
  public final int saleId;
  public final int quantity;
  public final double price;

  public Item(int id, int productId, int saleId, int quantity, double price) {
    this.id = id;
    this.productId = productId;
    this.saleId = saleId;
    this.quantity = quantity;
    this.price = price;
  }
}