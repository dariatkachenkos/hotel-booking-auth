package com.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/** Сутність Номер.*/
@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type;
    
    @Column(nullable = false)
    private BigDecimal pricePerNight;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Boolean available = true;
    
    /** Готель*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    
    /** Бронювання цього номера.*/
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Set<Booking> bookings = new HashSet<>();
    
    public enum RoomType {
        STANDARD,
        DELUXE,
        SUITE
    }
}
