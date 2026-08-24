package com.example.webserver.controller;

import com.example.webserver.dto.UserProfileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

import com.example.webserver.entity.UserEntity;
import com.example.webserver.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.security.access.prepost.PreAuthorize;

import com.example.webserver.dto.UpdateUserRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.GrantedAuthority;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/profile")
    public UserProfileResponse profile(Authentication authentication) {

        String username = authentication.getName();

        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.toSet());

        return new UserProfileResponse(
            username, 
            roles
        );
    }

    @PreAuthorize("@userAuthorizationService.canAccessUser(#username, authentication)")
    @GetMapping("/{username}")
    public UserProfileResponse getUserProfile(
        @PathVariable String username
    ) {

    UserEntity user = userService.getUserByUsername(username);

    Set<String> roles = user.getRoles()
            .stream()
            .map(role -> "ROLE_" + role.name())
            .collect(java.util.stream.Collectors.toSet());

    return new UserProfileResponse(
            user.getUsername(),
            roles
        );
    }

    @PreAuthorize("@userAuthorizationService.canModifyUser(#username, authentication)")
    @PutMapping("/{username}")
    public UserProfileResponse updateUser(
            @PathVariable String username,

            @Valid 
            @RequestBody UpdateUserRequest request
        
    ) {

        UserEntity updatedUser = userService.updateUser(username, request);

        Set<String> roles = updatedUser.getRoles()
                .stream()
                .map(role -> "ROLE_" + role.name())
                .collect(java.util.stream.Collectors.toSet());

        return new UserProfileResponse(
                updatedUser.getUsername(),
                roles
            );
    }
}