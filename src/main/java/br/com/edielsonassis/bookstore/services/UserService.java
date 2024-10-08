package br.com.edielsonassis.bookstore.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.repositories.UserRepository;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        try {
            log.info("Verifying the user's email: {}", email);
            return repository.findByEmail(email);   
        } catch (UsernameNotFoundException e) {
            log.error("User not found.", e.getMessage());
            throw new ObjectNotFoundException("User not found");
        }    
    }
}