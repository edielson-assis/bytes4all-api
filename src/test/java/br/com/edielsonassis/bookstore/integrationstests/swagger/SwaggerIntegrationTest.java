package br.com.edielsonassis.bookstore.integrationstests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.integrationstests.config.TestConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("JUnit test for Should display UI Swagger page")
    void testShouldDisplaySwaggerUIPage() {
        var content = given()
            .basePath("/swagger-ui/index.html")
                .port(TestConfig.SERVER_PORT)
                    .when()
                        .get()
                            .then()
                                .statusCode(200)
                                    .extract()
                                        .body()
                                            .asString();

        assertTrue(content.contains("Swagger UI"));
    }
}