package br.com.edielsonassis.bookstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.bookstore.model.Permission;

public interface PermissonRepository extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByDescriptionIgnoreCase(String name);
}