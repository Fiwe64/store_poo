package view;

import io.github.cdimascio.dotenv.Dotenv;
import repositories.ConnectionFactory;
import repositories.user.User;
import repositories.user.UserRole;
import view.adminSession.TelaAdministrador;
import view.adminSession.usuario.TelaRegistrarUsuario;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

  private final JTextField campoCpf;
  private final JPasswordField campoSenha;
  private final JButton botaoLogin, botaoRegistrar;

  // Lê o nome do banco de dados de uma variável MYSQL_DATABASE no .env
  private static final String DB_NAME = Dotenv
    .load()
    .get("MYSQL_DATABASE");

  public TelaLogin() {
    setTitle("Login");
    setSize(1280, 832);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JLabel titulo = new JLabel("Login", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 48));
    add(titulo, BorderLayout.NORTH);

    JPanel painelPrincipal = new JPanel(new BorderLayout());
    painelPrincipal.setBorder(BorderFactory.createEmptyBorder(120, 10, 120, 10));

    JPanel painelCampos = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(24/2, 10, 24/2, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // CPF
    gbc.gridx = 0; gbc.gridy = 0;
    painelCampos.add(new JLabel("CPF:"), gbc);
    gbc.gridx = 1;
    campoCpf = new JTextField(25);
    painelCampos.add(campoCpf, gbc);

    // Senha
    gbc.gridx = 0; gbc.gridy = 1;
    painelCampos.add(new JLabel("Senha:"), gbc);
    gbc.gridx = 1;
    campoSenha = new JPasswordField(25);
    painelCampos.add(campoSenha, gbc);

    painelPrincipal.add(painelCampos, BorderLayout.CENTER);

    JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 48, 48));
    botaoLogin     = new JButton("Login");
    botaoRegistrar = new JButton("Registrar");
    for (JButton btn : new JButton[]{botaoLogin, botaoRegistrar}) {
      btn.setFont(new Font("SansSerif", Font.BOLD, 20));
      btn.setPreferredSize(new Dimension(160, 50));
      painelBotoes.add(btn);
    }

    botaoLogin.addActionListener(e -> realizarLogin());
    botaoRegistrar.addActionListener(e -> new TelaRegistrarUsuario().setVisible(true));

    painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
    add(painelPrincipal, BorderLayout.CENTER);
  }

  private void realizarLogin() {
    String cpf = campoCpf.getText().trim();
    String senha = new String(campoSenha.getPassword());

    try {
      ConnectionFactory factory = new ConnectionFactory(DB_NAME); // ✅ Mantém a conexão viva
      User user = factory.user.findUnique(cpf);

      if (user == null) {
        JOptionPane.showMessageDialog(this, "Usuário não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (!user.password.equals(senha)) {
        JOptionPane.showMessageDialog(this, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
      }

      dispose();
      if (user.role == UserRole.administrator) {
        new TelaAdministrador(factory).setVisible(true);
      } else {
        new TelaCompras(factory, user.id).setVisible(true);
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Erro de conexão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }


  public static void main(String[] args) {

  }
}
