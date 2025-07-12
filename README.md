# Sistema de Gerenciamento de Loja

Este projeto é um sistema de gerenciamento de loja que permite controlar usuários, produtos, vendas e itens de venda através de uma interface Java com banco de dados MySQL.

## Funcionalidades Principais

- **Gestão de Usuários**:
  - Criação de usuários com diferentes papéis (administrador, caixa, cliente)
  - Atualização e exclusão de usuários
  - Busca por ID ou CPF

- **Gestão de Produtos**:
  - Cadastro de produtos com código único (UUID)
  - Controle de estoque e preços
  - Atualização e remoção de produtos

- **Sistema de Vendas**:
  - Registro de vendas associadas a usuários
  - Adição de múltiplos itens por venda
  - Atualização de vendas com controle transacional
  - Gestão automática de estoque

## Tecnologias Utilizadas

- **Linguagem**: Java 17+
- **Banco de Dados**: MySQL 8+
- **Bibliotecas**:
  - MySQL Connector/J
  - Dotenv (para gerenciamento de variáveis de ambiente)
- **Padrões de Projeto**: Repository Pattern, DTO (Data Transfer Object)

## Configuração do Ambiente

### Pré-requisitos

1. Java JDK 17 ou superior
2. MySQL Server instalado e em execução
3. Maven para gerenciamento de dependências

### Configuração do Banco de Dados

1. Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
MYSQL_URI=jdbc:mysql://localhost:3306
MYSQL_USERNAME=seu_usuario
MYSQL_PASSWORD=sua_senha
```

2. Execute o script SQL para criar o banco de dados:

```sql
CREATE DATABASE IF NOT EXISTS store;
```

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   ├── repositories/
│   │   │   ├── ConnectionFactory.java
│   │   │   ├── item/
│   │   │   │   ├── Item.java
│   │   │   │   ├── ItemsRepository.java
│   │   │   │   └── dto/
│   │   │   │       ├── ItemCreateDto.java
│   │   │   │       └── ItemUpdateDto.java
│   │   │   ├── product/
│   │   │   │   ├── Product.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   └── dto/
│   │   │   │       ├── ProductCreateDto.java
│   │   │   │       └── ProductUpdateDto.java
│   │   │   ├── sale/
│   │   │   │   ├── Sale.java
│   │   │   │   ├── SaleRepository.java
│   │   │   │   └── dto/
│   │   │   │       ├── SaleCreateDto.java
│   │   │   │       └── SaleItem.java
│   │   │   └── user/
│   │   │       ├── User.java
│   │   │       ├── UserRepository.java
│   │   │       ├── UserRole.java
│   │   │       └── dto/
│   │   │           ├── UserCreateDto.java
│   │   │           └── UserUpdateDto.java
│   │   └── Main.java
```

## Exemplos de Uso

### Criando um novo usuário

```java
UserCreateDto userDto = new UserCreateDto(
    UserRole.administrator,
    "John Doe",
    "012345", // senha
    "01234567890", // CPF
    "Av. Avenida, Nº 01",
    "Manaus",
    "AM",
    "10000000"
);

User user = db.user.create(userDto);
```

### Cadastrando um novo produto

```java
ProductCreateDto productDto = new ProductCreateDto(
    "Macarrão instantâneo",
    1.5,
    100 // estoque inicial
);

Product product = db.product.create(productDto);
```

### Realizando uma venda

```java
List<SaleItem> items = List.of(
    new SaleItem(1, 2), // ID do produto, quantidade
    new SaleItem(2, 1)
);

SaleCreateDto saleDto = new SaleCreateDto(1, items); // ID do usuário
Sale sale = db.sale.create(saleDto);
```

### Atualizando uma venda existente

```java
List<SaleItem> newItems = List.of(
    new SaleItem(1, 3), // Novo item
    new SaleItem(3, 1)  // Item adicional
);

Sale updatedSale = db.sale.update(1, newItems); // ID da venda
```

## Executando o Projeto

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/sistema-loja-java.git
```

2. Configure o arquivo `.env` com suas credenciais do MySQL

3. Compile e execute o projeto:
```bash
mvn compile
mvn exec:java -Dexec.mainClass="Main"
```

## Características Importantes

1. **Transações ACID**:
   - Todas operações críticas usam transações com commit/rollback
   - Garantia de consistência de dados

2. **Gestão Automática de Estoque**:
   - Estoque é atualizado automaticamente durante vendas
   - Validação de estoque suficiente antes de realizar vendas

3. **Segurança**:
   - Senhas não são armazenadas em texto plano (deverão ser hasheadas)
   - Dados sensíveis protegidos por variáveis de ambiente

4. **Arquitetura Modular**:
   - Separação clara entre repositórios, DTOs e entidades
   - Fácil manutenção e extensibilidade

## Contribuição

Contribuições são bem-vindas! Siga os passos:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Faça commit das suas alterações (`git commit -m 'Adiciona nova feature'`)
4. Faça push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.
