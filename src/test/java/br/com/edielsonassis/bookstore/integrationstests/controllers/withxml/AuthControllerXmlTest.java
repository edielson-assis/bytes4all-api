package br.com.edielsonassis.bookstore.integrationstests.controllers.withxml;

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
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.edielsonassis.bookstore.config.TestConfig;
import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.request.UserSignupRequest;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.TokenResponse;
import br.com.edielsonassis.bookstore.integrationstests.dtos.response.UserResponse;
import br.com.edielsonassis.bookstore.util.MediaType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
	private static XmlMapper objectMapper;
    private static UserSignupRequest userSignup;
	private static TokenAndRefreshTokenResponse token;
    private static final String BASE_PATH = "/api/v1/auth";

    @BeforeAll
	static void setup() {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
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
                .setContentType(MediaType.APPLICATION_XML)
                .setAccept(MediaType.APPLICATION_XML)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
		
        var content = given().spec(specification)
                .basePath(BASE_PATH.concat("/signup"))
                .accept(MediaType.APPLICATION_XML)
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
        assertEquals("teste@email.com", user.getEmail());
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
                .accept(MediaType.APPLICATION_XML)
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
                .pathParam("username", "teste@email.com")
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
}