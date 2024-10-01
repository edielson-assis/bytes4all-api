package br.com.edielsonassis.bookstore.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.edielsonassis.bookstore.data.vo.v1.AddressVo;
import br.com.edielsonassis.bookstore.data.vo.v1.PersonVo;
import br.com.edielsonassis.bookstore.model.Address;
import br.com.edielsonassis.bookstore.model.Person;
import br.com.edielsonassis.bookstore.model.enums.Gender;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0);
    }
    
    public PersonVo mockVO() {
        return mockVO(0);
    }
    
    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonVo> mockVOList() {
        List<PersonVo> persons = new ArrayList<PersonVo>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }
    
    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setAddress(mockAddress(number));
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? Gender.MALE : Gender.FEMALE);
        person.setPersonId(number.longValue());
        person.setLastName("Last Name Test" + number);
        return person;
    }

    public PersonVo mockVO(Integer number) {
        PersonVo person = new PersonVo();
        person.setAddress(mockAddressVo(number));
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? Gender.MALE : Gender.FEMALE);
        person.setPersonId(number.longValue());
        person.setLastName("Last Name Test" + number);
        return person;
    }

    private AddressVo mockAddressVo(Integer number) {
        AddressVo addres = new AddressVo();
        addres.setStreet("Street Test" + number);
        addres.setNeighborhood("Neighborhood Test" + number);
        addres.setCity("City Test" + number);
        addres.setState("State Test" + number);
        return addres;
    }

    private Address mockAddress(Integer number) {
        Address addres = new Address();
        addres.setStreet("Street Test" + number);
        addres.setNeighborhood("Neighborhood Test" + number);
        addres.setCity("City Test" + number);
        addres.setState("State Test" + number);
        return addres;
    }
}