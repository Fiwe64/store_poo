package view.adminSession.usuario;

import io.github.cdimascio.dotenv.Dotenv;
import repositories.ConnectionFactory;
import repositories.user.UserRole;
import repositories.user.dto.UserCreateDto;

import javax.swing.*;
import java.awt.*;

public class TelaRegistrarUsuario extends JFrame {

  private final JTextField nome, cpf, endereco, cidade, estado, cep;
  private final JPasswordField senha;
  private final JComboBox<UserRole> tipoUsuario;

  // Lê o nome do banco do .env
  private static final String DB_NAME = Dotenv.load().get("MYSQL_DATABASE");

  public TelaRegistrarUsuario() {
    setTitle("Registrar Usuário");
    setSize(1280, 832);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    JLabel titulo = new JLabel("Registrar Novo Usuário", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
    add(titulo, BorderLayout.NORTH);

    JPanel painel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(15, 15, 15, 15);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    nome     = addCampo(painel, "Nome:",     0, gbc);
    cpf      = addCampo(painel, "CPF:",      1, gbc);
    senha    = new JPasswordField(25);
    gbc.gridx = 0; gbc.gridy = 2;
    painel.add(new JLabel("Senha:"), gbc);
    gbc.gridx = 1;
    painel.add(senha, gbc);

    endereco = addCampo(painel, "Endereço:", 3, gbc);
    cidade   = addCampo(painel, "Cidade:",   4, gbc);
    estado   = addCampo(painel, "Estado:",   5, gbc);
    cep      = addCampo(painel, "CEP:",      6, gbc);

    gbc.gridx = 0; gbc.gridy = 7;
    painel.add(new JLabel("Tipo de Usuário:"), gbc);
    gbc.gridx = 1;
    tipoUsuario = new JComboBox<>(UserRole.values());
    tipoUsuario.setFont(new Font("SansSerif", Font.PLAIN, 18));
    painel.add(tipoUsuario, gbc);

    JButton btnRegistrar = new JButton("Registrar");
    btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 20));
    btnRegistrar.addActionListener(e -> registrarUsuario());
    gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
    painel.add(btnRegistrar, gbc);

    add(painel, BorderLayout.CENTER);
  }

  private JTextField addCampo(JPanel painel, String label, int y, GridBagConstraints gbc) {
    gbc.gridx = 0; gbc.gridy = y;
    painel.add(new JLabel(label), gbc);
    gbc.gridx = 1;
    JTextField campo = new JTextField(25);
    campo.setFont(new Font("SansSerif", Font.PLAIN, 18));
    painel.add(campo, gbc);
    return campo;
  }

  private void registrarUsuario() {
    try (ConnectionFactory factory = new ConnectionFactory(DB_NAME)) {
      var dto = new UserCreateDto(
        (UserRole) tipoUsuario.getSelectedItem(),
        nome.getText(),
        new String(senha.getPassword()),
        cpf.getText(),
        endereco.getText(),
        cidade.getText(),
        estado.getText(),
        cep.getText()
      );
      factory.user.create(dto);
      JOptionPane.showMessageDialog(this, "Usuário registrado com sucesso!");
      dispose();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
        "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }
}
