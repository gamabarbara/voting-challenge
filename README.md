# Voting Challenge API

## Descrição

Este projeto implementa uma API REST para gerenciar sessões de votação em assembleias de cooperativas. A aplicação permite cadastrar pautas, abrir sessões de votação, receber votos e contabilizar resultados.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.4
- Banco de dados H2 em arquivo (persistente)
- Swagger/OpenAPI
- Maven

## Pré-requisitos

- JDK 21
- Maven

## Configuração do Ambiente

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/votingchallenge.git
   cd votingchallenge
   ```

## Como Executar

### Via Maven:

```bash
mvn spring-boot:run
```

### Via JAR:

```bash
mvn clean package
java -jar target/votingchallenge-0.0.1-SNAPSHOT.jar
```

A aplicação será iniciada na porta 8080.

## Banco de Dados H2 Persistente

O projeto utiliza o H2 em modo arquivo para garantir a persistência dos dados entre reinicializações da aplicação. Os dados são armazenados no diretório `./data/` na raiz do projeto.

### Acessando o Console H2

Para visualizar e gerenciar os dados do banco:

1. Inicie a aplicação
2. Acesse: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
3. Configure:
   - JDBC URL: `jdbc:h2:file:./data/votingdb`
   - Usuário: `sa`
   - Senha: (deixe em branco)
   - Clique em "Conectar"

## Acessando a Documentação da API

A documentação da API está disponível através do Swagger UI:

- URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui/index.html)

Através desta interface, você pode:
- Visualizar todos os endpoints disponíveis
- Testar as operações da API
- Verificar os modelos de dados e parâmetros necessários
- Acessar informações detalhadas sobre cada endpoint

## Funcionalidades

A API oferece as seguintes funcionalidades:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação (com tempo configurável ou padrão de 1 minuto)
- Receber votos (Sim/Não) de associados
- Contabilizar votos e apresentar resultados

## Contato

Em caso de dúvidas ou problemas, entre em contato:
- Nome: Bárbara Gama
- Email: barbara.cp@outlook.com
- GitHub: [github.com/gamabarbara](https://github.com/gamabarbara)
