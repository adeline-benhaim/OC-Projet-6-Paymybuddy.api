package com.paymybuddy.api.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static int getIdCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String userId = authentication.getName();
            return Integer.parseInt(userId);
        }
        return 0;
    }

    public static boolean isUserConnected() {
        return getIdCurrentUser() != 0;
    }
}
