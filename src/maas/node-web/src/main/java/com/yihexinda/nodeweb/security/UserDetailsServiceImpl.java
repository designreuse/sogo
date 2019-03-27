package com.yihexinda.nodeweb.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.yihexinda.auth.domain.User;
import com.yihexinda.auth.dto.AuthorityDto;
import com.yihexinda.nodeweb.service.AuthService;
import com.yihexinda.nodeweb.service.UserService;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " was not found");
        } else {
            List<AuthorityDto> authorityDtos = authService.findAuthoritiesByUserId(user.getId());
            final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.addAll(authorityDtos.stream().map(x -> new SimpleGrantedAuthority("AUTHORITY_" + x.getName().toUpperCase())).collect(Collectors.toList()));
            return new com.yihexinda.auth.domain.UserDetails(user, grantedAuthorities);
        }
    }
}
