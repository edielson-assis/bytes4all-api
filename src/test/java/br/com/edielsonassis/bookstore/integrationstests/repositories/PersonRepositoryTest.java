package br.com.edielsonassis.bookstore.integrationstests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.models.Address;
import br.com.edielsonassis.bookstore.models.Person;
import br.com.edielsonassis.bookstore.models.enums.Gender;
import br.com.edielsonassis.bookstore.repositories.PersonRepository;
import br.com.edielsonassis.bookstore.utils.constants.DefaultValue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {
	
	@Autowired
	private PersonRepository repository;
	
	private static Person person;
	
	@BeforeAll
	static void setup() {
		person = new Person();
        person.setFirstName("First Name Test");
        person.setLastName("Last Name Test");
        person.setGender(Gender.MALE);
        person.setAddress(mockAddress());
	}
	
	@Test
	@DisplayName("When find person by name then return Person")
    void testWhenFindPersonByNameThenReturnPerson()  throws JsonMappingException, JsonProcessingException {
		repository.save(person);
		Pageable pageable = PageRequest.of(DefaultValue.PAGE, DefaultValue.SIZE, Sort.by(Direction.ASC, "firstName"));
		var savedPerson = repository.findPersonByName("rst", pageable).getContent().get(0);
		
		assertNotNull(savedPerson.getPersonId());
		assertNotNull(savedPerson.getFirstName());
		assertNotNull(savedPerson.getLastName());
		assertNotNull(savedPerson.getAddress());
		assertNotNull(savedPerson.getGender());
		
		assertEquals(1, savedPerson.getPersonId());
		assertEquals("First Name Test", savedPerson.getFirstName());
		assertEquals("Last Name Test", savedPerson.getLastName());
		assertEquals("Male", savedPerson.getGender().getValue());
		assertEquals("City Test", savedPerson.getAddress().getCity());
		assertEquals("ST", savedPerson.getAddress().getState());
	}

    private static Address mockAddress() {
        Address addres = new Address();
        addres.setCity("City Test");
        addres.setState("ST");
        return addres;
    }
}