package com.yihexinda.auth.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yihexinda.core.abst.AbstractEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"password"})
@NoArgsConstructor
public class User extends AbstractEntity {
    private static final long serialVersionUID = 5754304177960784017L;
    Long id;
    String username;
    String password;
    List<Role> roles;
    List<Authority> authorities;

    public User(String username) {
        this.username = username;
    }

    //    public boolean hasAuthority(String authorityStr) {
//        for (Authority authority : authorities) {
//            if (authority.getName().toUpperCase().equals(authorityStr)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
