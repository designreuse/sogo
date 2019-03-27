package com.yihexinda.nodeweb.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Lists;
import com.yihexinda.auth.constant.AuthoritiesConstants;
import com.yihexinda.data.enums.OrderTypeEnum;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     */
    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    public static List<SimpleGrantedAuthority> getAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        }
        return new ArrayList<>();
    }

    /**
     * 检查权限
     */
    public static boolean hasAuthority(String authorityStr) {
        for (GrantedAuthority authority : getAuthorities()) {
            if (authority.getAuthority().equals(authorityStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据权限获取用户可查看的订单类型
     *
     * @return
     */
    public static List<Integer> getHasAuthority() {
        List<Integer> returnList = Lists.newArrayList();
        if (hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_CHUPIAO)) {
            returnList.add(OrderTypeEnum.出票.getCode());
        }
        if (hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_TUIPIAO)) {
            returnList.add(OrderTypeEnum.退票.getCode());
        }
        if (hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_GAIQI)) {
            returnList.add(OrderTypeEnum.改期.getCode());
        }
        if (hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_HANGEYAN)) {
            returnList.add(OrderTypeEnum.航延.getCode());
        }
        return returnList;

    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(AuthoritiesConstants.NODE_MANAGEMENT)) {
                    return false;
                }
            }
        }
        return true;
    }

//    /**
//     * Return the current user, or throws an exception, if the user is not
//     * authenticated yet.
//     *
//     * @return the current user
//     */
//    public static User getCurrentUser() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof User) {
//                return (User) authentication.getPrincipal();
//            }
//        }
//        throw new IllegalStateException("User not found!");
//    }

    /**
     * If the current user has a specific authority (security role).
     *
     * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p>
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
            }
        }
        return false;
    }
}

