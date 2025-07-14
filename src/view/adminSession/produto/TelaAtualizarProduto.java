// TelaAtualizarProduto.java
package view.adminSession.produto;

import repositories.ConnectionFactory;
import repositories.product.Product;
import repositories.product.dto.ProductUpdateDto;

import javax.swing.*;
import java.awt.*;

public class TelaAtualizarProduto extends JFrame {
  public TelaAtualizarProduto(ConnectionFactory factory) {
    setTitle("Atualizar Produtos");
    setSize(1280, 832);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JLabel titulo = new JLabel("Atualizar Produtos", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
    add(titulo, BorderLayout.NORTH);

    // Painel principal com header e linhas
    JPanel painelLista = new JPanel();
    painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));
    painelLista.setBorder(BorderFactory.createEmptyBorder(20, 32, 20, 32));

    // Cabeçalho estilo matriz
    JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
    header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));

    JLabel hId = new JLabel("ID");        hId.setPreferredSize(new Dimension(40, 30));  hId.setFont(new Font("SansSerif", Font.BOLD, 16));  hId.setHorizontalAlignment(SwingConstants.CENTER);
    JLabel hCode = new JLabel("Código"); hCode.setPreferredSize(new Dimension(220, 30)); hCode.setFont(new Font("SansSerif", Font.BOLD, 16)); hCode.setHorizontalAlignment(SwingConstants.CENTER);
    JLabel hDesc = new JLabel("Descrição"); hDesc.setPreferredSize(new Dimension(180, 30)); hDesc.setFont(new Font("SansSerif", Font.BOLD, 16)); hDesc.setHorizontalAlignment(SwingConstants.CENTER);
    JLabel hPrice = new JLabel("Preço");   hPrice.setPreferredSize(new Dimension(80, 30)); hPrice.setFont(new Font("SansSerif", Font.BOLD, 16)); hPrice.setHorizontalAlignment(SwingConstants.CENTER);
    JLabel hStock = new JLabel("Estoque"); hStock.setPreferredSize(new Dimension(80, 30)); hStock.setFont(new Font("SansSerif", Font.BOLD, 16)); hStock.setHorizontalAlignment(SwingConstants.CENTER);
    JLabel hAction = new JLabel("Ação");  hAction.setPreferredSize(new Dimension(100, 30)); hAction.setFont(new Font("SansSerif", Font.BOLD, 16)); hAction.setHorizontalAlignment(SwingConstants.CENTER);

    header.add(hId);
    header.add(hCode);
    header.add(hDesc);
    header.add(hPrice);
    header.add(hStock);
    header.add(hAction);
    painelLista.add(header);

    // Linhas de produtos
    Product[] produtos = factory.product.findMany();
    for (Product p : produtos) {
      JPanel linha = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
      linha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

      JLabel lblId = new JLabel(String.valueOf(p.id));
      lblId.setPreferredSize(new Dimension(40, 30)); lblId.setHorizontalAlignment(SwingConstants.CENTER); lblId.setFont(new Font("SansSerif", Font.PLAIN, 14));

      JLabel lblCodigo = new JLabel(p.code);
      lblCodigo.setPreferredSize(new Dimension(220, 30)); lblCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14)); lblCodigo.setHorizontalAlignment(SwingConstants.CENTER);

      JTextField txtDescricao = new JTextField(p.description);
      txtDescricao.setPreferredSize(new Dimension(180, 30)); txtDescricao.setFont(new Font("SansSerif", Font.PLAIN, 14)); txtDescricao.setHorizontalAlignment(SwingConstants.CENTER);

      JTextField txtPreco = new JTextField(String.valueOf(p.price));
      txtPreco.setPreferredSize(new Dimension(80, 30)); txtPreco.setFont(new Font("SansSerif", Font.PLAIN, 14)); txtPreco.setHorizontalAlignment(SwingConstants.CENTER);

      JTextField txtEstoque = new JTextField(String.valueOf(p.stock));
      txtEstoque.setPreferredSize(new Dimension(80, 30)); txtEstoque.setFont(new Font("SansSerif", Font.PLAIN, 14)); txtEstoque.setHorizontalAlignment(SwingConstants.CENTER);

      JButton btnAtualizar = new JButton("Atualizar");
      btnAtualizar.setPreferredSize(new Dimension(100, 30)); btnAtualizar.setFont(new Font("SansSerif", Font.BOLD, 13));
      btnAtualizar.addActionListener(e -> {
        try {
          var dto = new ProductUpdateDto(
            txtDescricao.getText(),
            Double.parseDouble(txtPreco.getText()),
            Integer.parseInt(txtEstoque.getText())
          );
          factory.product.update(p.id, dto);
          JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
        }
      });

      linha.add(lblId);
      linha.add(lblCodigo);
      linha.add(txtDescricao);
      linha.add(txtPreco);
      linha.add(txtEstoque);
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
      new TelaGerenciarProdutos(factory).setVisible(true);
    });
    JPanel painelSul = new JPanel();
    painelSul.add(btnVoltar);
    add(painelSul, BorderLayout.SOUTH);
  }
}
