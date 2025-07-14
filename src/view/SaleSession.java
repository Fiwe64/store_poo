package view;

import repositories.ConnectionFactory;
import repositories.sale.Sale;
import repositories.sale.dto.SaleCreateDto;

import java.util.List;

/**
 * Gerenciador de sessão de vendas (simplificado)
 *
 * Nota: Com a nova implementação de carrinho na TelaCompras,
 * esta classe pode ser utilizada para registrar a última venda.
 */
public class SaleSession {
  private static SaleSession instance;
  private int userId;
  private int saleId;
  private Sale ultimaVenda;

  private SaleSession() {}

  public static synchronized SaleSession getInstance() {
    if (instance == null) {
      instance = new SaleSession();
    }
    return instance;
  }

  public void registrarUltimaVenda(Sale venda) {
    this.ultimaVenda = venda;
    this.saleId = venda.id;
    this.userId = venda.userId;
  }

  public Sale getUltimaVenda() {
    return ultimaVenda;
  }

  public int getCurrentSaleId() {
    return saleId;
  }

  public int getUserId() {
    return userId;
  }
}
