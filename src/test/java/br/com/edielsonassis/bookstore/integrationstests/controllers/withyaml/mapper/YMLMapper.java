package br.com.edielsonassis.bookstore.integrationstests.controllers.withyaml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YMLMapper implements ObjectMapper {
	
	private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
	protected TypeFactory typeFactory;

	public YMLMapper() {
		objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.registerModule(new JavaTimeModule());
		typeFactory = TypeFactory.defaultInstance();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object deserialize(ObjectMapperDeserializationContext context) {
		try {
			String dataToDeserialize = context.getDataToDeserialize().asString();
			Class type = (Class) context.getType();
            log.info("Trying to deserialize object of type: " + type.getName());
			return objectMapper.readValue(dataToDeserialize, typeFactory.constructType(type));
		} catch (JsonMappingException e) {
            log.error("Error deserializing object");
			e.printStackTrace();
		} catch (JsonProcessingException e) {
            log.error("Error deserializing object");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object serialize(ObjectMapperSerializationContext context) {
		try {
            log.info("Trying serialize object of type" + context);
			return objectMapper.writeValueAsString(context.getObjectToSerialize());
		} catch (JsonProcessingException e) {
            log.error("Error serializing object");
			e.printStackTrace();
		}
		return null;
	}
}