package com.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

/** Сутність Користувач.*/
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String phone;
    
    /** Роль користувача.*/
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    /**Бронювання користувача. */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Booking> bookings = new HashSet<>();
    
    public enum Role {
        USER,
        ADMIN
    }
}
