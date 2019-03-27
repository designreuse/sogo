package com.yihexinda.platformweb.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.yihexinda.core.encryption.MD5;

/**
 * @author Jack
 * @date 2018/10/24.
 */
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override

    public String encode(CharSequence rawPassword) {

        return MD5.encryptByMD5(MD5.encryptByMD5(rawPassword.toString()));

    }

    @Override

    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        return MD5.encryptByMD5(MD5.encryptByMD5(rawPassword.toString())).equals(encodedPassword);

    }

}