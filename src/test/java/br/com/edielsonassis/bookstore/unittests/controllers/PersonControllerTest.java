package br.com.edielsonassis.bookstore.unittests.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.edielsonassis.bookstore.BookstoreApplication;
import br.com.edielsonassis.bookstore.controllers.PersonController;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.PersonUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.AddressResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.PersonResponse;
import br.com.edielsonassis.bookstore.models.enums.Gender;
import br.com.edielsonassis.bookstore.security.JwtTokenProvider;
import br.com.edielsonassis.bookstore.security.SecurityConfig;
import br.com.edielsonassis.bookstore.services.PersonService;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;

@ContextConfiguration(classes = {BookstoreApplication.class, SecurityConfig.class, JwtTokenProvider.class})
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService service;

    @MockBean
    private UserDetailsService usuarioDetailsService;

    private PersonResponse person;

    private static final Long PERSON_ID = 1L;
    private static final String PATH = "/api/v1/people";

    @BeforeEach
    void setup() {
        person = new PersonResponse();
        person.setPersonId(PERSON_ID);
        person.setFirstName("First Name Test");
        person.setLastName("Last Name Test");
        person.setGender(Gender.MALE);
        person.setAddress(mockAddress());
    }

    @Test
    @WithMockUser
    @DisplayName("When create person then return PersonResponse")
    void testWhenCreatePersonThenReturnPersonResponse() throws JsonProcessingException, Exception {
        given(service.createPerson(any(PersonRequest.class))).willReturn(person);

        ResultActions response = mockMvc.perform(post(PATH.concat("/create")).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.gender", is(person.getGender().getValue())))
                .andExpect(jsonPath("$.address.city", is(person.getAddress().getCity())))
                .andExpect(jsonPath("$.address.state", is(person.getAddress().getState())));
    }

    @Test
    @WithMockUser
    @DisplayName("When find person by ID then return PersonResponse")
    void testWhenFindPersonByIdThenReturnPersonResponse() throws JsonProcessingException, Exception {
        given(service.findPersonById(PERSON_ID)).willReturn(person);

        ResultActions response = mockMvc.perform(get(PATH.concat("/get/{id}"), PERSON_ID));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.personId", is(person.getPersonId().intValue())))
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.gender", is(person.getGender().getValue())))
                .andExpect(jsonPath("$.address.city", is(person.getAddress().getCity())))
                .andExpect(jsonPath("$.address.state", is(person.getAddress().getState())));
    }

    @Test
    @WithMockUser
    @DisplayName("When find person by ID then throw ObjectNotFoundException")
    void testWhenFindPersonByIdThenThrowObjectNotFoundException() throws JsonProcessingException, Exception {
        given(service.findPersonById(PERSON_ID)).willThrow(ObjectNotFoundException.class);

        ResultActions response = mockMvc.perform(get(PATH.concat("/get/{id}"), PERSON_ID));

        response.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("When find person by name then return PersonResponse")
    void testWhenFindPersonByNameThenReturnPersonResponse() throws JsonProcessingException, Exception {
        Page<PersonResponse> list = new PageImpl<>(List.of(person), PageRequest.of(0, 1), 1);

        given(service.findPersonByName(anyString(), anyInt(), anyInt(), anyString())).willReturn(list);

        ResultActions response = mockMvc.perform(get(PATH.concat("/get/name/{name}"), "rst").param("page", "0").param("size", "1").param("direction", "asc"));

        response.andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.personResponseList.size()", is(list.getSize())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].personId", is(person.getPersonId().intValue())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].firstName", is(person.getFirstName())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].lastName", is(person.getLastName())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].gender", is(person.getGender().getValue())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].address.city", is(person.getAddress().getCity())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].address.state", is(person.getAddress().getState())));
    }

    @Test
    @WithMockUser
    @DisplayName("When find all people then return PersonResponse list")
    void testWhenFindAllPeopleThenReturnPersonResponseList() throws JsonProcessingException, Exception {
        Page<PersonResponse> list = new PageImpl<>(List.of(person), PageRequest.of(0, 1), 1);

        given(service.findAllPeople(anyInt(), anyInt(), anyString())).willReturn(list);

        ResultActions response = mockMvc.perform(get(PATH).param("page", "0").param("size", "1").param("direction", "asc"));

        response.andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.personResponseList.size()", is(list.getSize())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].personId", is(person.getPersonId().intValue())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].firstName", is(person.getFirstName())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].lastName", is(person.getLastName())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].gender", is(person.getGender().getValue())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].address.city", is(person.getAddress().getCity())))
            .andExpect(jsonPath("$._embedded.personResponseList[0].address.state", is(person.getAddress().getState())));
    }

    @Test
    @WithMockUser
    @DisplayName("When update person then return PersonResponse")
    void testWhenUpdatePersonThenReturnPersonResponse() throws JsonProcessingException, Exception {
        person.setFirstName("New First Name Test");
        person.setLastName("New Last Name Test");
        person.setGender(Gender.FEMALE);
        person.setAddress(mockAddress());

        given(service.updatePerson(any(PersonUpdateRequest.class))).willReturn(person);

        ResultActions response = mockMvc.perform(put(PATH.concat("/update")).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.personId", is(person.getPersonId().intValue())))
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.gender", is(person.getGender().getValue())))
                .andExpect(jsonPath("$.address.city", is(person.getAddress().getCity())))
                .andExpect(jsonPath("$.address.state", is(person.getAddress().getState())));
    }

    @Test
    @WithMockUser
    @DisplayName("When delete person then return no content")
    void testWhenDeletePersonThenReturnNoContent() throws JsonProcessingException, Exception {
        willDoNothing().given(service).deletePerson(PERSON_ID);

        ResultActions response = mockMvc.perform(delete(PATH.concat("/delete/{id}"), PERSON_ID));

        response.andExpect(status().isNoContent()).andDo(print());
    }

    private AddressResponse mockAddress() {
        AddressResponse addres = new AddressResponse();
        addres.setCity("City Test");
        addres.setState("SP");
        return addres;
    }
}