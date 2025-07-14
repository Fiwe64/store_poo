package view.adminSession.usuario;

import repositories.ConnectionFactory;
import repositories.user.UserRole;
import repositories.user.dto.UserCreateDto;

import javax.swing.*;
import java.awt.*;

public class TelaCriarUsuario extends JFrame {
  private final JTextField campoNome, campoSenha, campoCpf, campoEndereco, campoCidade, campoEstado, campoCep;
  private final JComboBox<UserRole> campoTipo;

  public TelaCriarUsuario(ConnectionFactory factory) {
    setTitle("Criar Usuário");
    setSize(1280, 832);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JLabel titulo = new JLabel("Criar Usuário", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
    add(titulo, BorderLayout.NORTH);

    JPanel painel = new JPanel(new GridBagLayout());
    painel.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(16, 12, 16, 12);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;

    // Campos
    campoNome     = new JTextField();
    campoSenha    = new JTextField();
    campoCpf      = new JTextField();
    campoEndereco = new JTextField();
    campoCidade   = new JTextField();
    campoEstado   = new JTextField();
    campoCep      = new JTextField();
    campoTipo     = new JComboBox<>(UserRole.values());

    adicionarCampo(painel, gbc, "Nome:", campoNome, 0);
    adicionarCampo(painel, gbc, "Senha:", campoSenha, 1);
    adicionarCampo(painel, gbc, "CPF:", campoCpf, 2);
    adicionarCampo(painel, gbc, "Endereço:", campoEndereco, 3);
    adicionarCampo(painel, gbc, "Cidade:", campoCidade, 4);
    adicionarCampo(painel, gbc, "Estado:", campoEstado, 5);
    adicionarCampo(painel, gbc, "CEP:", campoCep, 6);
    adicionarCampo(painel, gbc, "Tipo de Usuário:", campoTipo, 7);

    // Botões
    JButton btnCriar = new JButton("Criar");
    btnCriar.setFont(new Font("SansSerif", Font.BOLD, 18));
    btnCriar.addActionListener(e -> {
      try {
        var dto = new UserCreateDto(
          (UserRole) campoTipo.getSelectedItem(),
          campoNome.getText(),
          campoSenha.getText(),
          campoCpf.getText(),
          campoEndereco.getText(),
          campoCidade.getText(),
          campoEstado.getText(),
          campoCep.getText()
        );
        factory.user.create(dto);
        JOptionPane.showMessageDialog(this, "Usuário criado com sucesso!");
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao criar usuário: " + ex.getMessage());
      }
    });

   /* JButton btnTrocarTipo = new JButton("Mudar Tipo");
    btnTrocarTipo.setFont(new Font("SansSerif", Font.PLAIN, 16));
    btnTrocarTipo.addActionListener(e -> {
      UserRole atual = (UserRole) campoTipo.getSelectedItem();
      UserRole novo = switch (atual) {
        case administrator -> UserRole.client;
        case client -> UserRole.cashier;
        case cashier -> UserRole.administrator;
      };
      campoTipo.setSelectedItem(novo);
    });*/

    JButton btnVoltar = new JButton("Voltar");
    btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 18));
    btnVoltar.addActionListener(e -> {
      dispose();
      new TelaGerenciarUsuarios(factory).setVisible(true);
    });

    gbc.gridx = 0;
    gbc.gridy = 8;
    painel.add(btnCriar, gbc);
    gbc.gridx = 1;
    //painel.add(btnTrocarTipo, gbc);
    gbc.gridx = 2;
    painel.add(btnVoltar, gbc);

    add(painel, BorderLayout.CENTER);
  }

  private void adicionarCampo(JPanel painel, GridBagConstraints gbc, String label, JComponent campo, int linha) {
    gbc.gridy = linha;
    gbc.gridx = 0;
    JLabel lbl = new JLabel(label);
    lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
    painel.add(lbl, gbc);

    gbc.gridx = 1;
    campo.setPreferredSize(new Dimension(300, 30));
    campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
    painel.add(campo, gbc);
  }
}
