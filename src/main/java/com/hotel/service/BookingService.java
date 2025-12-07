package com.hotel.service;

import com.hotel.dto.BookingDto;
import com.hotel.entity.Booking;
import com.hotel.entity.Room;
import com.hotel.entity.User;
import com.hotel.exception.BadRequestException;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    
    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Бронювання номера авторизованим користувачем.
     */
    public BookingDto createBooking(BookingDto dto) {
        // Отримуємо поточного користувача
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("User {} creating booking for room {}", username, dto.getRoomId());
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", dto.getRoomId()));
        
        // Перевірка доступності
        if (!room.getAvailable()) {
            throw new BadRequestException("Номер недоступний");
        }
        
        // Перевірка дат
        if (dto.getCheckOutDate().isBefore(dto.getCheckInDate()) ||
            dto.getCheckOutDate().isEqual(dto.getCheckInDate())) {
            throw new BadRequestException("Невірні дати бронювання");
        }
        
        // Перевірка конфліктів
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                room.getId(), dto.getCheckInDate(), dto.getCheckOutDate());
        if (!conflicts.isEmpty()) {
            throw new BadRequestException("Номер вже заброньований на ці дати");
        }
        
        // Створення бронювання
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        
        // Розрахунок вартості
        long nights = booking.getNights();
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        booking.setTotalPrice(totalPrice);
        
        booking = bookingRepository.save(booking);
        logger.info("Booking created: id={}, total={}", booking.getId(), totalPrice);
        
        return toDto(booking);
    }
    
    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long id) {
        return toDto(bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id)));
    }
    
    @Transactional(readOnly = true)
    public List<BookingDto> getMyBookings() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return bookingRepository.findByUserId(user.getId()).stream().map(this::toDto).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByHotel(Long hotelId) {
        return bookingRepository.findByHotelId(hotelId).stream().map(this::toDto).collect(Collectors.toList());
    }
    
    /**
     * Скасування бронювання (тільки ADMIN).
     */
    public BookingDto cancelBooking(Long id) {
        logger.info("Admin cancelling booking: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new BadRequestException("Бронювання вже скасовано");
        }
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);
        
        logger.info("Booking {} cancelled", id);
        return toDto(booking);
    }
    
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        bookingRepository.delete(booking);
    }
    
    private BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus());
        dto.setUserName(booking.getUser().getFullName());
        dto.setRoomNumber(booking.getRoom().getRoomNumber());
        dto.setHotelName(booking.getRoom().getHotel().getName());
        dto.setNights(booking.getNights());
        return dto;
    }
}
