package repositories.user;

public class User {
  public final int id;
  public final UserRole role;
  public final String cpf;
  public final String name;
  public final String password;
  public final String address;
  public final String city;
  public final String state;
  public final String zip;

  public User(
    int id,
    UserRole role,
    String cpf,
    String name,
    String password,
    String address,
    String city,
    String state,
    String zip
  ) {
    this.id = id;
    this.role = role;
    this.cpf = cpf;
    this.name = name;
    this.password = password;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zip = zip;
  }
}
