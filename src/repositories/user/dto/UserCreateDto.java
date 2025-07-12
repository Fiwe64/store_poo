package repositories.user.dto;

import repositories.user.UserRole;

public record UserCreateDto(
  UserRole role,
  String name,
  String password,
  String cpf,
  String address,
  String city,
  String state,
  String zip
) {}
