package br.com.edielsonassis.bookstore.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.edielsonassis.bookstore.data.vo.v1.AddressVo;
import br.com.edielsonassis.bookstore.data.vo.v1.PersonVo;
import br.com.edielsonassis.bookstore.mapper.Mapper;
import br.com.edielsonassis.bookstore.model.Address;
import br.com.edielsonassis.bookstore.model.Person;

public class ModelConverterTest {
    
    MockPerson inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    public void parseEntityToVOTest() {
        PersonVo output = Mapper.parseObject(inputObject.mockEntity(), PersonVo.class);
        assertEquals(Long.valueOf(0L), output.getPersonId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals(addressVo(0).toString(), output.getAddress().toString());
        assertEquals("MALE", output.getGender().name());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<PersonVo> outputList = Mapper.parseListObjects(inputObject.mockEntityList(), PersonVo.class);
        PersonVo outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getPersonId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals(addressVo(0).toString(), outputZero.getAddress().toString());
        assertEquals("MALE", outputZero.getGender().name());
        
        PersonVo outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getPersonId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals(addressVo(7).toString(), outputSeven.getAddress().toString());
        assertEquals("FEMALE", outputSeven.getGender().name());
        
        PersonVo outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getPersonId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals(addressVo(12).toString(), outputTwelve.getAddress().toString());
        assertEquals("MALE", outputTwelve.getGender().name());
    }

    @Test
    public void parseVOToEntityTest() {
        Person output = Mapper.parseObject(inputObject.mockVO(), Person.class);
        assertEquals(Long.valueOf(0L), output.getPersonId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals(address(0).toString(), output.getAddress().toString());
        assertEquals("MALE", output.getGender().name());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Person> outputList = Mapper.parseListObjects(inputObject.mockVOList(), Person.class);
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

    private AddressVo addressVo(Integer number) {
        AddressVo addres = new AddressVo();
        addres.setStreet("Street Test" + number);
        addres.setNeighborhood("Neighborhood Test" + number);
        addres.setCity("City Test" + number);
        addres.setState("State Test" + number);
        return addres;
    }

    private Address address(Integer number) {
        Address addres = new Address();
        addres.setStreet("Street Test" + number);
        addres.setNeighborhood("Neighborhood Test" + number);
        addres.setCity("City Test" + number);
        addres.setState("State Test" + number);
        return addres;
    }
}