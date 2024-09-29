package br.com.edielsonassis.bookstore.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.model.Person;
import br.com.edielsonassis.bookstore.repositories.PersonRepository;
import br.com.edielsonassis.bookstore.services.exceptions.DatabaseException;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class PersonService {
    
    private final PersonRepository repository;

    public Person createPerson(Person person) {
        log.info("Registering a new person: {}", person.getFirstName());
        return repository.save(person);
    }

    public Person findPersonById(Long id) {
        log.info("Searching for person with ID: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.error("Person ID not found: {}", id);
            return new ObjectNotFoundException("Person not found");
        });
    }

    public Page<Person> findAllPeolple(Pageable pageable) {
        log.info("Searching all people");
        return repository.findAll(pageable);
    }

    public void deletePerson(Long id) {
        Person person = findPersonById(id);
        try {
            log.info("Attempting to delete person with ID: {}", person.getPersonId());
            repository.delete(person);
            log.info("Person with name: {} successfully deleted", person.getFirstName());
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete person with name: {} due to referential integrity violation", person.getFirstName(), e);
            throw new DatabaseException(e.getMessage());
        }
    }
}