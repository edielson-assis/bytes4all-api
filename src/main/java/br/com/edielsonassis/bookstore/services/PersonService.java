package br.com.edielsonassis.bookstore.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.dtos.v1.request.PersonRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.PersonResponse;
import br.com.edielsonassis.bookstore.mapper.Mapper;
import br.com.edielsonassis.bookstore.model.Person;
import br.com.edielsonassis.bookstore.repositories.PersonRepository;
import br.com.edielsonassis.bookstore.services.exceptions.DataBaseException;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class PersonService {
    
    private final PersonRepository repository;

    public PersonResponse createPerson(PersonRequest personRequest) {
        var person = Mapper.parseObject(personRequest, Person.class);
        log.info("Creating a new person: {}", personRequest.getFirstName());
        return Mapper.parseObject(repository.save(person), PersonResponse.class);

    }

    public PersonResponse findPersonById(Long id) {
        log.info("Searching for person with ID: {}", id);
        var person = findById(id);
        return Mapper.parseObject(person, PersonResponse.class);
    }

    public List<PersonResponse> findAllPeople() {
        log.info("Searching all people");
        return Mapper.parseListObjects(repository.findAll(), PersonResponse.class);
    }

    public PersonResponse updatePerson(PersonUpdateRequest personRequest) {
        Person person = findById(personRequest.getPersonId());
        person = Mapper.parseObject(personRequest, Person.class);
        log.info("Updating person with name: {}", person.getFirstName());
        return Mapper.parseObject(repository.save(person), PersonResponse.class);
    }

    public void deletePerson(Long id) {
        Person person = findById(id);
        try {
            log.info("Attempting to delete person with ID: {}", person.getPersonId());
            repository.delete(person);
            log.info("Person with name: {} successfully deleted", person.getFirstName());
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete person with name: {} due to referential integrity violation", person.getFirstName(), e);
            throw new DataBaseException(e.getMessage());
        }
    }

    private Person findById(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Person ID not found: {}", id);
            return new ObjectNotFoundException("Person not found");
        });
    }
}