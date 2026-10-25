package com.skillswap.security;

import com.skillswap.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Small helper to grab the authenticated User (set by JwtAuthFilter) inside controllers. */
@Component
public class CurrentUser {
    public User get() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
