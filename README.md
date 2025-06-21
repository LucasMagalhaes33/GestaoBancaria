# API de Gestão Bancária

Esta é uma API RESTful simples para gestão de contas bancárias, desenvolvida com Java 17 e Spring Boot 3.

## Funcionalidades

* Criação de contas com saldo inicial.
* Consulta de saldo de contas.
* Realização de transações financeiras (Débito, Crédito, Pix) com taxas aplicáveis.
* Validação de saldo e existência de contas.
* Documentação da API com Swagger (OpenAPI).

## Arquitetura e Design

O projeto segue princípios de **Clean Architecture** e **Cognitive-Driven Design**, com os pacotes organizados por domínios de negócio (`conta`, `transacao`). Isso promove alta coesão e baixo acoplamento.

* **Linguagem**: Java 17
* **Framework**: Spring Boot 3
* **DTOs**: `Records` para imutabilidade.
* **Testes**: JUnit 5 e Mockito.
* **Documentação**: `springdoc-openapi` para Swagger UI.
* **Persistência**: Implementação em memória (pode ser facilmente substituída por um banco de dados).

---

### Pré-requisitos

* JDK 17 ou superior.
* Apache Maven 3.8 ou superior.

---

### Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git](https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git)
    cd SEU_REPOSITORIO
    ```

2.  **Compile e execute a aplicação com o Maven Wrapper:**
    * No Linux/macOS:
        ```bash
        ./mvnw spring-boot:run
        ```
    * No Windows:
        ```bash
        ./mvnw.cmd spring-boot:run
        ```

A aplicação estará disponível em `http://localhost:8080`.

---

### Endpoints da API

A documentação completa da API, com todos os detalhes e a possibilidade de testar os endpoints interativamente, está disponível no Swagger UI:

* **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

#### Resumo dos Endpoints

* **Criar Conta**: `POST /conta`
    ```json
    {
      "numeroConta": 12345,
      "saldo": 500.75
    }
    ```

* **Consultar Conta**: `GET /conta?numero_conta=12345`

* **Realizar Transação**: `POST /transacao`
    ```json
    {
      "forma_pagamento": "D",
      "numero_conta": 12345,
      "valor": 50.00
    }
    ```
    * Valores para `forma_pagamento`: `P`, `C`, `D`.

---

### Como Executar os Testes

Para rodar os testes unitários, execute o seguinte comando na raiz do projeto:

```bash
./mvnw test
```
