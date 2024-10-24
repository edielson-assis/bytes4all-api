package br.com.edielsonassis.bookstore.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import br.com.edielsonassis.bookstore.dtos.v1.request.AddressRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.models.Address;
import br.com.edielsonassis.bookstore.models.Person;
import br.com.edielsonassis.bookstore.models.enums.Gender;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0);
    }
    
    public PersonRequest mockDto() {
        return mockDto(0);
    }

    public PersonUpdateRequest mockUpdateDto() {
        return mockUpdateDto(0);
    }
    
    public Page<Person> mockEntityList(int page, int size) {
        List<Person> people = new ArrayList<>();
    
        for (int i = 0; i < 14; i++) {
            people.add(mockEntity(i));
        }
        int start = Math.min(page * size, people.size());
        int end = Math.min(start + size, people.size());
        List<Person> sublist = people.subList(start, end);
        return new PageImpl<>(sublist, PageRequest.of(page, size), people.size());
    }

    public List<PersonRequest> mockDtoList() {
        List<PersonRequest> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockDto(i));
        }
        return persons;
    }

    public List<PersonUpdateRequest> mockUpdateDtoList() {
        List<PersonUpdateRequest> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockUpdateDto(i));
        }
        return persons;
    }
    
    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setPersonId(number.longValue());
        person.setFirstName("First Name Test" + number);
        person.setLastName("Last Name Test" + number);
        person.setGender(((number % 2)==0) ? Gender.MALE : Gender.FEMALE);
        person.setAddress(mockAddress(number));
        return person;
    }

    public PersonRequest mockDto(Integer number) {
        PersonRequest person = new PersonRequest();
        person.setFirstName("First Name Test" + number);
        person.setLastName("Last Name Test" + number);
        person.setGender(((number % 2)==0) ? Gender.MALE : Gender.FEMALE);
        person.setAddress(mockAddressDto(number));
        return person;
    }

    public PersonUpdateRequest mockUpdateDto(Integer number) {
        PersonUpdateRequest person = new PersonUpdateRequest();
        person.setPersonId(number.longValue());
        person.setFirstName("First Name Test" + number);
        person.setLastName("Last Name Test" + number);
        person.setGender(((number % 2)==0) ? Gender.MALE : Gender.FEMALE);
        person.setAddress(mockAddressDto(number));
        return person;
    }

    private AddressRequest mockAddressDto(Integer number) {
        AddressRequest address = new AddressRequest();
        address.setCity("City Test" + number);
        address.setState("ST" + number);
        return address;
    }

    private Address mockAddress(Integer number) {
        Address address = new Address();
        address.setCity("City Test" + number);
        address.setState("ST" + number);
        return address;
    }
}