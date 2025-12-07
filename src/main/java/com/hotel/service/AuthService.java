package com.hotel.service;

import com.hotel.dto.AuthResponse;
import com.hotel.dto.LoginRequest;
import com.hotel.dto.RegisterRequest;
import com.hotel.entity.User;
import com.hotel.exception.BadRequestException;
import com.hotel.repository.UserRepository;
import com.hotel.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      JwtService jwtService, AuthenticationManager authenticationManager,
                      UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }
    
    public AuthResponse register(RegisterRequest request) {
        logger.info("Registering user: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username вже зайнятий");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email вже використовується");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(User.Role.USER);
        
        user = userRepository.save(user);
        logger.info("User registered: {}", user.getUsername());
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
    
    public AuthResponse registerAdmin(RegisterRequest request) {
        logger.info("Registering admin: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username вже зайнятий");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email вже використовується");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(User.Role.ADMIN);
        
        user = userRepository.save(user);
        logger.info("Admin registered: {}", user.getUsername());
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
    
    public AuthResponse login(LoginRequest request) {
        logger.info("Login attempt: {}", request.getUsername());
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        
        logger.info("User logged in: {}", user.getUsername());
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}
