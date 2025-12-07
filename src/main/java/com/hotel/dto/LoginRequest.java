package com.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Username обов'язковий")
    private String username;
    
    @NotBlank(message = "Пароль обов'язковий")
    private String password;
}
