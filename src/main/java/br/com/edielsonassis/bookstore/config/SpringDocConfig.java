package br.com.edielsonassis.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Bytes4All")
				.version("v1")
				.description("RestFul API of the Bytes4All application. The API allows users to upload and download files")
				.termsOfService("https://edielsonassis.com.br/termos-a-serem-definidos")
				.license(new License()
						.name("Apache 2.0")
						.url("https://github.com/edielson-assis/bytes4all-api/blob/main/LICENSE"))
				.contact(new Contact()
                                .name("Edielson Assis")
                                .email("grizos.ed@gmail.com")));
	}
}