package com.yihexinda.nodeweb.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yihexinda.auth.domain.User;
import com.yihexinda.nodeweb.service.UserService;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@Component
public class SecurityHelper {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;

//    public void login(String username, HttpServletRequest request) {
//        org.springframework.security.core.userdetails.UserDetails details = this.userDetailsService.loadUserByUsername(username);
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        HttpSession session = request.getSession();
//        sessionRegistry.registerNewSession(session.getId(), details);
//    }

    public void login(String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("user", userDetails);
        session.setMaxInactiveInterval(12 * 3600);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        User user = userService.findByUsername(SecurityUtils.getCurrentUserLogin());
        return user;
    }
}
