package br.com.edielsonassis.bookstore.integrationstests.controllers.withxml;

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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.integrationstests.config.TestConfig;
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
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerXmlTest extends AbstractIntegrationTest{
	
    @Autowired
    private UserRepository repository;

	private static RequestSpecification specification;
	private static XmlMapper objectMapper;
	private static PersonRequest person;
    private static final String BASE_PATH = "/api/v1/people";
    private static final String AUTH_PATH = "/api/v1/auth/signin";
    private static final Long PERSON_ID = 1L;
	
	@BeforeAll
	static void setup() {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
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
				.basePath(AUTH_PATH)
				.port(TestConfig.SERVER_PORT)
				.contentType(MediaType.APPLICATION_XML)
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
				.setContentType(MediaType.APPLICATION_XML)
				.setAccept(MediaType.APPLICATION_XML)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
    @Order(1)
	@DisplayName("When create person then return PersonResponse")
    void testWhenCreatePersonThenReturnPersonResponse() throws JsonMappingException, JsonProcessingException {		
		var content = given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_3000)
                .basePath(BASE_PATH.concat("/create"))
                .body(person)
                .when()
                .post()
				.then()
                .statusCode(201)
                .extract()
                .body()
                .asString();
		
		PersonResponse persistedPerson = objectMapper.readValue(content, PersonResponse.class);
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertTrue(persistedPerson.getPersonId() > 0);
		
		assertEquals("First Name Test", persistedPerson.getFirstName());
		assertEquals("Last Name Test", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender().getValue());
		assertEquals("City Test", persistedPerson.getAddress().getCity());
		assertEquals("ST", persistedPerson.getAddress().getState());
	}

	@Test
	@Order(2)
    @DisplayName("When create with wrong origin then status code 403")
	void testWhenCreateWithWrongOriginThenStatusCode403() throws JsonMappingException, JsonProcessingException {
       var content = given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_7000)
                .basePath(BASE_PATH.concat("/create"))
				.body(person)
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
                .asString();
		
		PersonResponse persistedPerson = objectMapper.readValue(content, PersonResponse.class);
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertTrue(persistedPerson.getPersonId() > 0);
		
		assertEquals("First Name Test", persistedPerson.getFirstName());
		assertEquals("Last Name Test", persistedPerson.getLastName());
		assertEquals("Male", persistedPerson.getGender().getValue());
		assertEquals("City Test", persistedPerson.getAddress().getCity());
		assertEquals("ST", persistedPerson.getAddress().getState());
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
                .body(updatePerson)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonResponse persistedPerson = objectMapper.readValue(content, PersonResponse.class);

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
    @Order(6)
    @DisplayName("When find person by name then return PersonResponse")
    void testWhenFindPersonByNameThenReturnPersonResponse() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
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
                .asString();

		PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
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
        var content = given().spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_3000)
                .basePath(BASE_PATH)
				.queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
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