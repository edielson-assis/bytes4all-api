package br.com.edielsonassis.bookstore.integrationstests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.edielsonassis.bookstore.integrationstests.config.AbstractIntegrationTest;
import br.com.edielsonassis.bookstore.repositories.PermissionRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PermissionRepositoryTest extends AbstractIntegrationTest {
	
	@Autowired
	private PermissionRepository repository;
	
	private static final String PERMISSION_NAME = "ADMIN";
    
	@Test
    @DisplayName("When find permission by name then return Permission")
    void testWhenFindPermissionByNameThenReturnPermission() {
        var permission = repository.findByDescriptionIgnoreCase(PERMISSION_NAME);

        assertNotNull(permission);
        assertNotNull(permission.get().getPermissionId());
        assertNotNull(permission.get().getDescription());
        assertNotNull(permission.get().getAuthority());

        assertEquals(1, permission.get().getPermissionId());
        assertEquals("ADMIN", permission.get().getDescription());
    }
}