<h1 align="center">Bytes4All</h1> 

![Badge Concluído](https://img.shields.io/static/v1?label=Status&message=Em%20Desenvolvimento&color=success&style=for-the-badge)<br>
![Badge Java](https://img.shields.io/static/v1?label=Java&message=21&color=orange&style=for-the-badge&logo=java)<br>
![Badge Springboot](https://img.shields.io/static/v1?label=Springboot&message=v3.3.4&color=brightgreen&style=for-the-badge&logo=spring)<br>
![Badge MySQL](https://img.shields.io/static/v1?label=MySQL&message=v8.0.33&color=orange&style=for-the-badge&logo=MySQL)<br>
[![Docker Hub Repo](https://img.shields.io/docker/pulls/edielsonassis/bytes4all.svg)](https://hub.docker.com/repository/docker/edielsonassis/bytes4all)

<br>


## :book: Descrição do projeto 

<p align="justify">
Esta é a API Restful da aplicação Bytes4All, desenvolvida em Java e Spring Boot. Os dados da API  podem ser serializados em três formatos diferentes: JSON, XML e YAML. 
Visando incentivar a criacação de conteúdos, a API possibilita a  publicação de conteúdos que não são protegidos por direitos autorais. Fazendo com que o usuário produza o próprio conteúdo. 
O sistema permite que os usuários realizem upload e download de e-books. Alguns dos principais recursos do sistema incluem:

- Upload: Os usuários podem publicar e-books no formato PDF na plataforma, descrevendo detalhes sobre o e-book.

- Download: Os usuários podem baixar os e-books que estiverem disponíveis, bastando selecionar o e-book desejado.
</p>


### :lock: Autenticação do usuário

- `Login de usuário`: O usuário deve fazer login para se autenticar na plataforma. Credênciais que devem ser usadas: email e senha. Toda comunicação e feita via token JWT.
- `Cadastro do usuário`: Caso o usuário não possua cadastro, deverá se cadastrar para conseguir fazer login. 
- `Atualização do usuário`: Usuários autenticados podem atualizar os próprios dados de cadastro.
- `Exclusão da conta do usuário`: O usuário poderá excluir a sua conta da plataforma.

--------

## :bust_in_silhouette: Exemplos de endpoints:

### POST - api/v1/books/create

```{
  "author": "Robert C. Martin",
  "launchDate": "2008-08-01",
  "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
  "description": "A book that teaches software developers how to write clean, maintainable, and efficient code."
}
```

### GET - api/v1/books/id

```{
  "bookId": 1,
  "author": "Robert C. Martin",
  "launchDate": "2008-08-01",
  "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
  "description": "A book that teaches software developers how to write clean, maintainable, and efficient code."
}
```
 
## Como rodar a aplicação :arrow_forward:

Abra o terminal do git bash na pasta onde deseja salvar o projeto, e digite o seguinte comando: 

```
git clone git@github.com:edielson-assis/bytes4all-api.git
``` 
Após clonar o projeto, basta ter o docker instalado na sua máquina e digitar o seguinte comando no terminal:

```
docker-compose up -d
```

Isso fará com que a aplicação seja inicializada na porta 80.

Agora abra o seu navegador de internet e, na barra de endereço, digite o seguinte comando:

```
http://localhost/swagger-ui/index.html
```
Por fim, é só testar as funcionalidades do projeto. Não se esqueça de se registrar e fazer login para se autenticar na plataforma. Toda comunicação entre os endpoints é feita via token JWT.

## Linguagens, dependencias e libs utilizadas :books:

- [Java](https://docs.oracle.com/en/java/javase/21/docs/api/index.html)
- [Maven](https://maven.apache.org/ref/3.9.3/maven-core/index.html)
- [Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
- [MySQL Connector](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
- [Spring Data JPA](https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa/3.2.1)
- [Bean Validation API](https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api/3.0.2)
- [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
- [Flyway MySQL](https://mvnrepository.com/artifact/org.flywaydb/flyway-mysql/9.22.2)
- [Flyway Core](https://mvnrepository.com/artifact/org.flywaydb/flyway-core/9.22.2)
- [Spring Security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security/3.2.1)
- [Java JWT](https://mvnrepository.com/artifact/com.auth0/java-jwt/4.4.0)
- [Swagger](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui/2.3.0)
- [TestContainers](https://mvnrepository.com/artifact/org.testcontainers/mysql/1.19.5)
- [Docker](https://docs.docker.com/)

## Licença 

The [Apache License 2.0 License](https://github.com/edielson-assis/bytes4all-api/blob/main/LICENSE) (Apache License 2.0)

Copyright :copyright: 2024 - Bytes4All
