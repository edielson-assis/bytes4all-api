package br.com.edielsonassis.bookstore.integrationstests.repositories;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.models.Book;
import br.com.edielsonassis.bookstore.repositories.BookRepository;
import br.com.edielsonassis.bookstore.utils.constants.DefaultValue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest extends AbstractIntegrationTest {
	
	@Autowired
	private BookRepository repository;
	
	private static Book book;
	
	@BeforeAll
	static void setup() {
		book = new Book();
        book.setAuthor("Author Test");
        book.setLaunchDate(LocalDate.of(2024, 10, 15));
        book.setTitle("Title Test");
        book.setDescription("Description Test");
	}
	
	@Test
	@DisplayName("When find book by name then return Book")
    void testWhenFindBookByNameThenReturnBook()  throws JsonMappingException, JsonProcessingException {
		repository.save(book);
		Pageable pageable = PageRequest.of(DefaultValue.PAGE, DefaultValue.SIZE, Sort.by(Direction.ASC, "title"));
		var savedBook = repository.findBookByName("tle", pageable).getContent().get(0);
		
		assertNotNull(savedBook);
        assertNotNull(savedBook.getBookId());
        assertNotNull(savedBook.getAuthor());
        assertNotNull(savedBook.getTitle());
        assertNotNull(savedBook.getDescription());
        assertNotNull(savedBook.getLaunchDate());
		
		assertTrue(savedBook.getBookId() > 0);
		
		assertEquals("Author Test", savedBook.getAuthor());
        assertEquals("Title Test", savedBook.getTitle());
        assertEquals("Description Test", savedBook.getDescription());
        assertEquals("2024-10-15", savedBook.getLaunchDate().toString());
	}
}