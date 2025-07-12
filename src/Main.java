import repositories.ConnectionFactory;
import repositories.product.Product;
import repositories.product.dto.ProductCreateDto;
import repositories.user.User;
import repositories.user.UserRole;
import repositories.user.dto.UserCreateDto;

public class Main {
  public static void main(String[] args) throws Exception {
    ConnectionFactory db = new ConnectionFactory("store");

    User user = db.user.create(
      new UserCreateDto(
        UserRole.administrator,
        "John Doe",
        "012345",
        "01234567890",
        "Av. Avenida, Nº 01",
        "Manaus",
        "AM",
        "10000000"
      )
    );

    Product product = db.product.create(
      new ProductCreateDto(
        "Macarrão instantâneo",
        1.5,
        100
      )
    );
  }
}
