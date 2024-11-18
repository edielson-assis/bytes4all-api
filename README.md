<h1 align="center">Bytes4All</h1> 

![Badge Concluído](https://img.shields.io/static/v1?label=Status&message=Concluído&color=success&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=orange&style=for-the-badge&logo=java)
![Badge Springboot](https://img.shields.io/static/v1?label=Springboot&message=v3.3.4&color=brightgreen&style=for-the-badge&logo=spring)
![Badge MySQL](https://img.shields.io/static/v1?label=MySQL&message=v8.0.33&color=orange&style=for-the-badge&logo=MySQL)
![Badge Docker](https://img.shields.io/static/v1?label=Docker&message=v27.3.1&color=blue&style=for-the-badge&logo=Docker)

<br>


## :book: Descrição do projeto 

<p align="justify">
Bytes4All é uma API Restful desenvolvida em Java e Spring Boot. Este projeto contém uma aplicação dockerizada que pode ser facilmente executada usando Docker Compose. Siga as instruções abaixo para configurar e iniciar a aplicação. O sistema permite que os usuários realizem upload e download de arquivos no formato PDF. Os dados da API podem ser serializados em três formatos diferentes: JSON, XML e YAML. 
  
Alguns dos principais recursos do sistema incluem:

- Upload: Os usuários podem publicar e-books no formato PDF.

- Download: Os usuários podem baixar os e-books que estiverem disponíveis.
</p>

## :bulb: Funcionalidades

### :lock: Autenticação do usuário

- `Login de usuário`: O usuário deve fazer login para se autenticar na plataforma. Credênciais que devem ser usadas: email e senha. Toda comunicação e feita via token JWT.
- `Cadastro do usuário`: Caso o usuário não possua cadastro, deverá se cadastrar para conseguir fazer login. 
- `Exclusão da conta do usuário`: O usuário poderá excluir a sua conta da plataforma.

### :toolbox: Upload e download

- `Upload`: Apenas upload no formato PDF será aceito. O tamanho máximo por arquivo é de 4MB. 
- `Download`: Após o cadastro do livro, basta acessar o link para download.

--------

## Pré-requisito:

- Docker e Docker Compose instalados no sistema. Você pode baixar o Docker Desktop (que já inclui o Docker Compose) a partir do [site oficial do Docker](https://www.docker.com/).


## Como Executar

### Passo 1: Obtenha o arquivo `docker-compose.yml`

Baixe o arquivo `docker-compose.yml` fornecido no repositório, dentro da pasta examples. Esse arquivo contém as definições de configuração necessárias para rodar a aplicação e suas dependências, como o banco de dados.

### Passo 2: Execute o Docker Compose

No terminal, navegue até a pasta onde você salvou o `docker-compose.yml` e execute o seguinte comando:

```
docker compose up -d
```

### Passo 3: Verifique os Logs (Opcional)

Para verificar se a aplicação está funcionando corretamente, você pode inspecionar os logs com o comando:

```
docker compose logs -f
```

Esse comando exibirá os logs de todos os containers, permitindo que você veja o status da aplicação e do banco de dados.

### Passo 4: Acesse a Aplicação

Após o Docker Compose iniciar todos os containers, a aplicação estará acessível. Você poderá acessá-la no navegador em:

```
http://localhost/swagger-ui/index.html
```
Isso fará com que a aplicação seja inicializada na porta 80.

## Parar e Remover os Containers

```
docker compose down
```
Esse comando encerra a execução dos containers e remove os recursos associados, liberando espaço no sistema.

## Outra alternativa para rodar a aplicação 

Abra o terminal do git bash na pasta onde deseja salvar o projeto e digite o seguinte comando: 

```
git clone git@github.com:edielson-assis/bytes4all-api.git
```
Depois de clonar o projeto, abra o arquivo `application.yml` e mude o profile para **dev**. Em seguida, siga as instruções do passo 2 em diante para configurar e iniciar a aplicação.

## :books: Linguagens, dependencias e libs utilizadas 

- [Java](https://docs.oracle.com/en/java/javase/17/docs/api/index.html)
- [Maven](https://maven.apache.org/ref/3.9.3/maven-core/index.html)
- [Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
- [MySQL Connector](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
- [Spring Data JPA](https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa/3.2.1)
- [Bean Validation API](https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api/3.0.2)
- [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
- [Flyway MySQL](https://mvnrepository.com/artifact/org.flywaydb/flyway-mysql/9.22.2)
- [Flyway Core](https://mvnrepository.com/artifact/org.flywaydb/flyway-core/9.22.2)
- [Spring Security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security/3.2.1)
- [Spring HATEOAS](https://mvnrepository.com/artifact/org.springframework.hateoas/spring-hateoas/2.3.3)
- [Java JWT](https://mvnrepository.com/artifact/com.auth0/java-jwt/4.4.0)
- [Swagger](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui/2.3.0)
- [TestContainers](https://mvnrepository.com/artifact/org.testcontainers/mysql/1.19.5)
- [Docker](https://docs.docker.com/)
- [REST Assured](https://mvnrepository.com/artifact/io.rest-assured/rest-assured/5.5.0)
- [Jackson Dataformat](https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml/2.18.1)
- [ModelMapper](https://mvnrepository.com/artifact/org.modelmapper/modelmapper/3.2.1)

## Licença 

The [Apache License 2.0 License](https://github.com/edielson-assis/bytes4all-api/blob/main/LICENSE) (Apache License 2.0)

Copyright :copyright: 2024 - Bytes4All