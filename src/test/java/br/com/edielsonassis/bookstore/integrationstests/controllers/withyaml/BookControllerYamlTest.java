package br.com.edielsonassis.bookstore.integrationstests.controllers.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.integrationstests.config.TestConfig;
import br.com.edielsonassis.bookstore.integrationstests.controllers.withyaml.mapper.YMLMapper;
import br.com.edielsonassis.bookstore.integrationstests.dtos.page.PagedModelBook;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.BookRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.BookResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.models.User;
import br.com.edielsonassis.bookstore.repositories.UserRepository;
import br.com.edielsonassis.bookstore.utils.constants.MediaType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookControllerYamlTest extends AbstractIntegrationTest {
    
    @Autowired
    private UserRepository repository;

    private static RequestSpecification specification;
	private static YMLMapper objectMapper;
    private static BookRequest book;
    private static final String BASE_PATH = "/api/v1/books";
    private static final String AUTH_PATH = "/api/v1/auth/signin";
    private static final Long BOOK_ID = 1L;

    @BeforeAll
	static void setup() {
		objectMapper = new YMLMapper();
		
		book = new BookRequest();
        book.setAuthor("Author Test");
        book.setLaunchDate(LocalDate.of(2024, 10, 15));
        book.setTitle("Title Test");
        book.setDescription("Description Test");
	}

    @Test
	@Order(0)
	@DisplayName("Should perform login and return a JWT token")
    void testShouldPerformLoginAndReturnAJwtToken() throws JsonMappingException, JsonProcessingException {
        User user = new User();
        user.setFullName("Test auth");
        user.setEmail("teste@email.com");
        user.setPassword("91e2532173dc95ef503ed5ed39f7822f576a93b7c5ae41ef52b2467bd0234f089bbfd3f3f79ed7ba");

        repository.save(user);
        
		UserSigninRequest userSignin = new UserSigninRequest();
        userSignin.setEmail("teste@email.com");
        userSignin.setPassword("1234567");
		
		var accessToken = given()
				.config(RestAssuredConfig
				.config()
				.encoderConfig(EncoderConfig.encoderConfig()
				.encodeContentTypeAs(MediaType.APPLICATION_YAML, ContentType.TEXT)))
				.accept(MediaType.APPLICATION_YAML)
				.basePath(AUTH_PATH)
				.port(TestConfig.SERVER_PORT)
				.contentType(MediaType.APPLICATION_YAML)
				.body(userSignin, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenAndRefreshTokenResponse.class, objectMapper)
				.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setPort(TestConfig.SERVER_PORT)
                .setConfig(RestAssuredConfig
                .config()
                .encoderConfig(EncoderConfig.encoderConfig()
                .encodeContentTypeAs(MediaType.APPLICATION_YAML, ContentType.TEXT)))
                .setAccept(MediaType.APPLICATION_YAML)
                .setContentType(MediaType.APPLICATION_YAML)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

    @Test
    @Order(1)
	@DisplayName("When create a book then return BookResponse")
    void testWhenCreateBookThenReturnBookResponse() throws JsonMappingException, JsonProcessingException {		
		var content = given().spec(specification)
                .basePath(BASE_PATH.concat("/create"))
                .body(book, objectMapper)
                .when()
                .post()
				.then()
                .statusCode(201)
                .extract()
                .body()
                .as(BookResponse.class, objectMapper);
		
		assertNotNull(content);
		assertNotNull(content.getBookId());
		assertNotNull(content.getAuthor());
		assertNotNull(content.getTitle());
		assertNotNull(content.getDescription());
		assertNotNull(content.getLaunchDate());
		
		assertTrue(content.getBookId() > 0);
		
		assertEquals("Author Test", content.getAuthor());
        assertEquals("Title Test", content.getTitle());
        assertEquals("Description Test", content.getDescription());
        assertEquals("2024-10-15", content.getLaunchDate().toString());
	}

    @Test
	@Order(2)
	@DisplayName("When find book by ID then return BookResponse")
    void testWhenFindBookByIdThenReturnBookResponse() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .basePath(BASE_PATH.concat("/get"))
                .pathParam("id", BOOK_ID)
                .when()
                .get("{id}")
				.then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookResponse.class, objectMapper);
		
		assertNotNull(content);
		assertNotNull(content.getBookId());
		assertNotNull(content.getAuthor());
		assertNotNull(content.getTitle());
		assertNotNull(content.getDescription());
		assertNotNull(content.getLaunchDate());
		
		assertTrue(content.getBookId() > 0);
		
		assertEquals("Author Test", content.getAuthor());
        assertEquals("Title Test", content.getTitle());
        assertEquals("Description Test", content.getDescription());
        assertEquals("2024-10-15", content.getLaunchDate().toString());
	}

    @Test
    @Order(3)
    @DisplayName("When update a book then return BookResponse")
    void testWhenUpdateBookThenReturnBookResponse() throws JsonMappingException, JsonProcessingException {
        var updateBook = new BookUpdateRequest();
        updateBook.setBookId(BOOK_ID);
        updateBook.setAuthor("New Author Test");
        updateBook.setLaunchDate(LocalDate.parse("2008-08-01"));
        updateBook.setTitle("New Title Test");
        updateBook.setDescription("New Description Test");
        
        var content = given().spec(specification)
                .basePath(BASE_PATH.concat("/update"))
                .body(updateBook, objectMapper)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookResponse.class, objectMapper);

        assertNotNull(content);
        assertNotNull(content.getBookId());
        assertNotNull(content.getAuthor());
        assertNotNull(content.getTitle());
        assertNotNull(content.getDescription());
        assertNotNull(content.getLaunchDate());
        
        assertTrue(content.getBookId() > 0);
        
        assertEquals("New Author Test", content.getAuthor());
        assertEquals("New Title Test", content.getTitle());
        assertEquals("New Description Test", content.getDescription());
        assertEquals("2008-08-01", content.getLaunchDate().toString());
    }
    
    @Test
    @Order(4)
    @DisplayName("When find book by name then return BookResponse")
    void testWhenFindBookByNameThenReturnBookResponse() throws JsonMappingException, JsonProcessingException {
        var wrapper = given().spec(specification)
                .basePath(BASE_PATH)
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelBook.class, objectMapper);

        var list = wrapper.getContent();

        assertEquals(1, list.size());

        var persistedBook = list.get(0);

        assertNotNull(persistedBook);
        assertNotNull(persistedBook.getBookId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getDescription());
        assertNotNull(persistedBook.getLaunchDate());
        
        assertTrue(persistedBook.getBookId() > 0);
        
        assertEquals("New Author Test", persistedBook.getAuthor());
        assertEquals("New Title Test", persistedBook.getTitle());
        assertEquals("New Description Test", persistedBook.getDescription());
        assertEquals("2008-08-01", persistedBook.getLaunchDate().toString());
    }

    @Test
    @Order(5)
    @DisplayName("When find all books then return BookResponse list")
    void testWhenFindAllBooksThenReturnBookResponseList() throws JsonMappingException, JsonProcessingException {
        var wrapper = given().spec(specification)
                .basePath(BASE_PATH)
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelBook.class, objectMapper);

        var list = wrapper.getContent();

        assertEquals(1, list.size());

        var persistedBook = list.get(0);

        assertNotNull(persistedBook);
        assertNotNull(persistedBook.getBookId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getDescription());
        assertNotNull(persistedBook.getLaunchDate());
        
        assertTrue(persistedBook.getBookId() > 0);
        
        assertEquals("New Author Test", persistedBook.getAuthor());
        assertEquals("New Title Test", persistedBook.getTitle());
        assertEquals("New Description Test", persistedBook.getDescription());
        assertEquals("2008-08-01", persistedBook.getLaunchDate().toString());
    }

    @Test
    @Order(6)
    @DisplayName("When delete book then return no content")
    void testWhenDeleteBookThenReturnNoContent() throws JsonMappingException, JsonProcessingException {
        given().spec(specification)
            .basePath(BASE_PATH.concat("/delete"))
            .pathParam("id", BOOK_ID)
            .when()
            .delete("{id}")
            .then()
            .statusCode(204);
    }
}