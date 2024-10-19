package br.com.edielsonassis.bookstore.serialization.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class YamlJacksonConverter extends AbstractJackson2HttpMessageConverter {
    
    public YamlJacksonConverter() {
        super(initializer(), MediaType.parseMediaType("application/x-yaml"));
    }

    private static YAMLMapper initializer() {
        YAMLMapper yamlMapper = new YAMLMapper();
        yamlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).registerModule(new JavaTimeModule());
        return yamlMapper;
    }
}