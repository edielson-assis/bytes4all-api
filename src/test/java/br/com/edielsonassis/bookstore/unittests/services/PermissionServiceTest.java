package br.com.edielsonassis.bookstore.unittests.services;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
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

import br.com.edielsonassis.bookstore.model.Permission;
import br.com.edielsonassis.bookstore.repositories.PermissionRepository;
import br.com.edielsonassis.bookstore.services.PermissionService;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;

@TestInstance(Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {
    
    @Mock
    private PermissionRepository repository;

    @InjectMocks
    private PermissionService service;

    private Permission permission;

    private static final String PERMISSION_NAME = "ADMIN";
    private static final Long PERMISSION_ID = 1L;
    private static final Integer NUMBER_ONE = 1;

    @BeforeEach
    void setup() {
        permission = new Permission();
        permission.setPermissionId(PERMISSION_ID);
        permission.setDescription(PERMISSION_NAME);
    }

    @Test
    @DisplayName("When find permission by name then return Permission")
    void testWhenFindPermissionByNameThenReturnPermission() {
        when(repository.findByDescriptionIgnoreCase(anyString())).thenReturn(Optional.of(permission));

        var savedPermission = service.findbyPermission(PERMISSION_NAME);

        assertNotNull(savedPermission);
        assertNotNull(savedPermission.getPermissionId());
        assertNotNull(savedPermission.getDescription());
        assertNotNull(savedPermission.getAuthority());

        assertTrue(savedPermission.getPermissionId() > 0);

        assertEquals(PERMISSION_ID, savedPermission.getPermissionId());
        assertEquals("ADMIN", savedPermission.getDescription());

        verify(repository, times(NUMBER_ONE)).findByDescriptionIgnoreCase(anyString());
    }

    @Test
    @DisplayName("When find permission by name then throw ObjectNotFoundException")
    void testWhenFindPermissionByNameThenThrowObjectNotFoundException() {
        when(repository.findByDescriptionIgnoreCase(anyString())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.findbyPermission(PERMISSION_NAME));

        verify(repository, times(NUMBER_ONE)).findByDescriptionIgnoreCase(anyString());
    }
}