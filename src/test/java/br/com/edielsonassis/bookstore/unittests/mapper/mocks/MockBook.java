package br.com.edielsonassis.bookstore.unittests.mapper.mocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.model.Book;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookRequest mockDto() {
        return mockDto(0);
    }

    public BookUpdateRequest mockUpdateDto() {
        return mockUpdateDto(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookRequest> mockDtoList() {
        List<BookRequest> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDto(i));
        }
        return books;
    }

    public List<BookUpdateRequest> mockUpdateDtoList() {
        List<BookUpdateRequest> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockUpdateDto(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setBookId(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(LocalDate.now());
        book.setTitle("Title Test" + number);
        book.setDescription("Description Test" + number);
        return book;
    }

    public BookRequest mockDto(Integer number) {
        return BookRequest.builder()
                .author("Author Test" + number)
                .launchDate(LocalDate.now())
                .title("Title Test" + number)
                .description("Description Test" + number).build();
    }

    public BookUpdateRequest mockUpdateDto(Integer number) {
        return BookUpdateRequest.builder()
                .bookId(number.longValue())
                .author("Author Test" + number)
                .launchDate(LocalDate.now())
                .title("Title Test" + number)
                .description("Description Test" + number).build();
    }
}