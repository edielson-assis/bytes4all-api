package br.com.edielsonassis.bookstore.integrationstests.controllers.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.integrationstests.config.TestConfig;
import br.com.edielsonassis.bookstore.integrationstests.controllers.withyaml.mapper.YMLMapper;
import br.com.edielsonassis.bookstore.integrationstests.dtos.page.PagedModelPerson;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.AddressRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.PersonRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.PersonResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.models.User;
import br.com.edielsonassis.bookstore.models.enums.Gender;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerYamlTest extends AbstractIntegrationTest{
	
    @Autowired
    private UserRepository repository;

	private static RequestSpecification specification;
	private static YMLMapper objectMapper;
	private static PersonRequest person;
    private static final String BASE_PATH = "/api/v1/people";
    private static final String AUTH_PATH = "/api/v1/auth/signin";
    private static final Long PERSON_ID = 1L;
	
	@BeforeAll
	static void setup() throws Exception {
		objectMapper = new YMLMapper();
		
		person = new PersonRequest();
        person.setFirstName("First Name Test");
        person.setLastName("Last Name Test");
        person.setGender(Gender.MALE);
        person.setAddress(mockAddressDto());
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
	@DisplayName("When create person then return PersonResponse")
    void testWhenCreatePersonThenReturnPersonResponse() throws JsonMappingException, JsonProcessingException {		
		var content = given().spec(specification)
				.basePath(BASE_PATH.concat("/create"))
                .body(person, objectMapper)
                .when()
                .post()
				.then()
                .statusCode(201)
                .extract()
                .body()
                .as(PersonResponse.class, objectMapper);
		
		assertNotNull(content);
		assertNotNull(content.getPersonId());
		assertNotNull(content.getFirstName());
		assertNotNull(content.getLastName());
		assertNotNull(content.getAddress());
		assertNotNull(content.getGender());
		
		assertTrue(content.getPersonId() > 0);
		
		assertEquals("First Name Test", content.getFirstName());
		assertEquals("Last Name Test", content.getLastName());
		assertEquals("Male", content.getGender().getValue());
		assertEquals("City Test", content.getAddress().getCity());
		assertEquals("ST", content.getAddress().getState());
	}

	@Test
	@Order(2)
    @DisplayName("When create with wrong origin then status code 403")
	void testWhenCreateWithWrongOriginThenStatusCode403() throws JsonMappingException, JsonProcessingException {
       var content = given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_7000)
                .basePath(BASE_PATH.concat("/create"))
				.body(person, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();
		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	@DisplayName("When find person by ID then return PersonResponse")
    void testWhenFindPersonByIdThenReturnPersonResponse() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_3000)
                .basePath(BASE_PATH.concat("/get"))
                .pathParam("id", PERSON_ID)
                .when()
                .get("{id}")
				.then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonResponse.class, objectMapper);
		
		assertNotNull(content);
		assertNotNull(content.getPersonId());
		assertNotNull(content.getFirstName());
		assertNotNull(content.getLastName());
		assertNotNull(content.getAddress());
		assertNotNull(content.getGender());
		
		assertTrue(content.getPersonId() > 0);
		
		assertEquals("First Name Test", content.getFirstName());
		assertEquals("Last Name Test", content.getLastName());
		assertEquals("Male", content.getGender().getValue());
		assertEquals("City Test", content.getAddress().getCity());
		assertEquals("ST", content.getAddress().getState());
	}
	

	@Test
	@Order(4)
	@DisplayName("When find by ID with wrong origin then status code 403")
	void testWhenFindByIdWithWrongOriginThenStatusCode403() throws JsonMappingException, JsonProcessingException {
		var content = given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_7000)
                .basePath(BASE_PATH.concat("/get"))
                .pathParam("id", PERSON_ID)
                .when()
                .get("{id}")
				.then()
                .statusCode(403)
                .extract()
                .body()
                .asString();
		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

    @Test
    @Order(5)
    @DisplayName("When update person then return PersonResponse")
    void testWhenUpdatePersonThenReturnPersonResponse() throws JsonMappingException, JsonProcessingException {
        var updatePerson = new PersonUpdateRequest();
		updatePerson.setPersonId(PERSON_ID);
		updatePerson.setFirstName("New First Name Test");
		updatePerson.setLastName("New Last Name Test");
		updatePerson.setGender(Gender.FEMALE);
		updatePerson.setAddress(mockAddressDto());
        
        var content = given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_3000)
                .basePath(BASE_PATH.concat("/update"))
                .body(updatePerson, objectMapper)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonResponse.class, objectMapper);

        assertNotNull(content);
		assertNotNull(content.getPersonId());
		assertNotNull(content.getFirstName());
		assertNotNull(content.getLastName());
		assertNotNull(content.getAddress());
		assertNotNull(content.getGender());
		
		assertTrue(content.getPersonId() > 0);
		
		assertEquals("New First Name Test", content.getFirstName());
		assertEquals("New Last Name Test", content.getLastName());
		assertEquals("Female", content.getGender().getValue());
		assertEquals("City Test", content.getAddress().getCity());
		assertEquals("ST", content.getAddress().getState());
    }
	
	@Test
    @Order(6)
    @DisplayName("When find person by name then return PersonResponse")
    void testWhenFindPersonByNameThenReturnPersonResponse() throws JsonMappingException, JsonProcessingException {
        var wrapper = given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_3000)
                .basePath(BASE_PATH.concat("/get/name/"))
				.pathParam("name", "rst")
				.queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get("{name}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelPerson.class, objectMapper);

		var list = wrapper.getContent();

        assertEquals(1, list.size());

        var persistedPerson = list.get(0);

        assertNotNull(persistedPerson);		
		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertTrue(persistedPerson.getPersonId() > 0);
		
		assertEquals("New First Name Test", persistedPerson.getFirstName());
		assertEquals("New Last Name Test", persistedPerson.getLastName());
		assertEquals("Female", persistedPerson.getGender().getValue());
		assertEquals("City Test", persistedPerson.getAddress().getCity());
		assertEquals("ST", persistedPerson.getAddress().getState());
    }

    @Test
    @Order(7)
    @DisplayName("When find all people then return PersonResponse list")
    void testWhenFindAllPeopleThenReturnPersonResponseList() throws JsonMappingException, JsonProcessingException {
        var wrapper = given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_3000)
                .basePath(BASE_PATH)
				.queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelPerson.class, objectMapper);

		var list = wrapper.getContent();

        assertEquals(1, list.size());

        var persistedPerson = list.get(0);

        assertNotNull(persistedPerson);		
		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertTrue(persistedPerson.getPersonId() > 0);
		
		assertEquals("New First Name Test", persistedPerson.getFirstName());
		assertEquals("New Last Name Test", persistedPerson.getLastName());
		assertEquals("Female", persistedPerson.getGender().getValue());
		assertEquals("City Test", persistedPerson.getAddress().getCity());
		assertEquals("ST", persistedPerson.getAddress().getState());
    }

    @Test
    @Order(8)
    @DisplayName("When delete person then return no content")
    void testWhenDeletePersonThenReturnNoContent() throws JsonMappingException, JsonProcessingException {
        given().spec(specification)
            .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_3000)
            .basePath(BASE_PATH.concat("/delete"))
            .pathParam("id", PERSON_ID)
            .when()
            .delete("{id}")
            .then()
            .statusCode(204);
    }
	
	private static AddressRequest mockAddressDto() {
        AddressRequest address = new AddressRequest();
        address.setCity("City Test");
        address.setState("ST");
        return address;
    }
}