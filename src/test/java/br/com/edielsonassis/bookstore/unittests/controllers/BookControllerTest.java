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

import java.time.LocalDate;
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
import br.com.edielsonassis.bookstore.controllers.BookController;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.BookResponse;
import br.com.edielsonassis.bookstore.security.JwtTokenProvider;
import br.com.edielsonassis.bookstore.security.SecurityConfig;
import br.com.edielsonassis.bookstore.services.BookService;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;

@ContextConfiguration(classes = {BookstoreApplication.class, SecurityConfig.class, JwtTokenProvider.class})
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService service;

    @MockBean
    private UserDetailsService usuarioDetailsService;

    private BookResponse book;

    private static final Long BOOK_ID = 1L;
    private static final String PATH = "/api/v1/books";

    @BeforeEach
    void setup() {
        book = new BookResponse();
        book.setBookId(BOOK_ID);
        book.setAuthor("Author Test");
        book.setLaunchDate(LocalDate.now());
        book.setTitle("Title Test");
        book.setDescription("Description Test");
    }

    @Test
    @WithMockUser
    @DisplayName("When create a book then return BookResponse")
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
    @WithMockUser
    @DisplayName("When find book by ID then return BookResponse")
    void testWhenFindBookByIdThenReturnBookResponse() throws JsonProcessingException, Exception {
        given(service.findBookById(BOOK_ID)).willReturn(book);

        ResultActions response = mockMvc.perform(get(PATH.concat("/get/{id}"), BOOK_ID));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.bookId", is(book.getBookId().intValue())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.launchDate", is(book.getLaunchDate().toString())))
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andExpect(jsonPath("$.description", is(book.getDescription())));
    }

    @Test
    @WithMockUser
    @DisplayName("When find book by ID then throw ObjectNotFoundException")
    void testWhenFindBookByIdThenThrowObjectNotFoundException() throws JsonProcessingException, Exception {
        given(service.findBookById(BOOK_ID)).willThrow(ObjectNotFoundException.class);

        ResultActions response = mockMvc.perform(get(PATH.concat("/get/{id}"), BOOK_ID));

        response.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("When find book by name then return BookResponse")
    void testWhenFindBookByNameThenReturnBookResponse() throws JsonProcessingException, Exception {
        Page<BookResponse> list = new PageImpl<>(List.of(book), PageRequest.of(0, 1), 1);

        given(service.findBookByName(anyString(), anyInt(), anyInt(), anyString())).willReturn(list);

        ResultActions response = mockMvc.perform(get(PATH.concat("/get/name/{name}"), "tle").param("page", "0")  .param("size", "1").param("direction", "asc"));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$._embedded.bookResponseList.size()", is(list.getSize())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].bookId", is(book.getBookId().intValue())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].author", is(book.getAuthor())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].launchDate", is(book.getLaunchDate().toString())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].title", is(book.getTitle())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].description", is(book.getDescription())));
    }

    @Test
    @WithMockUser
    @DisplayName("When find all books then return BookResponse list")
    void testWhenFindAllBooksThenReturnBookResponseList() throws JsonProcessingException, Exception {
        Page<BookResponse> list = new PageImpl<>(List.of(book), PageRequest.of(0, 1), 1);

        given(service.findAllBooks(anyInt(), anyInt(), anyString())).willReturn(list);

        ResultActions response = mockMvc.perform(get(PATH).param("page", "0")  .param("size", "1").param("direction", "asc"));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$._embedded.bookResponseList.size()", is(list.getSize())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].bookId", is(book.getBookId().intValue())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].author", is(book.getAuthor())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].launchDate", is(book.getLaunchDate().toString())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].title", is(book.getTitle())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].description", is(book.getDescription())));
    }

    @Test
    @WithMockUser
    @DisplayName("When update a book then return BookResponse")
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
    @WithMockUser
    @DisplayName("When delete book then return no content")
    void testWhenDeleteBookThenReturnNoContent() throws JsonProcessingException, Exception {
        willDoNothing().given(service).deleteBook(BOOK_ID);

        ResultActions response = mockMvc.perform(delete(PATH.concat("/delete/{id}"), BOOK_ID));

        response.andExpect(status().isNoContent()).andDo(print());
    }
}