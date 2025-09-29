package com.rapo.rapo.dto;

import com.rapo.rapo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Role role;
}