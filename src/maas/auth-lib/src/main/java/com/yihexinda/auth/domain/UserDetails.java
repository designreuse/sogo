package com.yihexinda.auth.domain;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class UserDetails extends org.springframework.security.core.userdetails.User {
    private final User user;

    public UserDetails(User user, List<GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}