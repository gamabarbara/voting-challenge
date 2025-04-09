# Voting Challenge API

## Descrição
Este projeto implementa uma API REST para gerenciar sessões de votação em assembleias de cooperativas. A aplicação permite cadastrar pautas, abrir sessões de votação, receber votos e contabilizar resultados.

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3.4.4
- Banco de dados H2 em arquivo (persistente)
- Swagger/OpenAPI
- Maven
- Docker

## Pré-requisitos
- JDK 21
- Maven
- Docker (opcional)

## Formas de Execução

### ⚠️ Importante
**Observação:** As duas opções de execução (local e Docker) não podem ser utilizadas simultaneamente, pois ambas utilizam a porta 8080 e acessam o mesmo arquivo de banco de dados.

### Opção 1: Execução Local

1. Clone o repositório:
   ```
   git clone https://github.com/seu-usuario/votingchallenge.git
   cd votingchallenge
   ```

2. Execute a aplicação:
   - Via Maven:
     ```
     mvn spring-boot:run
     ```
   - Via JAR:
     ```
     mvn clean package
     java -jar target/votingchallenge-0.0.1-SNAPSHOT.jar
     ```

### Opção 2: Execução via Docker

1. Clone o repositório:
   ```
   git clone https://github.com/seu-usuario/votingchallenge.git
   cd votingchallenge
   ```

2. Construa e execute o container:
   ```
   docker build -t votingchallenge .
   docker run -p 8080:8080 votingchallenge
   ```
   Caso queira executar o docker em segundo plano
   ```
   docker run -dp 8080:8080 votingchallenge
   ```

A aplicação será iniciada na porta 8080 em ambos os casos.

## Banco de Dados H2 Persistente
O projeto utiliza o H2 em modo arquivo para garantir a persistência dos dados entre reinicializações da aplicação. Os dados são armazenados no diretório `./data/` na raiz do projeto.

### Acessando o Console H2
Para visualizar e gerenciar os dados do banco:
1. Inicie a aplicação
2. Acesse: http://localhost:8080/h2-console
3. Configure:
   - JDBC URL: `jdbc:h2:file:./data/votingdb`
   - Usuário: `sa`
   - Senha: (deixe em branco)
4. Clique em "Conectar"

## Documentação da API
A documentação da API está disponível através do Swagger UI:
- URL: http://localhost:8080/swagger-ui.html

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
- GitHub: github.com/gamabarbara
