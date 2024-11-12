package br.com.edielsonassis.bookstore.integrationstests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.integrationstests.config.TestConfig;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.BookRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.BookResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.BookUpdateResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.wrapper.WrapperBook;
import br.com.edielsonassis.bookstore.models.User;
import br.com.edielsonassis.bookstore.repositories.UserRepository;
import br.com.edielsonassis.bookstore.utils.constants.MediaType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookControllerJsonTest extends AbstractIntegrationTest {
    
    @Autowired
    private UserRepository repository;

    private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
    private static BookRequest book;
    private static final String BASE_PATH = "/api/v1/books";
    private static final String AUTH_PATH = "/api/v1/auth/signin";
    private static Long BOOK_ID = 1L;

    @BeforeAll
	static void setup() {
		objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
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
        user.setEmail("test1@email.com");
        user.setPassword("91e2532173dc95ef503ed5ed39f7822f576a93b7c5ae41ef52b2467bd0234f089bbfd3f3f79ed7ba");

        repository.save(user);
        
		UserSigninRequest userSignin = new UserSigninRequest();
        userSignin.setEmail("test1@email.com");
        userSignin.setPassword("1234567");
		
		var accessToken = given()
				.basePath(AUTH_PATH)
				.port(TestConfig.SERVER_PORT)
				.contentType(MediaType.APPLICATION_JSON)
				.body(userSignin)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenAndRefreshTokenResponse.class)
				.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setPort(TestConfig.SERVER_PORT)
				.setContentType(MediaType.APPLICATION_JSON)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

    @Test
    @Order(1)
	@DisplayName("When create a book then return BookResponse")
    void testWhenCreateBookThenReturnBookResponse() throws JsonMappingException, JsonProcessingException {
        File file = new File("src/test/resources/test.pdf");
        var content = given().spec(specification)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .basePath(BASE_PATH)
                .multiPart("file", file, MediaType.APPLICATION_PDF_VALUE)
                .param("author", book.getAuthor()) 
                .param("title", book.getTitle())
                .param("description", book.getDescription())
                .param("launchDate", book.getLaunchDate().toString())
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        BookResponse persistedBook = objectMapper.readValue(content, BookResponse.class);

        BOOK_ID = persistedBook.getBookId();

        assertNotNull(persistedBook);
        assertNotNull(persistedBook.getBookId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getDescription());
        assertNotNull(persistedBook.getLaunchDate());

        assertTrue(persistedBook.getBookId() > 0);

        assertEquals("Author Test", persistedBook.getAuthor());
        assertEquals("Title Test", persistedBook.getTitle());
        assertEquals("Description Test", persistedBook.getDescription());
        assertEquals("2024-10-15", persistedBook.getLaunchDate().toString());
    }

    @Test
    @Order(2)
    @DisplayName("When update a book then return BookResponse")
    void testWhenUpdateBookThenReturnBookResponse() throws JsonMappingException, JsonProcessingException {
        var updateBook = new BookUpdateRequest();
        updateBook.setBookId(BOOK_ID);
        updateBook.setAuthor("New Author Test");
        updateBook.setLaunchDate(LocalDate.parse("2008-08-01"));
        updateBook.setTitle("New Title Test");
        updateBook.setDescription("New Description Test");
        
        var content = given().spec(specification)
                .basePath(BASE_PATH)
                .body(updateBook)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookUpdateResponse persistedBook = objectMapper.readValue(content, BookUpdateResponse.class);

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
    @Order(3)
    @DisplayName("When find book by name then return BookResponse")
    void testWhenFindBookByNameThenReturnBookResponse() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .basePath(BASE_PATH)
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperBook wrapper = objectMapper.readValue(content, WrapperBook.class);
		var list = wrapper.getEmbedded().getBooks();

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
    @Order(4)
    @DisplayName("When find all books then return BookResponse list")
    void testWhenFindAllBooksThenReturnBookResponseList() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .basePath(BASE_PATH.concat("/get"))
                .pathParam("name", "tle")
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get("{name}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperBook wrapper = objectMapper.readValue(content, WrapperBook.class);
		var list = wrapper.getEmbedded().getBooks();

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
    @DisplayName("When delete book then return no content")
    void testWhenDeleteBookThenReturnNoContent() throws JsonMappingException, JsonProcessingException {
        given().spec(specification)
            .basePath(BASE_PATH)
            .pathParam("id", BOOK_ID)
            .when()
            .delete("{id}")
            .then()
            .statusCode(204);
    }
}