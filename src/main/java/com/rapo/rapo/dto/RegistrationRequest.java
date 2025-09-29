package com.rapo.rapo.dto;

import com.rapo.rapo.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotBlank
    private String name;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Role role;
    
    // Driver specific fields (optional)
    private String model;
    private String licensePlate;
    private Integer capacity;
}