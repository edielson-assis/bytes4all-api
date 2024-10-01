package br.com.edielsonassis.bookstore.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edielsonassis.bookstore.data.vo.v1.PersonVo;
import br.com.edielsonassis.bookstore.services.PersonService;
import br.com.edielsonassis.bookstore.util.MediaType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/person")
public class PersonController {
	
	private final PersonService service;

	@Transactional
    @PostMapping(
			consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	public ResponseEntity<PersonVo> createPerson(@RequestBody PersonVo personVo) {
		var person = service.createPerson(personVo);
		person.add(linkTo(methodOn(PersonController.class).findPersonById(person.getPersonId())).withSelfRel());
        return new ResponseEntity<>(person, HttpStatus.CREATED);
	}

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	public ResponseEntity<PersonVo> findPersonById(@PathVariable(value = "id") Long id) {
		var person = service.findPersonById(id);
		person.add(linkTo(methodOn(PersonController.class).findPersonById(id)).withSelfRel());
		person.add(linkTo(methodOn(PersonController.class).findAllPeople()).withRel("People List"));
        return new ResponseEntity<>(person, HttpStatus.OK);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	public ResponseEntity<List<PersonVo>> findAllPeople() {
		var people = service.findAllPeople();
		people.stream().forEach(person -> person.add(linkTo(methodOn(PersonController.class).findPersonById(person.getPersonId())).withSelfRel()));
        return new ResponseEntity<>(people, HttpStatus.OK);
	}

	@Transactional
	@PutMapping(
			consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	public ResponseEntity<PersonVo> updatePerson(@RequestBody PersonVo personVo) {
		var person = service.updatePerson(personVo);
		person.add(linkTo(methodOn(PersonController.class).findPersonById(person.getPersonId())).withSelfRel());
		return new ResponseEntity<>(person, HttpStatus.OK);
	}
	
	@Transactional
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletePerson(@PathVariable(value = "id") Long id) {
		service.deletePerson(id);
		return ResponseEntity.noContent().build();
	}
}