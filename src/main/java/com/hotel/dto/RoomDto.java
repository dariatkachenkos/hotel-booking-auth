package com.hotel.dto;

import com.hotel.entity.Room;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;
    
    @NotBlank(message = "Номер кімнати обов'язковий")
    private String roomNumber;
    
    @NotNull(message = "Тип обов'язковий")
    private Room.RoomType type;
    
    @NotNull(message = "Ціна обов'язкова")
    @DecimalMin("0.01")
    private BigDecimal pricePerNight;
    
    @NotNull(message = "Місткість обов'язкова")
    @Min(1)
    private Integer capacity;
    
    private String description;
    private Boolean available;
    private Long hotelId;
    private String hotelName;
}
