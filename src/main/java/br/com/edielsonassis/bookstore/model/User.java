package br.com.edielsonassis.bookstore.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "userId")
@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user", columnDefinition = "UUID")
    private Long userId;

    @Column(nullable = false, name = "full_name", length = 150)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "is_account_non_expired")
    private boolean isAccountNonExpired;

    @Column(nullable = false, name = "is_account_non_locked")
    private boolean isAccountNonLocked;

    @Column(nullable = false, name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Column(nullable = false, name = "is_enabled")
    private boolean isEnabled;

    @ManyToMany
    @JoinTable(name = "user_permission", joinColumns = {@JoinColumn (name = "user_id")}, inverseJoinColumns = {@JoinColumn (name = "permission_id")})
    private List<Permission> permissions;

    public List<String> getRoles() {
		return permissions.stream().map(Permission::getDescription).collect(Collectors.toList());
	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return !this.isEnabled;
    }
}