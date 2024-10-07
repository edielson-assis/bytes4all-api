package br.com.edielsonassis.bookstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowedMappings}")
    private String allowedMappings;

    @Value("${cors.allowedOrigins}")
	private String allowedOrigins;

    @Value("${cors.allowedMethods}")
    private String allowedMethods;

    @Value("${cors.allowedHeaders}")
    private String allowedHeaders;

    @Value("${cors.maxAge}")
    private Integer maxAge;
    
    @Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping(allowedMappings)
            .allowedOrigins(allowedOrigins.split(","))
			.allowedMethods(allowedMethods.split(","))
            .allowedHeaders(allowedHeaders.split(","))
		    .allowCredentials(true)
            .maxAge(maxAge);
	}
}