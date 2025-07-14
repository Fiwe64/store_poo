// TelaCriarProduto.java
package view.adminSession.produto;

import repositories.ConnectionFactory;
import repositories.product.dto.ProductCreateDto;

import javax.swing.*;
import java.awt.*;

public class TelaCriarProduto extends JFrame {
  public TelaCriarProduto(ConnectionFactory factory) {
    setTitle("Criar Produto");
    setSize(1280, 832);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JLabel titulo = new JLabel("Criar Produto", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
    add(titulo, BorderLayout.NORTH);

    JPanel painelLista = new JPanel();
    painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));
    painelLista.setBorder(BorderFactory.createEmptyBorder(20, 32, 20, 32));

    // Cabeçalho
    JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
    header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
    String[] cols = {"Descrição", "Preço", "Estoque", "Ação"};
    int[] widths = {400, 80, 80, 100};
    for (int i = 0; i < cols.length; i++) {
      JLabel h = new JLabel(cols[i], SwingConstants.CENTER);
      h.setPreferredSize(new Dimension(widths[i], 30));
      h.setFont(new Font("SansSerif", Font.BOLD, 16));
      header.add(h);
    }
    painelLista.add(header);

    // Linha de criação
    JPanel linha = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
    linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
    linha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JTextField txtDescricao = new JTextField();
    txtDescricao.setPreferredSize(new Dimension(400, 30));
    JTextField txtPreco     = new JTextField();
    txtPreco.setPreferredSize(new Dimension(80, 30));
    JTextField txtEstoque   = new JTextField();
    txtEstoque.setPreferredSize(new Dimension(80, 30));

    JButton btnCriar = new JButton("Criar");
    btnCriar.setPreferredSize(new Dimension(100, 30));
    btnCriar.setFont(new Font("SansSerif", Font.BOLD, 14));
    btnCriar.addActionListener(e -> {
      try {
        var dto = new ProductCreateDto(
          txtDescricao.getText(),
          Double.parseDouble(txtPreco.getText()),
          Integer.parseInt(txtEstoque.getText())
        );
        factory.product.create(dto);
        JOptionPane.showMessageDialog(this, "Produto criado com sucesso!");
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao criar produto: " + ex.getMessage());
      }
    });

    linha.add(txtDescricao);
    linha.add(txtPreco);
    linha.add(txtEstoque);
    linha.add(btnCriar);
    painelLista.add(linha);

    JScrollPane scroll = new JScrollPane(painelLista);
    add(scroll, BorderLayout.CENTER);

    JButton btnVoltar = new JButton("Voltar");
    btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 18));
    btnVoltar.setPreferredSize(new Dimension(160, 40));
    btnVoltar.addActionListener(e -> {
      dispose();
      new TelaGerenciarProdutos(factory).setVisible(true);
    });
    JPanel painelSul = new JPanel();
    painelSul.add(btnVoltar);
    add(painelSul, BorderLayout.SOUTH);
  }
}
