package br.com.edielsonassis.bookstore.unittests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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

import br.com.edielsonassis.bookstore.data.vo.v1.AddressVo;
import br.com.edielsonassis.bookstore.data.vo.v1.PersonVo;
import br.com.edielsonassis.bookstore.model.Person;
import br.com.edielsonassis.bookstore.repositories.PersonRepository;
import br.com.edielsonassis.bookstore.services.PersonService;
import br.com.edielsonassis.bookstore.services.exceptions.DatabaseException;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;
import br.com.edielsonassis.bookstore.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    private Person person;
    private PersonVo personVo;
    private MockPerson input;
    private static final Long PERSON_ID = 1L;

    @BeforeEach
    void setup() {
        input = new MockPerson();
        person = input.mockEntity(1);
        personVo = input.mockVO(1);
    }

    @Test
    @DisplayName("When create person then return person VO")
    void testWhenCreatePersonThenReturnPersonVO() {
        when(repository.save(any(Person.class))).thenReturn(person);

        var savedPerson = service.createPerson(personVo);

        assertNotNull(savedPerson);
        assertNotNull(savedPerson.getPersonId());
        assertNotNull(savedPerson.getLinks());

        assertEquals("First Name Test1", savedPerson.getFirstName());
		assertEquals("Last Name Test1", savedPerson.getLastName());
        assertEquals("FEMALE", savedPerson.getGender().name());
        assertEquals(addressVo(1).toString(), savedPerson.getAddress().toString());
        
        verify(repository, times(1)).save(any(Person.class));
    }

    @Test
    @DisplayName("When find person by ID then return person VO")
    void testWhenFindPersonByIdThenReturnPersonVO() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));

        var savedPerson = service.findPersonById(PERSON_ID);

        assertNotNull(savedPerson);
        assertNotNull(savedPerson.getPersonId());
        assertNotNull(savedPerson.getLinks());

        assertEquals("First Name Test1", savedPerson.getFirstName());
		assertEquals("Last Name Test1", savedPerson.getLastName());
        assertEquals("FEMALE", savedPerson.getGender().name());
        assertEquals(addressVo(1).toString(), savedPerson.getAddress().toString());
        
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("When find person by ID then throw ObjectNotFoundException")
    void testWhenFindPersonByIdThenThrowObjectNotFoundException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.findPersonById(PERSON_ID));
        
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("When find all people then return people VO list")
    void testWhenFindAllPeopleThenReturnPeopleVOList() {
        List<Person> list = input.mockEntityList(); 

        when(repository.findAll()).thenReturn(list);

        var people = service.findAllPeople();

        assertNotNull(people);
		assertEquals(14, people.size());
		
		var personOne = people.get(1);
		
		assertNotNull(personOne);
        assertNotNull(personOne.getPersonId());
        assertNotNull(personOne.getLinks());

        assertEquals("First Name Test1", personOne.getFirstName());
		assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("FEMALE", personOne.getGender().name());
        assertEquals(addressVo(1).toString(), personOne.getAddress().toString());
        
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("When update person then return person VO")
    void testWhenUpdatePersonThenReturnPersonVO() {
        Person updatedPerson = input.mockEntity(1);

        when(repository.findById(anyLong())).thenReturn(Optional.of(person));
        when(repository.save(any(Person.class))).thenReturn(updatedPerson);

        var savedPerson = service.updatePerson(personVo);

        assertNotNull(savedPerson);
        assertNotNull(savedPerson.getPersonId());
        assertNotNull(savedPerson.getLinks());

        assertEquals("First Name Test1", savedPerson.getFirstName());
		assertEquals("Last Name Test1", savedPerson.getLastName());
        assertEquals("FEMALE", savedPerson.getGender().name());
        assertEquals(addressVo(1).toString(), savedPerson.getAddress().toString());
        
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("When delete person then return no content")
    void testWhenDeletePersonThenReturnNoContent() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));
        doNothing().when(repository).delete(person);

        service.deletePerson(PERSON_ID);
        
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("When delete person then throw DatabaseException")
    void testWhenDeletePersonThenThrowDatabaseException() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));
        doThrow(new DataIntegrityViolationException("Referential integrity violation")).when(repository).delete(person);
        
        assertThrows(DatabaseException.class, () -> service.deletePerson(PERSON_ID));
        
        verify(repository, times(1)).delete(person);
    }

    private AddressVo addressVo(Integer number) {
        AddressVo addres = new AddressVo();
        addres.setStreet("Street Test" + number);
        addres.setNeighborhood("Neighborhood Test" + number);
        addres.setCity("City Test" + number);
        addres.setState("State Test" + number);
        return addres;
    }
}