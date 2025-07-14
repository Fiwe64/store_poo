import repositories.ConnectionFactory;
import repositories.product.Product;
import repositories.product.dto.ProductCreateDto;
import repositories.user.User;
import repositories.user.UserRole;
import repositories.user.dto.UserCreateDto;
import view.TelaLogin;

import javax.swing.*;

public class Main {
  public static void main(String[] args) throws Exception {
    SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
}

}
