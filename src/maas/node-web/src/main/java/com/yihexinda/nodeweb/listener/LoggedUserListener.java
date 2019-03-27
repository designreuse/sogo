package com.yihexinda.nodeweb.listener;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.yihexinda.auth.domain.UserDetails;
import com.yihexinda.nodeweb.service.UserService;

@Component
public class LoggedUserListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private HttpSession session;
    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
        UserDetails userDetail = (UserDetails) token.getPrincipal();
        session.setAttribute("user", userService.findByUsername(userDetail.getUsername()));
    }

}