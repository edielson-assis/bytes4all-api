package br.com.edielsonassis.bookstore.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/person")
public class PersonController {
	
	private final PersonService service;

	@Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonVo> createPerson(@RequestBody PersonVo person) {
		var savedPerson = service.createPerson(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
	}

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonVo> findPersonById(@PathVariable(value = "id") Long id) {
		var savedPerson = service.findPersonById(id);
        return new ResponseEntity<>(savedPerson, HttpStatus.OK);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonVo>> findAllPeolple() {
		var people = service.findAllPeolple();
        return new ResponseEntity<>(people, HttpStatus.OK);
	}

	@Transactional
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonVo> updatePerson(@RequestBody PersonVo personVo) {
		var person = service.updatePerson(personVo);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}
	
	@Transactional
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletePerson(@PathVariable(value = "id") Long id) {
		service.deletePerson(id);
		return ResponseEntity.noContent().build();
	}
}