package br.com.edielsonassis.bookstore.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import br.com.edielsonassis.bookstore.dtos.v1.response.BookResponse;
import br.com.edielsonassis.bookstore.mapper.Mapper;
import br.com.edielsonassis.bookstore.models.Book;
import br.com.edielsonassis.bookstore.unittests.mapper.mocks.MockBook;

class ModelConverterTest {
    
    MockBook inputObject;

    @BeforeEach
    void setUp() {
        inputObject = new MockBook();
    }

    @Test
    void parseEntityToDtoTest() {
        BookResponse output = Mapper.parseObject(inputObject.mockEntity(), BookResponse.class);
        
        assertEquals(Long.valueOf(0L), output.getBookId());
        assertEquals("Author Test0", output.getAuthor());
        assertEquals("Title Test0", output.getTitle());
        assertEquals("Description Test0", output.getDescription());
    }

    @Test
    void parseEntityListToDtoListTest() {
        Page<BookResponse> outputList = Mapper.parseListObjects(inputObject.mockEntityList(0, 14), BookResponse.class);
        BookResponse outputZero = outputList.getContent().get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getBookId());
        assertEquals("Author Test0", outputZero.getAuthor());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals("Description Test0", outputZero.getDescription());
        
        BookResponse outputSeven = outputList.getContent().get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getBookId());
        assertEquals("Author Test7", outputSeven.getAuthor());
        assertEquals("Title Test7", outputSeven.getTitle());
        assertEquals("Description Test7", outputSeven.getDescription());
        
        BookResponse outputTwelve = outputList.getContent().get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getBookId());
        assertEquals("Author Test12", outputTwelve.getAuthor());
        assertEquals("Title Test12", outputTwelve.getTitle());
        assertEquals("Description Test12", outputTwelve.getDescription());
    }

    @Test
    void parseDtoToEntityTest() {
        Book output = Mapper.parseObject(inputObject.mockDto(), Book.class);
        
        assertEquals("Author Test0", output.getAuthor());
        assertEquals("Title Test0", output.getTitle());
        assertEquals("Description Test0", output.getDescription());
    }

    @Test
    void parseDtoListToEntityListTest() {
        List<Book> outputList = Mapper.parseListObjects(inputObject.mockDtoList(), Book.class);
        Book outputZero = outputList.get(0);
        
        assertEquals("Author Test0", outputZero.getAuthor());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals("Description Test0", outputZero.getDescription());
        
        Book outputSeven = outputList.get(7);
        
        assertEquals("Author Test7", outputSeven.getAuthor());
        assertEquals("Title Test7", outputSeven.getTitle());
        assertEquals("Description Test7", outputSeven.getDescription());
        
        Book outputTwelve = outputList.get(12);
        
        assertEquals("Author Test12", outputTwelve.getAuthor());
        assertEquals("Title Test12", outputTwelve.getTitle());
        assertEquals("Description Test12", outputTwelve.getDescription());
    }

    @Test
    void parseUpdateDtoToEntityTest() {
        Book output = Mapper.parseObject(inputObject.mockUpdateDto(), Book.class);
        
        assertEquals(Long.valueOf(0L), output.getBookId());
        assertEquals("Author Test0", output.getAuthor());
        assertEquals("Title Test0", output.getTitle());
        assertEquals("Description Test0", output.getDescription());
    }

    @Test
    void parseUpdateDtoListToEntityListTest() {
        List<Book> outputList = Mapper.parseListObjects(inputObject.mockUpdateDtoList(), Book.class);
        Book outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getBookId());
        assertEquals("Author Test0", outputZero.getAuthor());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals("Description Test0", outputZero.getDescription());
        
        Book outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getBookId());
        assertEquals("Author Test7", outputSeven.getAuthor());
        assertEquals("Title Test7", outputSeven.getTitle());
        assertEquals("Description Test7", outputSeven.getDescription());
        
        Book outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getBookId());
        assertEquals("Author Test12", outputTwelve.getAuthor());
        assertEquals("Title Test12", outputTwelve.getTitle());
        assertEquals("Description Test12", outputTwelve.getDescription());
    }
}