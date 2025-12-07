package com.hotel.service;

import com.hotel.dto.HotelDto;
import com.hotel.entity.Hotel;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelService {
    
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);
    private final HotelRepository hotelRepository;
    
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }
    
    @Transactional(readOnly = true)
    public List<HotelDto> getAllHotels() {
        logger.info("Getting all hotels");
        return hotelRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public HotelDto getHotelById(Long id) {
        return toDto(hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id)));
    }
    
    public HotelDto createHotel(HotelDto dto) {
        logger.info("Creating hotel: {}", dto.getName());
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setCity(dto.getCity());
        hotel.setStars(dto.getStars());
        hotel.setDescription(dto.getDescription());
        return toDto(hotelRepository.save(hotel));
    }
    
    public HotelDto updateHotel(Long id, HotelDto dto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));
        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setCity(dto.getCity());
        hotel.setStars(dto.getStars());
        hotel.setDescription(dto.getDescription());
        return toDto(hotelRepository.save(hotel));
    }
    
    public void deleteHotel(Long id) {
        logger.info("Deleting hotel: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));
        hotelRepository.delete(hotel);
    }
    
    private HotelDto toDto(Hotel hotel) {
        HotelDto dto = new HotelDto();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setAddress(hotel.getAddress());
        dto.setCity(hotel.getCity());
        dto.setStars(hotel.getStars());
        dto.setDescription(hotel.getDescription());
        dto.setRoomCount(hotel.getRooms() != null ? hotel.getRooms().size() : 0);
        return dto;
    }
}
