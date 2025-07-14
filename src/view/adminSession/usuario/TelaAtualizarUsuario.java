package view.adminSession.usuario;

import repositories.ConnectionFactory;
import repositories.user.User;
import repositories.user.UserRole;
import repositories.user.dto.UserUpdateDto;

import javax.swing.*;
import java.awt.*;

public class TelaAtualizarUsuario extends JFrame {
  public TelaAtualizarUsuario(ConnectionFactory factory) {
    setTitle("Atualizar Usuários");
    setSize(1280, 832);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JLabel titulo = new JLabel("Atualizar Usuários", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
    add(titulo, BorderLayout.NORTH);

    JPanel painelLista = new JPanel();
    painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));
    painelLista.setBorder(BorderFactory.createEmptyBorder(20, 32, 20, 32));

    // Cabeçalho estilo matriz
    JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
    header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));

    header.add(createHeader("ID", 40));
    header.add(createHeader("CPF", 120));
    header.add(createHeader("Nome", 150));
    header.add(createHeader("Senha", 100));
    header.add(createHeader("Endereço", 150));
    header.add(createHeader("Cidade", 100));
    header.add(createHeader("Estado", 80));
    header.add(createHeader("CEP", 80));
    header.add(createHeader("Tipo", 100)); // Nova coluna
    header.add(createHeader("Ação", 100));

    painelLista.add(header);

    for (User u : factory.user.findMany()) {
      JPanel linha = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
      linha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

      linha.add(createLabel(String.valueOf(u.id), 40));
      linha.add(createLabel(u.cpf, 120));

      JTextField nome     = createField(u.name, 150);
      JTextField senha    = createField(u.password, 100);
      JTextField endereco = createField(u.address, 150);
      JTextField cidade   = createField(u.city, 100);
      JTextField estado   = createField(u.state, 80);
      JTextField cep      = createField(u.zip, 80);

      // Campo de seleção de tipo de usuário
      JComboBox<UserRole> tipo = new JComboBox<>(UserRole.values());
      tipo.setSelectedItem(u.role);
      tipo.setPreferredSize(new Dimension(100, 30));

      linha.add(nome);
      linha.add(senha);
      linha.add(endereco);
      linha.add(cidade);
      linha.add(estado);
      linha.add(cep);
      linha.add(tipo); // Adiciona o tipo à linha

      JButton btnAtualizar = new JButton("Atualizar");
      btnAtualizar.setPreferredSize(new Dimension(100, 30));
      btnAtualizar.setFont(new Font("SansSerif", Font.BOLD, 13));
      btnAtualizar.addActionListener(e -> {
        try {
          var dto = new UserUpdateDto(
            nome.getText(),
            (UserRole) tipo.getSelectedItem(), // Agora pega o tipo do JComboBox
            senha.getText(),
            endereco.getText(),
            cidade.getText(),
            estado.getText(),
            cep.getText()
          );
          factory.user.update(u.id, dto);
          JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
        }
      });

      linha.add(btnAtualizar);
      painelLista.add(linha);
      painelLista.add(Box.createVerticalStrut(20));
    }

    JScrollPane scroll = new JScrollPane(painelLista);
    add(scroll, BorderLayout.CENTER);

    JButton btnVoltar = new JButton("Voltar");
    btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 18));
    btnVoltar.setPreferredSize(new Dimension(160, 40));
    btnVoltar.addActionListener(e -> {
      dispose();
      new TelaGerenciarUsuarios(factory).setVisible(true);
    });
    JPanel painelSul = new JPanel();
    painelSul.add(btnVoltar);
    add(painelSul, BorderLayout.SOUTH);
  }

  private JLabel createHeader(String text, int width) {
    JLabel label = new JLabel(text);
    label.setPreferredSize(new Dimension(width, 30));
    label.setFont(new Font("SansSerif", Font.BOLD, 16));
    label.setHorizontalAlignment(SwingConstants.CENTER);
    return label;
  }

  private JLabel createLabel(String text, int width) {
    JLabel label = new JLabel(text);
    label.setPreferredSize(new Dimension(width, 30));
    label.setFont(new Font("SansSerif", Font.PLAIN, 14));
    label.setHorizontalAlignment(SwingConstants.CENTER);
    return label;
  }

  private JTextField createField(String text, int width) {
    JTextField field = new JTextField(text);
    field.setPreferredSize(new Dimension(width, 30));
    field.setFont(new Font("SansSerif", Font.PLAIN, 14));
    field.setHorizontalAlignment(SwingConstants.CENTER);
    return field;
  }
}
