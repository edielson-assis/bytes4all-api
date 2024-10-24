package br.com.edielsonassis.bookstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.bookstore.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByDescriptionIgnoreCase(String name);
}