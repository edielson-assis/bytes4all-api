package br.com.edielsonassis.bookstore.unittests.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import org.springframework.mock.web.MockMultipartFile;
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
    private static final String DOWNLOAD_URL = "http://api/v1/books";

    @BeforeEach
    void setup() {
        book = new BookResponse();
        book.setBookId(BOOK_ID);
        book.setAuthor("Author Test");
        book.setLaunchDate(LocalDate.now());
        book.setTitle("Title Test");
        book.setDescription("Description Test");
        book.setDownloadUrl(DOWNLOAD_URL);
    }

    @Test
    @WithMockUser
    @DisplayName("When create a book then return BookResponse")
    void testWhenCreateBookThenReturnBookResponse() throws Exception {
        given(service.createBook(any(BookRequest.class))).willReturn(book);
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "Some PDF content".getBytes());

        ResultActions response = mockMvc.perform(multipart(PATH)
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("author", book.getAuthor())
                .param("title", book.getTitle())
                .param("description", book.getDescription())
                .param("launchDate", book.getLaunchDate().toString()));

        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId", is(book.getBookId().intValue())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.launchDate", is(book.getLaunchDate().toString())))
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andExpect(jsonPath("$.description", is(book.getDescription())))
                .andExpect(jsonPath("$.downloadUrl", is(book.getDownloadUrl())));
    }

    @Test
    @WithMockUser
    @DisplayName("When find book by name then return BookResponse")
    void testWhenFindBookByNameThenReturnBookResponse() throws JsonProcessingException, Exception {
        Page<BookResponse> list = new PageImpl<>(List.of(book), PageRequest.of(0, 1), 1);

        given(service.findBookByName(anyString(), anyInt(), anyInt(), anyString())).willReturn(list);

        ResultActions response = mockMvc.perform(get(PATH.concat("/get/{name}"), "tle").param("page", "0")  .param("size", "1").param("direction", "asc"));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$._embedded.bookResponseList.size()", is(list.getSize())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].bookId", is(book.getBookId().intValue())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].author", is(book.getAuthor())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].launchDate", is(book.getLaunchDate().toString())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].title", is(book.getTitle())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].description", is(book.getDescription())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].downloadUrl", is(book.getDownloadUrl())));
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
                .andExpect(jsonPath("$._embedded.bookResponseList[0].description", is(book.getDescription())))
                .andExpect(jsonPath("$._embedded.bookResponseList[0].downloadUrl", is(book.getDownloadUrl())));
    }

    @Test
    @WithMockUser
    @DisplayName("When update a book then return BookResponse")
    void testWhenUpdateBookThenReturnBookResponse() throws JsonProcessingException, Exception {
        var bookResponse = new BookResponse();
        bookResponse.setBookId(BOOK_ID);
        bookResponse.setAuthor("New author Test");
        bookResponse.setLaunchDate(LocalDate.parse("2014-08-02"));
        bookResponse.setTitle("New title Test");
        bookResponse.setDescription("New description Test");
        bookResponse.setDownloadUrl(DOWNLOAD_URL);

        given(service.updateBook(any(BookUpdateRequest.class))).willReturn(bookResponse);

        ResultActions response = mockMvc.perform(put(PATH).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookResponse)));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.bookId", is(bookResponse.getBookId().intValue())))
                .andExpect(jsonPath("$.author", is(bookResponse.getAuthor())))
                .andExpect(jsonPath("$.launchDate", is(bookResponse.getLaunchDate().toString())))
                .andExpect(jsonPath("$.title", is(bookResponse.getTitle())))
                .andExpect(jsonPath("$.description", is(bookResponse.getDescription())))
                .andExpect(jsonPath("$.downloadUrl", is(bookResponse.getDownloadUrl())));
    }

    @Test
    @WithMockUser
    @DisplayName("When delete book then return no content")
    void testWhenDeleteBookThenReturnNoContent() throws JsonProcessingException, Exception {
        willDoNothing().given(service).deleteBook(BOOK_ID);

        ResultActions response = mockMvc.perform(delete(PATH.concat("/{id}"), BOOK_ID));

        response.andExpect(status().isNoContent()).andDo(print());
    }
}