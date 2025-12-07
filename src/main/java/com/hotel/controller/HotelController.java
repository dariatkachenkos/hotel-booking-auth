package com.hotel.controller;

import com.hotel.dto.HotelDto;
import com.hotel.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    
    private final HotelService hotelService;
    
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }
    
    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }
    
    @PostMapping
    public ResponseEntity<HotelDto> createHotel(@Valid @RequestBody HotelDto dto) {
        return new ResponseEntity<>(hotelService.createHotel(dto), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelDto dto) {
        return ResponseEntity.ok(hotelService.updateHotel(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
