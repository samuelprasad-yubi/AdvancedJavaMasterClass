package com.jmca.easyauthapplication.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jmca.easyauthapplication.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            Long id,
            String username,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }



    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        return new UserDetailsImpl(
                user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    // New Methods

    // Check if the user has a specific role
    public boolean hasRole(String role) {
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    // Get full name of the user
    public String getFullName() {
        // Assuming there might be a firstName and lastName in future User class
        return this.username; // Replace with firstName + " " + lastName when available
    }

    // Check if the user is an admin
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    // Check if the user has any role from the provided list of roles
    public boolean hasAnyRole(String... roles) {
        return authorities.stream()
                .anyMatch(authority -> Arrays.asList(roles).contains(authority.getAuthority()));
    }

    // Override toString method to print user details
    @Override
    public String toString() {
        return "UserDetailsImpl{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
