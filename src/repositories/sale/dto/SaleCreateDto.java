package repositories.sale.dto;

import java.util.List;

public record SaleCreateDto(
  Integer userId,
  List<SaleItem> items
) {}