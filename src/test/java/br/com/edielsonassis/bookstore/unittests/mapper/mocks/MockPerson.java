package br.com.edielsonassis.bookstore.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.edielsonassis.bookstore.dtos.v1.request.AddressRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.model.Address;
import br.com.edielsonassis.bookstore.model.Person;
import br.com.edielsonassis.bookstore.model.enums.Gender;

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
    
    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
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
        person.setAddress(mockAddress(number));
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? Gender.MALE : Gender.FEMALE);
        person.setPersonId(number.longValue());
        person.setLastName("Last Name Test" + number);
        return person;
    }

    public PersonRequest mockDto(Integer number) {
        return PersonRequest.builder()
                .firstName("First Name Test" + number)
                .lastName("Last Name Test" + number)
                .gender(((number % 2)==0) ? Gender.MALE : Gender.FEMALE)
                .address(mockAddressDto(number)).build();
    }

    public PersonUpdateRequest mockUpdateDto(Integer number) {
        return PersonUpdateRequest.builder()
                .personId(number.longValue())
                .firstName("First Name Test" + number)
                .lastName("Last Name Test" + number)
                .gender(((number % 2)==0) ? Gender.MALE : Gender.FEMALE)
                .address(mockAddressDto(number)).build();
    }

    private AddressRequest mockAddressDto(Integer number) {
        return AddressRequest.builder()
                .city("City Test" + number)
                .state("ST" + number).build();
    }

    private Address mockAddress(Integer number) {
        Address addres = new Address();
        addres.setCity("City Test" + number);
        addres.setState("ST" + number);
        return addres;
    }
}