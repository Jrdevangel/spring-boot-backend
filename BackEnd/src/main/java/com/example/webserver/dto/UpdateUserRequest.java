package com.example.webserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest (
    
    @NotBlank
    @Email
    String email
) {
}
