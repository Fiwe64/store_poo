package repositories.user.dto;

import repositories.user.UserRole;

public record UserUpdateDto(
  String name,
  UserRole role,
  String password,
  String address,
  String city,
  String state,
  String zip
) {}
