package br.com.edielsonassis.bookstore.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.data.vo.v1.PersonVo;
import br.com.edielsonassis.bookstore.mapper.Mapper;
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

    public PersonVo createPerson(PersonVo personVo) {
        var person = Mapper.parseObject(personVo, Person.class);
        log.info("Creating a new person: {}", personVo.getFirstName());
        return Mapper.parseObject(repository.save(person), PersonVo.class);

    }

    public PersonVo findPersonById(Long id) {
        log.info("Searching for person with ID: {}", id);
        var person = repository.findById(id).orElseThrow(() -> {
            log.error("Person ID not found: {}", id);
            return new ObjectNotFoundException("Person not found");
        });
        return Mapper.parseObject(person, PersonVo.class);
    }

    public List<PersonVo> findAllPeolple() {
        log.info("Searching all people");
        return Mapper.parseListObjects(repository.findAll(), PersonVo.class);
    }

    public PersonVo updatePerson(PersonVo personVo) {
        Person person = findById(personVo.getPersonId());
        person = Mapper.parseObject(personVo, Person.class);
        log.info("Updating person with name: {}", person.getFirstName());
        return Mapper.parseObject(repository.save(person), PersonVo.class);
    }

    public void deletePerson(Long id) {
        Person person = findById(id);
        try {
            log.info("Attempting to delete person with ID: {}", person.getPersonId());
            repository.delete(person);
            log.info("Person with name: {} successfully deleted", person.getFirstName());
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete person with name: {} due to referential integrity violation", person.getFirstName(), e);
            throw new DatabaseException(e.getMessage());
        }
    }

    private Person findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Person not found"));
    }
}