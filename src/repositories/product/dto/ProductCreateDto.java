package repositories.product.dto;

public record ProductCreateDto(
  String description,
  double price,
  Integer stock
) {

}
