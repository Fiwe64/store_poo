package repositories.product.dto;

public record ProductUpdateDto(
  String description,
  Double price,
  Integer stock
) {

}
