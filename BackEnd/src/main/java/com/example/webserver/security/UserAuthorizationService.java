package com.example.webserver.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorizationService {

    public boolean canAccessUser(
        String username,
        Authentication authentication
    ) {

        if (authentication == null) {
            return false;
        }

        boolean isOwner = username.equals(authentication.getName());

        return isOwner || isAdmin(authentication);
    }

    public boolean canModifyUser(
        String username,
        Authentication authentication
    ) {
        return canAccessUser(username, authentication);
    }

    public boolean canDeleteUser(
        String username,
        Authentication authentication
    ) {
        return isAdmin(authentication);
    }

    public boolean canManageRoles(Authentication authentication) {
        return isAdmin(authentication);
    }

    private boolean isAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities()
            .stream()
            .anyMatch(authority -> 
                        authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
