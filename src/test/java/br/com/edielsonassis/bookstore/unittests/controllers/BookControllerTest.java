package br.com.edielsonassis.bookstore.unittests.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.edielsonassis.bookstore.controllers.BookController;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.BookResponse;
import br.com.edielsonassis.bookstore.services.BookService;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService service;

    private BookResponse book;

    private static final Long PERSON_ID = 1L;
    private static final String PATH = "/api/v1/book";

    @BeforeEach
    void setup() {
        book = new BookResponse();
        book.setBookId(PERSON_ID);
        book.setAuthor("Author Test");
        book.setLaunchDate(LocalDate.now());
        book.setTitle("Title Test");
        book.setDescription("Description Test");
    }

    @Test
    @DisplayName("When create book then return BookResponse")
    void testWhenCreateBookThenReturnBookResponse() throws JsonProcessingException, Exception {
        given(service.createBook(any(BookRequest.class))).willReturn(book);

        ResultActions response = mockMvc.perform(post(PATH.concat("/create")).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)));

        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.launchDate", is(book.getLaunchDate().toString())))
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andExpect(jsonPath("$.description", is(book.getDescription())));
    }

    @Test
    @DisplayName("When find book by ID then return BookResponse")
    void testWhenFindBookByIdThenReturnBookResponse() throws JsonProcessingException, Exception {
        given(service.findBookById(PERSON_ID)).willReturn(book);

        ResultActions response = mockMvc.perform(get(PATH.concat("/get/{id}"), PERSON_ID));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.bookId", is(book.getBookId().intValue())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.launchDate", is(book.getLaunchDate().toString())))
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andExpect(jsonPath("$.description", is(book.getDescription())));
    }

    @Test
    @DisplayName("When find book by ID then throw ObjectNotFoundException")
    void testWhenFindBookByIdThenThrowObjectNotFoundException() throws JsonProcessingException, Exception {
        given(service.findBookById(PERSON_ID)).willThrow(ObjectNotFoundException.class);

        ResultActions response = mockMvc.perform(get(PATH.concat("/get/{id}"), PERSON_ID));

        response.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @DisplayName("When find all books then return BookResponse list")
    void testWhenFindAllBooksThenReturnBookResponseList() throws JsonProcessingException, Exception {
        List<BookResponse> list = List.of(book);

        given(service.findAllBooks()).willReturn(list);

        ResultActions response = mockMvc.perform(get(PATH));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.size()", is(list.size())))
                .andExpect(jsonPath("$.[0].bookId", is(list.get(0).getBookId().intValue())))
                .andExpect(jsonPath("$.[0].author", is(list.get(0).getAuthor())))
                .andExpect(jsonPath("$.[0].launchDate", is(list.get(0).getLaunchDate().toString())))
                .andExpect(jsonPath("$.[0].title", is(list.get(0).getTitle())))
                .andExpect(jsonPath("$.[0].description", is(list.get(0).getDescription())));
    }

    @Test
    @DisplayName("When update book then return BookResponse")
    void testWhenUpdateBookThenReturnBookResponse() throws JsonProcessingException, Exception {
        book.setAuthor("New author Test");
        book.setLaunchDate(LocalDate.parse("2014-08-02"));
        book.setTitle("New title Test");
        book.setDescription("New description Test");

        given(service.updateBook(any(BookUpdateRequest.class))).willReturn(book);

        ResultActions response = mockMvc.perform(put(PATH.concat("/update")).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.bookId", is(book.getBookId().intValue())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.launchDate", is(book.getLaunchDate().toString())))
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andExpect(jsonPath("$.description", is(book.getDescription())));
    }

    @Test
    @DisplayName("When delete book then throw DatabaseException")
    void testWhenDeleteBookThenThrowDatabaseException() throws JsonProcessingException, Exception {
        willDoNothing().given(service).deleteBook(PERSON_ID);

        ResultActions response = mockMvc.perform(delete(PATH.concat("/delete/{id}"), PERSON_ID));

        response.andExpect(status().isNoContent()).andDo(print());
    }
}