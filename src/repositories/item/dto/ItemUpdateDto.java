package repositories.item.dto;

public record ItemUpdateDto(
  Integer saleId,
  Integer quantity,
  Double price
) {

}
