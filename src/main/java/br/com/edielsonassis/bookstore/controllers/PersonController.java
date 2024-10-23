package br.com.edielsonassis.bookstore.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.edielsonassis.bookstore.controllers.swagger.PersonControllerSwagger;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.PersonResponse;
import br.com.edielsonassis.bookstore.services.PersonService;
import br.com.edielsonassis.bookstore.utils.constants.DefaultValue;
import br.com.edielsonassis.bookstore.utils.constants.MediaType;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/people")
public class PersonController implements PersonControllerSwagger {
	
	private final PersonService service;
	private final PagedResourcesAssembler<PersonResponse> assembler;

	@Transactional
    @PostMapping(path = "/create",
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)
	@Override
	public ResponseEntity<PersonResponse> createPerson(@Valid @RequestBody PersonRequest personRequest) {
		var person = service.createPerson(personRequest);
		person.add(linkTo(methodOn(PersonController.class).findPersonById(person.getPersonId())).withSelfRel());
        return new ResponseEntity<>(person, HttpStatus.CREATED);
	}

    @GetMapping(path = "/get/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Override
	public ResponseEntity<PersonResponse> findPersonById(@PathVariable(value = "id") Long id) {
		var person = service.findPersonById(id);
		person.add(linkTo(methodOn(PersonController.class).findPersonById(id)).withSelfRel());
		person.add(linkTo(methodOn(PersonController.class).findAllPeople(DefaultValue.PAGE, DefaultValue.SIZE, DefaultValue.DIRECTION)).withRel("People list"));
        return new ResponseEntity<>(person, HttpStatus.OK);
	}

	@GetMapping(path = "/get/name/{name}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Override
	public ResponseEntity<PagedModel<EntityModel<PersonResponse>>> findPersonByName(
			@PathVariable(value = "name") String name, 
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "size", defaultValue = "10") Integer size, 
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var people = service.findPersonByName(name, page, size, direction);
		var link = linkTo(methodOn(PersonController.class).findPersonByName(name, page, size, direction)).withSelfRel();
		people.stream().forEach(person -> person.add(linkTo(methodOn(PersonController.class).findPersonByName(name, page, size, direction)).withSelfRel()));
		return new ResponseEntity<>(assembler.toModel(people, link), HttpStatus.OK);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Override
	public ResponseEntity<PagedModel<EntityModel<PersonResponse>>> findAllPeople(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "size", defaultValue = "10") Integer size, 
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var people = service.findAllPeople(page, size, direction);
		var link = linkTo(methodOn(PersonController.class).findAllPeople(page, size, direction)).withSelfRel();
		people.stream().forEach(person -> person.add(linkTo(methodOn(PersonController.class).findPersonById(person.getPersonId())).withSelfRel()));
		return new ResponseEntity<>(assembler.toModel(people, link), HttpStatus.OK);
	}

	@Transactional
	@PutMapping(path = "/update",
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)
	@Override
	public ResponseEntity<PersonResponse> updatePerson(@Valid @RequestBody PersonUpdateRequest personRequest) {
		var person = service.updatePerson(personRequest);
		person.add(linkTo(methodOn(PersonController.class).findPersonById(person.getPersonId())).withSelfRel());
		return new ResponseEntity<>(person, HttpStatus.OK);
	}
	
	@Transactional
	@DeleteMapping(path = "/delete/{id}")
	@Override
	public ResponseEntity<Void> deletePerson(@PathVariable(value = "id") Long id) {
		service.deletePerson(id);
		return ResponseEntity.noContent().build();
	}
}