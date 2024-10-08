package br.com.edielsonassis.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.edielsonassis.bookstore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User WHERE u.email = :email")
    User findByEmail(@Param("email") String email);
}