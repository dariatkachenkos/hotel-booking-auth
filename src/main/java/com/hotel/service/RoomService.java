package com.hotel.service;

import com.hotel.dto.RoomDto;
import com.hotel.entity.Hotel;
import com.hotel.entity.Room;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.HotelRepository;
import com.hotel.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomService {
    
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    
    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }
    
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelId(hotelId).stream().map(this::toDto).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RoomDto> getAvailableRooms() {
        logger.info("Getting all available rooms");
        return roomRepository.findByAvailableTrue().stream().map(this::toDto).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RoomDto> getAvailableRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelIdAndAvailableTrue(hotelId).stream().map(this::toDto).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Long id) {
        return toDto(roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id)));
    }
    
    public RoomDto createRoom(Long hotelId, RoomDto dto) {
        logger.info("Creating room {} in hotel {}", dto.getRoomNumber(), hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hotelId));
        
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setType(dto.getType());
        room.setPricePerNight(dto.getPricePerNight());
        room.setCapacity(dto.getCapacity());
        room.setDescription(dto.getDescription());
        room.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : true);
        room.setHotel(hotel);
        
        return toDto(roomRepository.save(room));
    }
    
    public RoomDto updateRoom(Long id, RoomDto dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        room.setRoomNumber(dto.getRoomNumber());
        room.setType(dto.getType());
        room.setPricePerNight(dto.getPricePerNight());
        room.setCapacity(dto.getCapacity());
        room.setDescription(dto.getDescription());
        if (dto.getAvailable() != null) room.setAvailable(dto.getAvailable());
        return toDto(roomRepository.save(room));
    }
    
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        roomRepository.delete(room);
    }
    
    private RoomDto toDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setCapacity(room.getCapacity());
        dto.setDescription(room.getDescription());
        dto.setAvailable(room.getAvailable());
        dto.setHotelId(room.getHotel().getId());
        dto.setHotelName(room.getHotel().getName());
        return dto;
    }
}
