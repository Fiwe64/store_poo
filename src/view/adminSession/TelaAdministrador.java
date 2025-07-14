package view.adminSession;

import repositories.ConnectionFactory;
import view.adminSession.produto.TelaGerenciarProdutos;
import view.adminSession.usuario.TelaGerenciarUsuarios;
import view.adminSession.vendas.TelaGerenciarVendas;

import javax.swing.*;
import java.awt.*;

public class TelaAdministrador extends JFrame {

  private final ConnectionFactory factory;

  public TelaAdministrador(ConnectionFactory factory) {
    this.factory = factory;

    setTitle("Bem Vindo ADM");
    setSize(1280, 832);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // Título
    JLabel titulo = new JLabel("Bem Vindo ADM", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 48));
    add(titulo, BorderLayout.NORTH);

    // Painel 2x2 de botões - mantendo o layout da imagem
    JPanel painelBotoes = new JPanel(new GridLayout(2, 2, 40, 40));
    painelBotoes.setBorder(BorderFactory.createEmptyBorder(60, 240, 60, 240));

    // 1) Gerenciar Clientes
    JButton btnGerenciarClientes = criarBotao("Gerenciar Clientes");
    btnGerenciarClientes.addActionListener(e -> {
      new TelaGerenciarUsuarios(factory).setVisible(true); // Usa a mesma factory
      dispose();
    });
    painelBotoes.add(btnGerenciarClientes);

    // 2) Gerenciar Produtos
    JButton btnGerenciarProdutos = criarBotao("Gerenciar Produtos");
    btnGerenciarProdutos.addActionListener(e -> {
      new TelaGerenciarProdutos(factory).setVisible(true); // Usa a mesma factory
      dispose();
    });
    painelBotoes.add(btnGerenciarProdutos);

    // 3) Gerenciar Vendas
    JButton btnGerenciarVendas = criarBotao("Gerenciar Vendas");
    btnGerenciarVendas.addActionListener(e -> {
      new TelaGerenciarVendas(factory).setVisible(true); // Usa a mesma factory
      dispose();
    });
    painelBotoes.add(btnGerenciarVendas);

    // 4) Sair (novo botão conforme imagem)
    JButton btnSair = criarBotao("Sair");
    btnSair.addActionListener(e -> {
      // Fecha completamente a aplicação
      System.exit(0);
    });
    painelBotoes.add(btnSair);

    add(painelBotoes, BorderLayout.CENTER);
  }

  private JButton criarBotao(String texto) {
    JButton btn = new JButton(texto);
    btn.setFont(new Font("SansSerif", Font.PLAIN, 24));
    btn.setPreferredSize(new Dimension(300, 150));
    return btn;
  }
}
