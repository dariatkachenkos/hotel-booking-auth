package com.hotel.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    private Long id;
    
    @NotBlank(message = "Назва обов'язкова")
    private String name;
    
    @NotBlank(message = "Адреса обов'язкова")
    private String address;
    
    @NotBlank(message = "Місто обов'язкове")
    private String city;
    
    @NotNull(message = "Зірки обов'язкові")
    @Min(1) @Max(5)
    private Integer stars;
    
    private String description;
    private Integer roomCount;
}
