package view;

import repositories.ConnectionFactory;
import repositories.product.Product;
import repositories.sale.Sale;
import repositories.sale.dto.SaleCreateDto;
import repositories.sale.dto.SaleItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaCompras extends JFrame {
  private final ConnectionFactory factory;
  private final int userId;
  private final List<SaleItem> carrinho = new ArrayList<>();
  private JLabel lblTotal;
  private JButton btnFinalizar;
  private JButton btnCarrinho;

  public TelaCompras(ConnectionFactory factory, int userId) {
    this.factory = factory;
    this.userId = userId;
    initUI();
  }

  private void initUI() {
    setTitle("Sistema de Compras");
    setSize(1280, 832);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // Cabeçalho
    JPanel painelCabecalho = new JPanel(new BorderLayout());
    JLabel titulo = new JLabel("Loja Virtual", SwingConstants.CENTER);
    titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
    painelCabecalho.add(titulo, BorderLayout.NORTH);

    JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    lblTotal = new JLabel("Total: R$ 0,00");
    lblTotal.setFont(new Font("SansSerif", Font.BOLD, 24));
    painelInfo.add(lblTotal);
    painelCabecalho.add(painelInfo, BorderLayout.SOUTH);

    add(painelCabecalho, BorderLayout.NORTH);

    // Produtos
    JPanel painelProdutos = new JPanel(new GridLayout(0, 4, 20, 20));
    painelProdutos.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    for (Product produto : factory.product.findMany()) {
      if (produto.stock > 0) {
        painelProdutos.add(criarCardProduto(produto));
      }
    }

    add(new JScrollPane(painelProdutos), BorderLayout.CENTER);

    // Rodapé
    JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

    btnCarrinho = new JButton("Ver Carrinho (0)");
    btnCarrinho.setFont(new Font("SansSerif", Font.BOLD, 18));
    btnCarrinho.setPreferredSize(new Dimension(200, 50));
    btnCarrinho.addActionListener(e -> mostrarCarrinho());

    btnFinalizar = new JButton("Finalizar Compra");
    btnFinalizar.setFont(new Font("SansSerif", Font.BOLD, 18));
    btnFinalizar.setPreferredSize(new Dimension(200, 50));
    btnFinalizar.setEnabled(false);
    btnFinalizar.addActionListener(e -> finalizarCompra());

    JButton btnSair = new JButton("Sair");
    btnSair.setFont(new Font("SansSerif", Font.BOLD, 18));
    btnSair.setPreferredSize(new Dimension(150, 50));
    btnSair.addActionListener(e -> sair());

    painelRodape.add(btnCarrinho);
    painelRodape.add(btnFinalizar);
    painelRodape.add(btnSair);

    add(painelRodape, BorderLayout.SOUTH);
  }

  private JPanel criarCardProduto(Product produto) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    card.setPreferredSize(new Dimension(250, 300));

    JLabel lblImagem = new JLabel(new ImageIcon());
    lblImagem.setPreferredSize(new Dimension(250, 150));
    lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
    card.add(lblImagem, BorderLayout.NORTH);

    JPanel painelInfo = new JPanel(new GridLayout(3, 1));
    JLabel lblNome = new JLabel(produto.description, SwingConstants.CENTER);
    lblNome.setFont(new Font("SansSerif", Font.BOLD, 16));

    JLabel lblPreco = new JLabel(String.format("R$ %.2f", produto.price), SwingConstants.CENTER);
    lblPreco.setFont(new Font("SansSerif", Font.PLAIN, 16));

    JLabel lblEstoque = new JLabel("Disponível: " + produto.stock, SwingConstants.CENTER);
    lblEstoque.setFont(new Font("SansSerif", Font.PLAIN, 14));

    painelInfo.add(lblNome);
    painelInfo.add(lblPreco);
    painelInfo.add(lblEstoque);

    card.add(painelInfo, BorderLayout.CENTER);

    JButton btnAdd = new JButton("Adicionar");
    btnAdd.setFont(new Font("SansSerif", Font.PLAIN, 14));
    btnAdd.addActionListener(e -> adicionarAoCarrinho(produto));

    card.add(btnAdd, BorderLayout.SOUTH);

    return card;
  }

  private void adicionarAoCarrinho(Product produto) {
    if (produto.stock <= 0) {
      JOptionPane.showMessageDialog(this, "Produto sem estoque disponível!", "Erro", JOptionPane.WARNING_MESSAGE);
      return;
    }

    JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, produto.stock, 1));
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Selecione a quantidade:"));
    panel.add(spinner);

    int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar ao Carrinho", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      int quantidade = (Integer) spinner.getValue();
      for (SaleItem item : carrinho) {
        if (item.productId() == produto.id) {
          JOptionPane.showMessageDialog(this, "Este produto já está no carrinho!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
      }
      carrinho.add(new SaleItem(produto.id, quantidade));
      atualizarInterface();
      JOptionPane.showMessageDialog(this, quantidade + "x " + produto.description + " adicionado ao carrinho!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void atualizarInterface() {
    double total = calcularTotalCarrinho();
    lblTotal.setText(String.format("Total: R$ %.2f", total));
    btnCarrinho.setText("Ver Carrinho (" + carrinho.size() + ")");
    btnFinalizar.setEnabled(!carrinho.isEmpty());
  }

  private double calcularTotalCarrinho() {
    double total = 0;
    for (SaleItem item : carrinho) {
      Product produto = factory.product.findUnique(item.productId());
      if (produto != null) {
        total += produto.price * item.quantity();
      }
    }
    return total;
  }

  private void finalizarCompra() {
    if (carrinho.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Carrinho vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      SaleCreateDto saleDto = new SaleCreateDto(userId, carrinho);
      Sale venda = factory.sale.create(saleDto);
      SaleSession.getInstance().registrarUltimaVenda(venda);

      JOptionPane.showMessageDialog(this, "Compra finalizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
      carrinho.clear();
      atualizarInterface();
      atualizarProdutos();

    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Erro ao finalizar compra: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void atualizarProdutos() {
    getContentPane().removeAll();
    initUI();
    revalidate();
    repaint();
  }

  private void mostrarCarrinho() {
    if (carrinho.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Seu carrinho está vazio!", "Carrinho", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    JPanel painelCarrinho = new JPanel(new BorderLayout());
    painelCarrinho.setPreferredSize(new Dimension(500, 400));

    String[] colunas = {"Produto", "Quantidade", "Preço Unitário", "Subtotal"};
    Object[][] dados = new Object[carrinho.size()][4];

    double total = 0;
    for (int i = 0; i < carrinho.size(); i++) {
      SaleItem item = carrinho.get(i);
      Product produto = factory.product.findUnique(item.productId());

      dados[i][0] = produto.description;
      dados[i][1] = item.quantity();
      dados[i][2] = String.format("R$ %.2f", produto.price);
      double subtotal = produto.price * item.quantity();
      dados[i][3] = String.format("R$ %.2f", subtotal);
      total += subtotal;
    }

    JTable tabela = new JTable(dados, colunas);
    tabela.setEnabled(false);
    painelCarrinho.add(new JScrollPane(tabela), BorderLayout.CENTER);

    JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JLabel lblTotalCarrinho = new JLabel("Total: R$ " + String.format("%.2f", total));
    lblTotalCarrinho.setFont(new Font("SansSerif", Font.BOLD, 18));
    painelTotal.add(lblTotalCarrinho);
    painelCarrinho.add(painelTotal, BorderLayout.SOUTH);

    JDialog dialogo = new JDialog(this, "Seu Carrinho", true);
    dialogo.setContentPane(painelCarrinho);
    dialogo.pack();
    dialogo.setLocationRelativeTo(this);
    dialogo.setVisible(true);
  }

  private void sair() {
    int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente sair do sistema?", "Confirmação", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      System.exit(0);
    }
  }
}
