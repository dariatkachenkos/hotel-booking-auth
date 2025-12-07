package com.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelBookingAuthApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(HotelBookingAuthApplication.class, args);
        System.out.println("  Hotel Booking System with Auth запущено!");
        System.out.println("  API: http://localhost:8080");
        System.out.println("  H2: http://localhost:8080/h2-console");
    }
}
