package com.hotel.dto;

import com.hotel.entity.Booking;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    
    @NotNull(message = "ID номера обов'язковий")
    private Long roomId;
    
    @NotNull(message = "Дата заїзду обов'язкова")
    @FutureOrPresent
    private LocalDate checkInDate;
    
    @NotNull(message = "Дата виїзду обов'язкова")
    @Future
    private LocalDate checkOutDate;
    
    // Response fields
    private BigDecimal totalPrice;
    private Booking.BookingStatus status;
    private String userName;
    private String roomNumber;
    private String hotelName;
    private Long nights;
}
