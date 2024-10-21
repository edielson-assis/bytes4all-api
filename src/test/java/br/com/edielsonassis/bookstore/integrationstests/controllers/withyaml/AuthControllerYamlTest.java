package br.com.edielsonassis.bookstore.integrationstests.controllers.withyaml;

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
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.edielsonassis.bookstore.config.TestConfig;
import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.integrationstests.controllers.withyaml.mapper.YMLMapper;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.UserSignupRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.TokenResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.UserResponse;
import br.com.edielsonassis.bookstore.util.MediaType;
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
public class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper objectMapper;
    private static UserSignupRequest userSignup;
	private static TokenAndRefreshTokenResponse token;
    private static final String BASE_PATH = "/api/v1/auth";
    private static final String USERNAME = "teste@email.com";

    @BeforeAll
	static void setup() {
		objectMapper = new YMLMapper();
		
		userSignup = new UserSignupRequest();
        userSignup.setFullName("Test auth");
        userSignup.setEmail("teste@email.com");
        userSignup.setPassword("1234567");
	}

    @Test
	@Order(0)
    @DisplayName("Should signup and return a UserResponse")
	void testShouldSignupAndReturnUserResponse() throws JsonMappingException, JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_3000)
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
		
        var content = given().spec(specification)
                .basePath(BASE_PATH.concat("/signup"))
                .body(userSignup, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(UserResponse.class, objectMapper);
		
		assertNotNull(content);
		assertNotNull(content);

        assertEquals("Test auth", content.getFullName());
        assertEquals("teste@email.com", content.getEmail());
	}
	
	@Test
	@Order(1)
	@DisplayName("Should perform login and return a JWT token and a refresh token")
    void testShouldPerformLoginAndReturnAJwtTokenAndARefreshToken() throws JsonMappingException, JsonProcessingException {
		UserSigninRequest userSignin = new UserSigninRequest();
        userSignin.setEmail("teste@email.com");
        userSignin.setPassword("1234567");
		
		token = given().spec(specification)
				.basePath(BASE_PATH.concat("/signin"))
				.body(userSignin, objectMapper)
                .when()
				.post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenAndRefreshTokenResponse.class, objectMapper);
		
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
                .as(TokenResponse.class, objectMapper);
		
		assertNotNull(newTokenResponse.getAccessToken());
	}

    @Test
    @Order(3)
    @DisplayName("When delete user then return no content")
    void testWhenDeleteUserThenReturnNoContent() throws JsonMappingException, JsonProcessingException {
        given().spec(specification)
            .basePath(BASE_PATH.concat("/delete"))
            .pathParam("email", USERNAME)
            .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getRefreshToken())
            .when()
            .delete("{email}")
            .then()
            .statusCode(204);
    }
}