package repositories.item.dto;

public record ItemCreateDto(
  Integer productId,
  Integer saleId,
  Integer quantity,
  Double price
) {

}
