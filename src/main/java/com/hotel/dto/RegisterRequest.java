package com.hotel.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "Username обов'язковий")
    @Size(min = 3, max = 50)
    private String username;
    
    @NotBlank(message = "Пароль обов'язковий")
    @Size(min = 6)
    private String password;
    
    @NotBlank(message = "Email обов'язковий")
    @Email
    private String email;
    
    @NotBlank(message = "Ім'я обов'язкове")
    private String fullName;
    
    @NotBlank(message = "Телефон обов'язковий")
    private String phone;
}
