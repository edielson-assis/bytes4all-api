package br.com.edielsonassis.bookstore.integrationstests;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.edielsonassis.bookstore.config.TestConfig;
import br.com.edielsonassis.bookstore.dtos.v1.request.AddressRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.PersonResponse;
import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.model.enums.Gender;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@Order(7)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerJsonTest extends AbstractIntegrationTest{
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static PersonRequest person;
    private static final String PATH = "/api/v1/person";
    private static final Long PERSON_ID = 1L;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = PersonRequest.builder()
                .firstName("First Name Test")
                .lastName("Last Name Test")
                .gender(Gender.MALE)
                .address(mockAddressDto()).build();
	}
	
	@Test
    @Order(1)
	@DisplayName("When create person then return PersonResponse")
    void testWhenCreatePersonThenReturnPersonResponse() throws JsonMappingException, JsonProcessingException {		
        specification(PATH.concat("/create"), TestConfig.ORIGIN_3000);

		var content = given().spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
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
	public void testWhenCreateWithWrongOriginThenStatusCode403() throws JsonMappingException, JsonProcessingException {
        specification(PATH.concat("/create"), TestConfig.ORIGIN_7000);
		
		var content = given().spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
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
        specification(PATH.concat("/get"), TestConfig.ORIGIN_3000);

		var content = given().spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
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
	public void testWhenFindByIdWithWrongOriginThenStatusCode403() throws JsonMappingException, JsonProcessingException {
		specification(PATH.concat("/get"), TestConfig.ORIGIN_7000);
		
		var content = given().spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
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
        specification(PATH.concat("/update"), TestConfig.ORIGIN_3000);

        var updatePerson = PersonUpdateRequest.builder()
                .personId(PERSON_ID)
                .firstName("New First Name Test")
                .lastName("New Last Name Test")
                .gender(Gender.FEMALE)
                .address(mockAddressDto()).build();
        
        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
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
    @DisplayName("When find all people then return PersonResponse list")
    void testWhenFindAllPeopleThenReturnPersonResponseList() throws JsonMappingException, JsonProcessingException {
        specification(PATH, TestConfig.ORIGIN_3000);

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<PersonResponse> list = Arrays.asList(objectMapper.readValue(content, PersonResponse[].class));

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
    @DisplayName("When delete person then return no content")
    void testWhenDeletePersonThenReturnNoContent() throws JsonMappingException, JsonProcessingException {
        specification(PATH.concat("/delete"), TestConfig.ORIGIN_3000);

        given().spec(specification)
            .pathParam("id", PERSON_ID)
            .when()
            .delete("{id}")
            .then()
            .statusCode(204);
    }

    private static RequestSpecification specification(String path, String origin) {
        return specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_ORIGIN, origin)
                .setBasePath(path)
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }
	
	private static AddressRequest mockAddressDto() {
        return AddressRequest.builder()
                .city("City Test")
                .state("ST").build();
    }
}