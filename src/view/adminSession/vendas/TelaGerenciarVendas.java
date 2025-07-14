package view.adminSession.vendas;

import repositories.ConnectionFactory;
import view.adminSession.TelaAdministrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaGerenciarVendas extends JFrame {

  private final ConnectionFactory factory;

  public TelaGerenciarVendas(ConnectionFactory factory) {
    this.factory = factory;

    setTitle("Gerenciar Vendas");
    setSize(1280, 832);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // Título
    JLabel titulo = new JLabel("Vendas", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 48));
    add(titulo, BorderLayout.NORTH);

    // Painel central com botões "Criar Venda" e "Atualizar Venda"
    JPanel painelCentro = new JPanel(new GridLayout(2, 1, 0, 40));
    painelCentro.setBorder(BorderFactory.createEmptyBorder(100, 440, 100, 440));

    JButton btnCriarVenda = criarBotao("Criar Venda");
    JButton btnAtualizarVendas = criarBotao("Atualizar Venda");

    btnCriarVenda.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Lógica para criar nova venda
        // Exemplo: new TelaCriarVenda(factory).setVisible(true);
        dispose();
      }
    });

    btnAtualizarVendas.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Lógica para atualizar vendas
        // Exemplo: new TelaAtualizarVendas(factory).setVisible(true);
        dispose();
      }
    });

    painelCentro.add(btnCriarVenda);
    painelCentro.add(btnAtualizarVendas);
    add(painelCentro, BorderLayout.CENTER);

    // Botão Voltar (abre novamente o painel de ADM)
    JButton btnVoltar = new JButton("Voltar");
    btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 20));
    btnVoltar.setPreferredSize(new Dimension(160, 40));
    btnVoltar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
        new TelaAdministrador(factory).setVisible(true);
      }
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

  // Método main para testar a tela independentemente
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new TelaGerenciarVendas(new ConnectionFactory("store")).setVisible(true);
    });
  }
}
