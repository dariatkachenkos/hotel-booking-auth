package com.hotel.controller;

import com.hotel.dto.RoomDto;
import com.hotel.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    
    private final RoomService roomService;
    
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    
    // Перегляд доступних номерів (публічний)
    @GetMapping("/available")
    public ResponseEntity<List<RoomDto>> getAvailableRooms() {
        return ResponseEntity.ok(roomService.getAvailableRooms());
    }
    
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomDto>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }
    
    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getAvailableRoomsByHotel(hotelId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }
    
    @PostMapping("/hotel/{hotelId}")
    public ResponseEntity<RoomDto> createRoom(@PathVariable Long hotelId, @Valid @RequestBody RoomDto dto) {
        return new ResponseEntity<>(roomService.createRoom(hotelId, dto), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomDto dto) {
        return ResponseEntity.ok(roomService.updateRoom(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
