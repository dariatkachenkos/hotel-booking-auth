package com.hotel.controller;

import com.hotel.dto.BookingDto;
import com.hotel.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    
    private final BookingService bookingService;
    
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    // Бронювання номера (авторизований користувач)
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto dto) {
        return new ResponseEntity<>(bookingService.createBooking(dto), HttpStatus.CREATED);
    }
    
    // Мої бронювання
    @GetMapping("/my")
    public ResponseEntity<List<BookingDto>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
    
    // Бронювання готелю
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<BookingDto>> getBookingsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.getBookingsByHotel(hotelId));
    }
    
    // Всі бронювання (тільки ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
    
    // Скасування бронювання (тільки ADMIN)
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
    
    // Видалення бронювання (тільки ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
