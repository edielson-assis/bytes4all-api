package br.com.edielsonassis.bookstore.services;

import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.models.Permission;
import br.com.edielsonassis.bookstore.repositories.PermissionRepository;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class PermissionService {
    
    private final PermissionRepository repository;

    public Permission findbyPermission(String name) {
        log.info("Verifying for permission: {}", name);
        return repository.findByDescriptionIgnoreCase(name).orElseThrow(() -> {
            log.error("Permission not found: {}", name);
            return new ObjectNotFoundException("Permission not found");
        });
    }
}