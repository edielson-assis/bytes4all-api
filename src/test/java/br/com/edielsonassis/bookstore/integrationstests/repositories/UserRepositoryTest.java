package br.com.edielsonassis.bookstore.integrationstests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.models.User;
import br.com.edielsonassis.bookstore.repositories.UserRepository;

@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends AbstractIntegrationTest {
    
    @Autowired
	private UserRepository repository;
	
	private User user;
    private static final String EMAIL = "teste@email.com";
	
	@BeforeEach
	void setup() {
		user = new User();
        user.setFullName("Test auth");
        user.setEmail(EMAIL);
        user.setPassword("91e2532173dc95ef503ed5ed39f7822f576a93b7c5ae41ef52b2467bd0234f089bbfd3f3f79ed7ba");
        repository.save(user);
    }

    @Test
    @Order(1)
    @DisplayName("When find user by name then return User")
    void testWhenFindUserByNameThenReturnUser() {
        var savedUser = repository.findByEmail(EMAIL);

        assertNotNull(savedUser);
        assertNotNull(savedUser.get().getUserId());
        assertNotNull(savedUser.get().getFullName());
        assertNotNull(savedUser.get().getEmail());

        assertEquals(1, savedUser.get().getUserId());
        assertEquals("Test auth", savedUser.get().getFullName());
        assertEquals("teste@email.com", savedUser.get().getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("When exists user by email then return true")
    void testWhenExistsUserByEmailThenReturnTrue() {
        var exists = repository.existsByEmail(EMAIL);

        assertTrue(exists);
    }
}