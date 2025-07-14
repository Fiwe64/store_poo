package view.adminSession.vendas;

import repositories.ConnectionFactory;
import repositories.item.Item;
import repositories.product.Product;
import repositories.sale.Sale;
import repositories.user.User;
import repositories.item.dto.ItemUpdateDto;

import javax.swing.*;
import java.awt.*;

public class TelaAtualizarVendas extends JFrame {
  public TelaAtualizarVendas(ConnectionFactory factory) {
    setTitle("Atualizar Vendas");
    setSize(1280, 832);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JLabel titulo = new JLabel("Atualizar Vendas", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
    add(titulo, BorderLayout.NORTH);

    JPanel painelLista = new JPanel();
    painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));
    painelLista.setBorder(BorderFactory.createEmptyBorder(20, 32, 20, 32));

    for (Sale venda : factory.sale.findMany()) {
      JPanel painelVenda = new JPanel();
      painelVenda.setLayout(new BoxLayout(painelVenda, BoxLayout.Y_AXIS));
      painelVenda.setBorder(BorderFactory.createTitledBorder("Venda ID: " + venda.id));

      User usuario = factory.user.findUnique(venda.userId);
      JLabel lblUsuario = new JLabel("Usuário: " + usuario.name + " (CPF: " + usuario.cpf + ")");
      lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 16));
      lblUsuario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      painelVenda.add(lblUsuario);

      Item[] itens = factory.sale.findItemsBySaleId(venda.id);
      for (Item item : itens) {
        Product produto = factory.product.findUnique(item.productId);

        JPanel linha = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblProduto = new JLabel(produto.description);
        lblProduto.setPreferredSize(new Dimension(240, 30));
        lblProduto.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JTextField campoQtd = new JTextField(String.valueOf(item.quantity));
        campoQtd.setPreferredSize(new Dimension(100, 30));

        JTextField campoPreco = new JTextField(String.format("%.2f", item.price));
        campoPreco.setPreferredSize(new Dimension(100, 30));

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(100, 30));
        btnAtualizar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAtualizar.addActionListener(e -> {
          try {
            int novaQtd = Integer.parseInt(campoQtd.getText().trim());
            double novoPreco = Double.parseDouble(campoPreco.getText().trim());
            factory.item.update(item.id, new ItemUpdateDto(null, novaQtd, novoPreco));
            JOptionPane.showMessageDialog(this, "Item atualizado com sucesso!");
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar item: " + ex.getMessage());
          }
        });

        linha.add(lblProduto);
        linha.add(campoQtd);
        linha.add(campoPreco);
        linha.add(btnAtualizar);

        painelVenda.add(linha);
        painelVenda.add(Box.createVerticalStrut(10));
      }

      painelLista.add(painelVenda);
      painelLista.add(Box.createVerticalStrut(20));
    }

    JScrollPane scroll = new JScrollPane(painelLista);
    add(scroll, BorderLayout.CENTER);

    JButton btnVoltar = new JButton("Voltar");
    btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 18));
    btnVoltar.setPreferredSize(new Dimension(160, 40));
    btnVoltar.addActionListener(e -> {
      dispose();
      new TelaGerenciarVendas(factory).setVisible(true);
    });

    JPanel painelSul = new JPanel();
    painelSul.add(btnVoltar);
    add(painelSul, BorderLayout.SOUTH);
  }
}
