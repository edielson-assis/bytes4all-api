package br.com.edielsonassis.bookstore.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.dtos.v1.request.PersonRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.PersonResponse;
import br.com.edielsonassis.bookstore.mapper.Mapper;
import br.com.edielsonassis.bookstore.models.Person;
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

    public Page<PersonResponse> findPersonByName(String name, Integer page, Integer size, String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        log.info("Searching for person with name: {}", name);
        return repository.findPersonByName(name, pageable).map(person -> Mapper.parseObject(person, PersonResponse.class));
    }

    public Page<PersonResponse> findAllPeople(Integer page, Integer size, String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        log.info("Searching all people");
        return repository.findAll(pageable).map(person -> Mapper.parseObject(person, PersonResponse.class));
    }

    public PersonResponse updatePerson(PersonUpdateRequest personRequest) {
        var person = findById(personRequest.getPersonId());
        person = Mapper.parseObject(personRequest, Person.class);
        log.info("Updating person with name: {}", person.getFirstName());
        return Mapper.parseObject(repository.save(person), PersonResponse.class);
    }

    public void deletePerson(Long id) {
        var person = findById(id);
        try {
            log.info("Attempting to delete person with ID: {}", person.getPersonId());
            repository.delete(person);
            log.info("Person with name: {} successfully deleted", person.getFirstName());
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete person with name: {} due to referential integrity violation", person.getFirstName(), e.getMessage());
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