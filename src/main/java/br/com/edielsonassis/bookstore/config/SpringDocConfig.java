package br.com.edielsonassis.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components()
					.addSecuritySchemes("bearer-key",
						new SecurityScheme()
							.type(SecurityScheme.Type.HTTP)
							.scheme("bearer")
							.bearerFormat("JWT")))
				.info(new Info()
					.title("Bytes4All")
					.version("v1.0.0")
					.description("RestFul API of the Bytes4All application. The API allows users to upload and download e-books")
					.termsOfService("https://edielsonassis.com.br/termos-a-serem-definidos")
					.license(new License()
							.name("Apache 2.0")
							.url("https://github.com/edielson-assis/bytes4all-api/blob/main/LICENSE"))
					.contact(new Contact()
							.name("Edielson Assis")
							.email("grizos.ed@gmail.com")));
	}
}