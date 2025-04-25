package com.project.cloudbalance.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cloudbalance.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String pass;
    private GrantedAuthority authority;


    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
        this.id = user.getId();
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.pass = user.getPassword();
        this.authority = new SimpleGrantedAuthority("ROLE_"+user.getRole().name());

    }

    public static UserDetailsImpl build(User user){
        return new UserDetailsImpl(user);
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
