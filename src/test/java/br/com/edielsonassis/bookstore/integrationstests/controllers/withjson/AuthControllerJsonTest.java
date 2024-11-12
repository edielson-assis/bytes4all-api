package br.com.edielsonassis.bookstore.integrationstests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.integrationstests.config.TestConfig;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.UserSignupRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.TokenResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.UserResponse;
import br.com.edielsonassis.bookstore.utils.constants.MediaType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
    private static UserSignupRequest userSignup;
	private static TokenAndRefreshTokenResponse token;
    private static final String BASE_PATH = "/api/v1/auth";
    private static final String USERNAME = "test@email.com";

    @BeforeAll
	static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		userSignup = new UserSignupRequest();
        userSignup.setFullName("Test auth");
        userSignup.setEmail("test@email.com");
        userSignup.setPassword("1234567");
	}

    @Test
	@Order(0)
    @DisplayName("Should signup and return a UserResponse")
	void testShouldSignupAndReturnUserResponse() throws JsonMappingException, JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_3000)
                .setContentType(MediaType.APPLICATION_JSON)
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
		
        var content = given().spec(specification)
                .basePath(BASE_PATH.concat("/signup"))
                .body(userSignup)
                .when()
                .post()
				.then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        UserResponse user = objectMapper.readValue(content, UserResponse.class);
		
		assertNotNull(user);
		assertNotNull(user);

        assertEquals("Test auth", user.getFullName());
        assertEquals("test@email.com", user.getEmail());
	}
	
	@Test
	@Order(1)
	@DisplayName("Should perform login and return a JWT token and a refresh token")
    void testShouldPerformLoginAndReturnAJwtTokenAndARefreshToken() throws JsonMappingException, JsonProcessingException {
		UserSigninRequest userSignin = new UserSigninRequest();
        userSignin.setEmail("test@email.com");
        userSignin.setPassword("1234567");
		
		token = given().spec(specification)
				.basePath(BASE_PATH.concat("/signin"))
				.body(userSignin)
                .when()
				.post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenAndRefreshTokenResponse.class);
		
		assertNotNull(token.getAccessToken());
		assertNotNull(token.getRefreshToken());
	}
	
	@Test
	@Order(2)
    @DisplayName("Should refresh a JWT token")
	void testShouldRefreshAJWTToken() throws JsonMappingException, JsonProcessingException {
		var newTokenResponse = given().spec(specification)
				.basePath(BASE_PATH.concat("/refresh"))
                .pathParam("username", USERNAME)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
				.when()
                .get("{username}")
				.then()
                .statusCode(200)
				.extract()
                .body()
                .as(TokenResponse.class);
		
		assertNotNull(newTokenResponse.getAccessToken());
	}

    @Test
    @Order(3)
    @DisplayName("When delete user then return no content")
    void testWhenDeleteUserThenReturnNoContent() throws JsonMappingException, JsonProcessingException {
        given().spec(specification)
            .basePath(BASE_PATH)
            .pathParam("email", USERNAME)
            .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
            .when()
            .delete("{email}")
            .then()
            .statusCode(204);
    }
}