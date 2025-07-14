package view.adminSession.produto;

import repositories.ConnectionFactory;
import view.adminSession.TelaAdministrador;

import javax.swing.*;
import java.awt.*;

public class TelaGerenciarProdutos extends JFrame {

  private final ConnectionFactory factory;

  public TelaGerenciarProdutos(ConnectionFactory factory) {
    this.factory = factory;

    setTitle("Produtos");
    setSize(1280, 832);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // Título
    JLabel titulo = new JLabel("Produtos", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 48));
    add(titulo, BorderLayout.NORTH);

    // Painel central com botões "Criar" e "Atualizar"
    JPanel painelCentro = new JPanel(new GridLayout(2, 1, 0, 40));
    painelCentro.setBorder(BorderFactory.createEmptyBorder(100, 440, 100, 440));

    JButton btnCriar = criarBotao("Criar Produto");
    JButton btnAtualizar = criarBotao("Atualizar Produto");

    btnCriar.addActionListener(e -> {
      new TelaCriarProduto(factory).setVisible(true);
      dispose();
    });

    btnAtualizar.addActionListener(e -> {
      new TelaAtualizarProduto(factory).setVisible(true);
      dispose();
    });

    painelCentro.add(btnCriar);
    painelCentro.add(btnAtualizar);
    add(painelCentro, BorderLayout.CENTER);

    // Botão Voltar (abre novamente o painel de ADM)
    JButton btnVoltar = new JButton("Voltar");
    btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 20));
    btnVoltar.setPreferredSize(new Dimension(160, 40));
    btnVoltar.addActionListener(e -> {
      dispose();
      new TelaAdministrador(factory).setVisible(true);
    });
    JPanel painelSul = new JPanel();
    painelSul.add(btnVoltar);
    add(painelSul, BorderLayout.SOUTH);
  }

  private JButton criarBotao(String texto) {
    JButton btn = new JButton(texto);
    btn.setFont(new Font("SansSerif", Font.PLAIN, 24));
    btn.setPreferredSize(new Dimension(400, 120));
    return btn;
  }
}
