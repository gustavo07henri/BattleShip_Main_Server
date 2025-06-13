
# BattleShip Server

Backend do projeto **BattleShip**, um jogo de Batalha Naval multiplayer, desenvolvido como parte da disciplina de Sistemas Distribuídos e Mobile (SDM). Este servidor contém toda a lógica de negócio, persistência de dados e gerenciamento completo das partidas.

##  Tecnologias Utilizadas

- **Java**
- **Spring Framework**
- **Spring Boot**
- **REST API (HTTP)**
- **WebSocket (STOMP)**
- **JPA / Persistência de dados**

##  Funcionalidades

- Gerenciamento completo das partidas de Batalha Naval.
- Persistência de dados e recuperação de partidas.
- Comunicação em tempo real via WebSocket (STOMP).
- API RESTful para integração com o cliente frontend.
- Controle de estados, jogadas e lógica de negócio das partidas.

##  Instalação e Execução

Para executar o projeto localmente:

```bash
# Clone o repositório
git clone https://github.com/gustavo07henri/BattleShip_Main_Server.git

# Acesse o diretório do projeto
cd BattleShip_Main_Server

# Compile o projeto
mvn clean install

# Execute o servidor Spring Boot
mvn spring-boot:run
```

O servidor backend não possui interface gráfica. A interação com o sistema é realizada via API e WebSocket.

##  Frontend Recomendado

Para utilização completa do sistema, recomenda-se utilizar o cliente frontend disponível em:

[BattleShip Client - GitHub](https://github.com/gustavo07henri/battleship_client)

##  Pré-requisitos

- Java 17+
- Maven

##  Sobre o Projeto

O **BattleShip Server** foi desenvolvido como parte do portfólio acadêmico, com foco em:

- Sistemas distribuídos e mobile
- Comunicação assíncrona em tempo real
- Arquitetura backend orientada a serviços
- Desenvolvimento full stack com integração de múltiplos protocolos

##  Autor

**Gustavo Henrique**

[LinkedIn](https://www.linkedin.com/in/gustavo-santos-633a21246)
