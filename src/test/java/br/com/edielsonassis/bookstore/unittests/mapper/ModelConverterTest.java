package br.com.edielsonassis.bookstore.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.edielsonassis.bookstore.dtos.v1.response.AddressResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.PersonResponse;
import br.com.edielsonassis.bookstore.mapper.Mapper;
import br.com.edielsonassis.bookstore.model.Address;
import br.com.edielsonassis.bookstore.model.Person;
import br.com.edielsonassis.bookstore.unittests.mapper.mocks.MockPerson;

class ModelConverterTest {
    
    MockPerson inputObject;

    @BeforeEach
    void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    void parseEntityToDtoTest() {
        PersonResponse output = Mapper.parseObject(inputObject.mockEntity(), PersonResponse.class);
        
        assertEquals(Long.valueOf(0L), output.getPersonId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals(addressDto(0), output.getAddress());
        assertEquals("Male", output.getGender().getValue());
    }

    @Test
    void parseEntityListToDtoListTest() {
        List<PersonResponse> outputList = Mapper.parseListObjects(inputObject.mockEntityList(), PersonResponse.class);
        PersonResponse outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getPersonId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals(addressDto(0), outputZero.getAddress());
        assertEquals("Male", outputZero.getGender().getValue());
        
        PersonResponse outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getPersonId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals(addressDto(7), outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender().getValue());
        
        PersonResponse outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getPersonId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals(addressDto(12), outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender().getValue());
    }

    @Test
    void parseDtoToEntityTest() {
        Person output = Mapper.parseObject(inputObject.mockDto(), Person.class);
        
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals(address(0), output.getAddress());
        assertEquals("Male", output.getGender().getValue());
    }

    @Test
    void parseDtoListToEntityListTest() {
        List<Person> outputList = Mapper.parseListObjects(inputObject.mockDtoList(), Person.class);
        Person outputZero = outputList.get(0);
        
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals(address(0), outputZero.getAddress());
        assertEquals("Male", outputZero.getGender().getValue());
        
        Person outputSeven = outputList.get(7);
        
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals(address(7), outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender().getValue());
        
        Person outputTwelve = outputList.get(12);
        
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals(address(12), outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender().getValue());
    }

    @Test
    void parseUpdateDtoToEntityTest() {
        Person output = Mapper.parseObject(inputObject.mockUpdateDto(), Person.class);
        
        assertEquals(Long.valueOf(0L), output.getPersonId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals(address(0), output.getAddress());
        assertEquals("Male", output.getGender().getValue());
    }

    @Test
    void parseUpdateDtoListToEntityListTest() {
        List<Person> outputList = Mapper.parseListObjects(inputObject.mockUpdateDtoList(), Person.class);
        Person outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getPersonId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals(address(0), outputZero.getAddress());
        assertEquals("Male", outputZero.getGender().getValue());
        
        Person outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getPersonId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals(address(7), outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender().getValue());
        
        Person outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getPersonId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals(address(12), outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender().getValue());
    }

    private AddressResponse addressDto(Integer number) {
        AddressResponse addres = new AddressResponse();
        addres.setCity("City Test" + number);
        addres.setState("ST" + number);
        return addres;
    }

    private Address address(Integer number) {
        Address addres = new Address();
        addres.setCity("City Test" + number);
        addres.setState("ST" + number);
        return addres;
    }
}