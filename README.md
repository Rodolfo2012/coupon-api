# Coupon API

API para gerenciamento de cupons.

## Pré-requisitos

* Java 17
* Maven
* Docker

## Como Rodar (Desenvolvimento)

1. Clone o repositório:
   ```bash
   git clone https://github.com/Rodolfo2012/coupon-api.git
   ```
2. Navegue até o diretório do projeto:
   ```bash
   cd coupon-api
   ```
3. Compile e rode a aplicação:
   ```bash
   mvn spring-boot:run
   ```

## Como Rodar (Docker)

1. Clone o repositório:
   ```bash
   git clone https://github.com/Rodolfo2012/coupon-api.git
   ```
2. Navegue até o diretório do projeto:
   ```bash
   cd coupon-api
   ```
3. Execute o seguinte comando para construir a imagem e iniciar o container:
   ```bash
   docker-compose up --build
   ```

## Endpoints da API

### Criar Cupom

* **POST** `/v1/coupons`
* **Descrição:** Cria um novo cupom.
* **Request Body:**
  ```json
  {
    "code": "PROMO10",
    "discount": 10.00,
    "expiryDate": "2024-12-31"
  }
  ```

### Deletar Cupom

* **DELETE** `/v1/coupons/{code}`
* **Descrição:** Deleta um cupom existente pelo código.

## Documentação (Swagger)

A documentação completa da API está disponível via Swagger. Após iniciar a aplicação, acesse:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
