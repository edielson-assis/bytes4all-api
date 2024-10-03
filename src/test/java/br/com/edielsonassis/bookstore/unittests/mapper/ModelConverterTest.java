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

public class ModelConverterTest {
    
    MockPerson inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    public void parseEntityToDtoTest() {
        PersonResponse output = Mapper.parseObject(inputObject.mockEntity(), PersonResponse.class);
        assertEquals(Long.valueOf(0L), output.getPersonId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals(addressDto(0).toString(), output.getAddress().toString());
        assertEquals("MALE", output.getGender().name());
    }

    @Test
    public void parseEntityListToDtoListTest() {
        List<PersonResponse> outputList = Mapper.parseListObjects(inputObject.mockEntityList(), PersonResponse.class);
        PersonResponse outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getPersonId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals(addressDto(0).toString(), outputZero.getAddress().toString());
        assertEquals("MALE", outputZero.getGender().name());
        
        PersonResponse outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getPersonId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals(addressDto(7).toString(), outputSeven.getAddress().toString());
        assertEquals("FEMALE", outputSeven.getGender().name());
        
        PersonResponse outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getPersonId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals(addressDto(12).toString(), outputTwelve.getAddress().toString());
        assertEquals("MALE", outputTwelve.getGender().name());
    }

    @Test
    public void parseDtoToEntityTest() {
        Person output = Mapper.parseObject(inputObject.mockDto(), Person.class);
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals(address(0).toString(), output.getAddress().toString());
        assertEquals("MALE", output.getGender().name());
    }

    @Test
    public void parseDtoListToEntityListTest() {
        List<Person> outputList = Mapper.parseListObjects(inputObject.mockDtoList(), Person.class);
        Person outputZero = outputList.get(0);
        
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals(address(0).toString(), outputZero.getAddress().toString());
        assertEquals("MALE", outputZero.getGender().name());
        
        Person outputSeven = outputList.get(7);
        
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals(address(7).toString(), outputSeven.getAddress().toString());
        assertEquals("FEMALE", outputSeven.getGender().name());
        
        Person outputTwelve = outputList.get(12);
        
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals(address(12).toString(), outputTwelve.getAddress().toString());
        assertEquals("MALE", outputTwelve.getGender().name());
    }

    @Test
    public void parseUpdateDtoToEntityTest() {
        Person output = Mapper.parseObject(inputObject.mockUpdateDto(), Person.class);
        assertEquals(Long.valueOf(0L), output.getPersonId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals(address(0).toString(), output.getAddress().toString());
        assertEquals("MALE", output.getGender().name());
    }

    @Test
    public void parseUpdateDtoListToEntityListTest() {
        List<Person> outputList = Mapper.parseListObjects(inputObject.mockUpdateDtoList(), Person.class);
        Person outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getPersonId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals(address(0).toString(), outputZero.getAddress().toString());
        assertEquals("MALE", outputZero.getGender().name());
        
        Person outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getPersonId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals(address(7).toString(), outputSeven.getAddress().toString());
        assertEquals("FEMALE", outputSeven.getGender().name());
        
        Person outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getPersonId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals(address(12).toString(), outputTwelve.getAddress().toString());
        assertEquals("MALE", outputTwelve.getGender().name());
    }

    private AddressResponse addressDto(Integer number) {
        AddressResponse addres = new AddressResponse();
        addres.setCity("City Test" + number);
        addres.setState("State Test" + number);
        return addres;
    }

    private Address address(Integer number) {
        Address addres = new Address();
        addres.setCity("City Test" + number);
        addres.setState("State Test" + number);
        return addres;
    }
}