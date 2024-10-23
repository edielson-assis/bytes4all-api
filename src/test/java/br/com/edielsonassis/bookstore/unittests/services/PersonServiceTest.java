package br.com.edielsonassis.bookstore.unittests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.edielsonassis.bookstore.dtos.v1.request.PersonRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.AddressResponse;
import br.com.edielsonassis.bookstore.model.Person;
import br.com.edielsonassis.bookstore.repositories.PersonRepository;
import br.com.edielsonassis.bookstore.services.PersonService;
import br.com.edielsonassis.bookstore.services.exceptions.DataBaseException;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;
import br.com.edielsonassis.bookstore.unittests.mapper.mocks.MockPerson;
import br.com.edielsonassis.bookstore.utils.constants.DefaultValue;

@TestInstance(Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    private Person person;
    private PersonRequest personRequest;
    private MockPerson input;
    private static final Long PERSON_ID = 1L;
    private static final Integer NUMBER_ONE = 1;

    @BeforeEach
    void setup() {
        input = new MockPerson();
        person = input.mockEntity(NUMBER_ONE);
        personRequest = input.mockDto(NUMBER_ONE);
    }

    @Test
    @DisplayName("When create person then return PersonResponse")
    void testWhenCreatePersonThenReturnPersonResponse() {
        when(repository.save(any(Person.class))).thenReturn(person);

        var savedPerson = service.createPerson(personRequest);

        assertNotNull(savedPerson);
        assertNotNull(savedPerson.getPersonId());
        assertNotNull(savedPerson.getLinks());

        assertEquals("First Name Test1", savedPerson.getFirstName());
		assertEquals("Last Name Test1", savedPerson.getLastName());
        assertEquals("Female", savedPerson.getGender().getValue());
        assertEquals(address(NUMBER_ONE), savedPerson.getAddress().toString());
        
        verify(repository, times(NUMBER_ONE)).save(any(Person.class));
    }

    @Test
    @DisplayName("When find person by ID then return PersonResponse")
    void testWhenFindPersonByIdThenReturnPersonResponse() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));

        var savedPerson = service.findPersonById(PERSON_ID);

        assertNotNull(savedPerson);
        assertNotNull(savedPerson.getPersonId());
        assertNotNull(savedPerson.getLinks());

        assertEquals("First Name Test1", savedPerson.getFirstName());
		assertEquals("Last Name Test1", savedPerson.getLastName());
        assertEquals("Female", savedPerson.getGender().getValue());
        assertEquals(address(NUMBER_ONE), savedPerson.getAddress().toString());
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
    }

    @Test
    @DisplayName("When find person by ID then throw ObjectNotFoundException")
    void testWhenFindPersonByIdThenThrowObjectNotFoundException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.findPersonById(PERSON_ID));
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
    }

    @Test
    @DisplayName("When find person by name then return PersonResponse")
    void testWhenFindPersonByNameThenReturnPersonResponse() {
        Page<Person> list = input.mockEntityList(DefaultValue.PAGE, DefaultValue.SIZE); 

        when(repository.findPersonByName(anyString(), any(Pageable.class))).thenReturn(list);

        var people = service.findPersonByName("rst", DefaultValue.PAGE, DefaultValue.SIZE, DefaultValue.DIRECTION);

        assertNotNull(people);
		assertEquals(10, people.getSize());
		
		var personOne = people.get().toList().get(NUMBER_ONE);
		
		assertNotNull(personOne);
        assertNotNull(personOne.getPersonId());
        assertNotNull(personOne.getLinks());

        assertEquals("First Name Test1", personOne.getFirstName());
		assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender().getValue());
        assertEquals(address(NUMBER_ONE), personOne.getAddress().toString());
        
        verify(repository, times(NUMBER_ONE)).findPersonByName(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("When find all people then return PersonResponse list")
    void testWhenFindAllPeopleThenReturnPersonResponseList() {
        Page<Person> list = input.mockEntityList(DefaultValue.PAGE, DefaultValue.SIZE); 

        when(repository.findAll(any(Pageable.class))).thenReturn(list);

        var people = service.findAllPeople(DefaultValue.PAGE, DefaultValue.SIZE, DefaultValue.DIRECTION);

        assertNotNull(people);
		assertEquals(10, people.getSize());
		
		var personOne = people.get().toList().get(NUMBER_ONE);
		
		assertNotNull(personOne);
        assertNotNull(personOne.getPersonId());
        assertNotNull(personOne.getLinks());

        assertEquals("First Name Test1", personOne.getFirstName());
		assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender().getValue());
        assertEquals(address(NUMBER_ONE), personOne.getAddress().toString());
        
        verify(repository, times(NUMBER_ONE)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("When update person then return PersonResponse")
    void testWhenUpdatePersonThenReturnPersonResponse() {
        Person updatedPerson = input.mockEntity(NUMBER_ONE);
        PersonUpdateRequest personRequest = input.mockUpdateDto(NUMBER_ONE);

        when(repository.findById(anyLong())).thenReturn(Optional.of(person));
        when(repository.save(any(Person.class))).thenReturn(updatedPerson);

        var savedPerson = service.updatePerson(personRequest);

        assertNotNull(savedPerson);
        assertNotNull(savedPerson.getPersonId());
        assertNotNull(savedPerson.getLinks());

        assertEquals("First Name Test1", savedPerson.getFirstName());
		assertEquals("Last Name Test1", savedPerson.getLastName());
        assertEquals("Female", savedPerson.getGender().getValue());
        assertEquals(address(NUMBER_ONE), savedPerson.getAddress().toString());
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
        verify(repository, times(NUMBER_ONE)).save(any(Person.class));
    }

    @Test
    @DisplayName("When delete person then return no content")
    void testWhenDeletePersonThenReturnNoContent() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));
        doNothing().when(repository).delete(person);

        service.deletePerson(PERSON_ID);
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
        verify(repository, times(NUMBER_ONE)).delete(person);
    }

    @Test
    @DisplayName("When delete person then throw DatabaseException")
    void testWhenDeletePersonThenThrowDatabaseException() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));
        doThrow(new DataIntegrityViolationException("Referential integrity violation")).when(repository).delete(person);
        
        assertThrows(DataBaseException.class, () -> service.deletePerson(PERSON_ID));
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
        verify(repository, times(NUMBER_ONE)).delete(person);
    }

    private String address(Integer number) {
        AddressResponse addres = new AddressResponse();
        addres.setCity("City Test" + number);
        addres.setState("ST" + number);
        return addres.toString();
    }
}