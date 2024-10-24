package br.com.edielsonassis.bookstore.unittests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.models.Book;
import br.com.edielsonassis.bookstore.repositories.BookRepository;
import br.com.edielsonassis.bookstore.services.BookService;
import br.com.edielsonassis.bookstore.services.exceptions.DataBaseException;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;
import br.com.edielsonassis.bookstore.unittests.mapper.mocks.MockBook;
import br.com.edielsonassis.bookstore.utils.constants.DefaultValue;

@TestInstance(Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    private Book book;
    private BookRequest bookRequest;
    private MockBook input;
    private static final Long BOOK_ID = 1L;
    private static final Integer NUMBER_ONE = 1;

    @BeforeEach
    void setup() {
        input = new MockBook();
        book = input.mockEntity(NUMBER_ONE);
        bookRequest = input.mockDto(NUMBER_ONE);
    }

    @Test
    @DisplayName("When create a book then return BookResponse")
    void testWhenCreateBookThenReturnBookResponse() {
        when(repository.save(any(Book.class))).thenReturn(book);

        var savedBook = service.createBook(bookRequest);

        assertNotNull(savedBook);
        assertNotNull(savedBook.getBookId());
        assertNotNull(savedBook.getLinks());
		assertNotNull(savedBook.getLaunchDate());

        assertEquals("Author Test1", savedBook.getAuthor());
        assertEquals("Title Test1", savedBook.getTitle());
        assertEquals("Description Test1", savedBook.getDescription());
        
        verify(repository, times(NUMBER_ONE)).save(any(Book.class));
    }

    @Test
    @DisplayName("When find book by ID then return BookResponse")
    void testWhenFindBookByIdThenReturnBookResponse() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));

        var savedBook = service.findBookById(BOOK_ID);

        assertNotNull(savedBook);
        assertNotNull(savedBook.getBookId());
        assertNotNull(savedBook.getLinks());
        assertNotNull(savedBook.getLaunchDate());

        assertEquals("Author Test1", savedBook.getAuthor());
        assertEquals("Title Test1", savedBook.getTitle());
        assertEquals("Description Test1", savedBook.getDescription());
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
    }

    @Test
    @DisplayName("When find book by ID then throw ObjectNotFoundException")
    void testWhenFindBookByIdThenThrowObjectNotFoundException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.findBookById(BOOK_ID));
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
    }

    @Test
    @DisplayName("When find book by name then return BookResponse")
    void testWhenFindBookByNameThenReturnBookResponse() {
        Page<Book> list = input.mockEntityList(DefaultValue.PAGE, DefaultValue.SIZE); 

        when(repository.findBookByName(anyString(), any(Pageable.class))).thenReturn(list);

        var books = service.findBookByName("tle ", DefaultValue.PAGE, DefaultValue.SIZE, DefaultValue.DIRECTION);

        assertNotNull(books);
		assertEquals(10, books.getSize());
		
		var bookOne = books.get().toList().get(NUMBER_ONE);
		
		assertNotNull(bookOne);
        assertNotNull(bookOne.getBookId());
        assertNotNull(bookOne.getLinks());
        assertNotNull(bookOne.getLaunchDate());

        assertEquals("Author Test1", bookOne.getAuthor());
        assertEquals("Title Test1", bookOne.getTitle());
        assertEquals("Description Test1", bookOne.getDescription());
        
        verify(repository, times(NUMBER_ONE)).findBookByName(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("When find all books then return BookResponse list")
    void testWhenFindAllBooksThenReturnBookResponseList() {
        Page<Book> list = input.mockEntityList(DefaultValue.PAGE, DefaultValue.SIZE); 

        when(repository.findAll(any(Pageable.class))).thenReturn(list);

        var books = service.findAllBooks(DefaultValue.PAGE, DefaultValue.SIZE, DefaultValue.DIRECTION);

        assertNotNull(books);
		assertEquals(10, books.getSize());
		
		var bookOne = books.get().toList().get(NUMBER_ONE);
		
		assertNotNull(bookOne);
        assertNotNull(bookOne.getBookId());
        assertNotNull(bookOne.getLinks());
        assertNotNull(bookOne.getLaunchDate());

        assertEquals("Author Test1", bookOne.getAuthor());
        assertEquals("Title Test1", bookOne.getTitle());
        assertEquals("Description Test1", bookOne.getDescription());
        
        verify(repository, times(NUMBER_ONE)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("When update a book then return BookResponse")
    void testWhenUpdateBookThenReturnBookResponse() {
        Book updatedBook = input.mockEntity(NUMBER_ONE);
        BookUpdateRequest bookRequest = input.mockUpdateDto(NUMBER_ONE);

        when(repository.findById(anyLong())).thenReturn(Optional.of(book));
        when(repository.save(any(Book.class))).thenReturn(updatedBook);

        var savedBook = service.updateBook(bookRequest);

        assertNotNull(savedBook);
        assertNotNull(savedBook.getBookId());
        assertNotNull(savedBook.getLinks());
        assertNotNull(savedBook.getLaunchDate());

        assertEquals("Author Test1", savedBook.getAuthor());
        assertEquals("Title Test1", savedBook.getTitle());
        assertEquals("Description Test1", savedBook.getDescription());
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
        verify(repository, times(NUMBER_ONE)).save(any(Book.class));
    }

    @Test
    @DisplayName("When delete book then return no content")
    void testWhenDeleteBookThenReturnNoContent() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));
        doNothing().when(repository).delete(book);

        service.deleteBook(BOOK_ID);
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
        verify(repository, times(NUMBER_ONE)).delete(book);
    }

    @Test
    @DisplayName("When delete book then throw DataBaseException")
    void testWhenDeleteBookThenThrowDataBaseException() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));
        doThrow(new DataIntegrityViolationException("Referential integrity violation")).when(repository).delete(book);
        
        assertThrows(DataBaseException.class, () -> service.deleteBook(BOOK_ID));
        
        verify(repository, times(NUMBER_ONE)).findById(anyLong());
        verify(repository, times(NUMBER_ONE)).delete(book);
    }
}