package br.com.edielsonassis.bookstore.unittests.mapper.mocks;

import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.models.Book;

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
    
    public Page<Book> mockEntityList(int page, int size) {
        List<Book> books = new ArrayList<>();
    
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        int start = Math.min(page * size, books.size());
        int end = Math.min(start + size, books.size());
        List<Book> sublist = books.subList(start, end);
        return new PageImpl<>(sublist, PageRequest.of(page, size), books.size());
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
        book.setUser(new MockUser().user());
        return book;
    }

    public BookRequest mockDto(Integer number) {
        BookRequest book = new BookRequest();
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(LocalDate.now());
        book.setTitle("Title Test" + number);
        book.setDescription("Description Test" + number);
        book.setFile(mock(MultipartFile.class));
        return book;
    }

    public BookUpdateRequest mockUpdateDto(Integer number) {
        BookUpdateRequest book = new BookUpdateRequest();
        book.setBookId(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(LocalDate.now());
        book.setTitle("Title Test" + number);
        book.setDescription("Description Test" + number);
        return book;
    }
}