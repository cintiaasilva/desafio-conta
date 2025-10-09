# 🧾 Teste Prático Desenvolvedor - Contas a Pagar

## 📘 Premissa

Este projeto consiste em desenvolver um **Serviço REST** em **Java Spring Boot** para tratar as regras de negócio de contas a pagar, conforme o desafio proposto.

Os dados das contas são persistidos em um banco **H2** em memória e podem ser acessados e testados diretamente via **Swagger UI**.

---

## 🧠 Regras de Negócio

- Todos os campos são obrigatórios.
- No cadastro de contas a pagar, é necessário verificar se a conta está em atraso.
- Caso esteja em atraso, aplica-se a seguinte regra:

| Dias em Atraso | Multa | Juros/dia |
|----------------|--------|------------|
| até 3 dias     | 2%     | 0,1%       |
| 4 a 5 dias     | 3%     | 0,2%       |
| superior a 5   | 5%     | 0,3%       |

A quantidade de dias em atraso e o valor corrigido são calculados automaticamente e persistidos no banco de dados.

---

## 🏗️ Estrutura do Projeto

```
📦 Contas
 ┣ 📁 src
 ┃ ┣ 📁 main
 ┃ ┃ ┣ 📁 java/com/cintia/desafio/Contas
 ┃ ┃ ┃ ┣ 📁 config
 ┃ ┃ ┃ ┣ 📁 controller
 ┃ ┃ ┃ ┣ 📁 dto
 ┃ ┃ ┃ ┣ 📁 exception
 ┃ ┃ ┃ ┣ 📁 model
 ┃ ┃ ┃ ┣ 📁 repository
 ┃ ┃ ┃ ┗ 📁 service
 ┃ ┃ ┗ 📁 resources
 ┃ ┃   ┣ 📄 application.properties
 ┃ ┃   ┗ 📁 db.migration
 ┃ ┣ 📁 test (JUnit)
 ┣ 📄 Dockerfile
 ┣ 📄 pom.xml
 ┗ 📄 README.md
```

---

## ⚙️ Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **JUnit 5**
- **Flyway**
- **Maven**
- **Swagger UI (OpenAPI)**
- **Docker**

---

## 🚀 Como Executar o Projeto

### 🔹 1. Clonar o repositório
```bash
git clone https://github.com/cintiaasilva/desafio-conta.git
cd desafio-conta
```

### 🔹 2. Rodar com Maven
```bash
mvn spring-boot:run
```

O projeto será iniciado em:
```
http://localhost:8080
```

---

## 🐳 Execução via Docker

### 🔹 1. Construir a imagem
```bash
docker build -t contas-app .
```

### 🔹 2. Executar o container
```bash
docker run -p 8080:8080 contas-app
```

A aplicação estará disponível em:
```
http://localhost:8080
```

---

## 🌐 Documentação da API (Swagger)

O Swagger foi utilizado como **interface substituta do front-end Angular**, permitindo testar e validar todos os endpoints diretamente pelo navegador.

Acesse:
```
http://localhost:8080/swagger-ui.html
```

### Endpoints principais:
| Método | Endpoint      | Descrição                   |
|--------|----------------|-----------------------------|
| `POST` | `/api/conta/pagamento` | Inclui uma conta a pagar |
| `GET`  | `/api/conta` | Lista todos os registros das contas pagas |

---

## 🧩 Estrutura dos DTOs

### 📥 **Entrada (POST /contas)**
```json
{
  "nome": "Conta de Luz",
  "valorOriginal": 200.00,
  "dataVencimento": "2025-10-01",
  "dataPagamento": "2025-10-05"
}
```

### 📤 **Retorno (GET /contas)**
```json
[
  {
    "nome": "Conta de Luz",
    "valorOriginal": 200.00,
    "valorCorrigido": 210.40,
    "diasAtraso": 4,
    "dataPagamento": "2025-10-05"
  }
]
```

---

## 🧪 Testes

Foram implementados testes unitários utilizando **JUnit 5** e **Mockito**, cobrindo:
- Regras de cálculo de multa e juros.
- Persistência de dados via repositório.
- Interações de serviço com o mapper e repositório.

Para executar os testes:
```bash
mvn test
```

---

## 🧠 Decisões Técnicas

- Utilização do **H2** como banco em memória, simplificando a execução e testes.
- Uso de **Swagger UI** para testes de endpoints, substituindo a necessidade de front-end Angular.
- Estrutura modularizada com camadas claras (**Controller**, **Service**, **Repository**, **Model**).
- Utilização do **Flyway** como ferramenta de migração de banco de dados, simplificando os scripts SQL e Java
- Contêinerização via **Docker** para fácil deploy e portabilidade.

---

## 🧾 Exemplo de Cálculo

**Exemplo:**
- Valor original: R$ 100,00  
- 4 dias de atraso  
- Multa: 3% (R$ 3,00)  
- Juros: 0,2% ao dia × 4 dias = 0,8% (R$ 0,80)

**Valor final corrigido:**
```
100 + 3 + 0.8 = 103.80
```

---

## 💬 Considerações Finais

Este projeto foi desenvolvido com foco em:
- Boas práticas de arquitetura Java.
- Testabilidade.
- Clareza de regras de negócio.
- Entrega funcional e conteinerizada.
- Utilização da metologia kanban para organização e planejamento na ferramenta Trello, clique [aqui]([https://www.linkedin.com/in/cintia-silva26/](https://trello.com/invite/b/68e52fe8b73569c46052de9a/ATTI8ca259398970a30c61e863218434e4c1B6C5FE97/gerenciador-de-contas)) para visualização da organização. 

O uso do Swagger substitui o front-end Angular para simplificar a validação e manter a API clara e testável.

---

## ✨ Autor
**👩‍💻 Cintia Aparecida da Silva**  
Desenvolvedora Java | Foco em back-end e boas práticas de código  
🔗 [LinkedIn](https://www.linkedin.com/in/cintia-silva26/)
