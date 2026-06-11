package com.skillsphere.service;

import com.skillsphere.config.JwtService;
import com.skillsphere.dto.auth.AuthResponse;
import com.skillsphere.dto.auth.RegisterRequest;
import com.skillsphere.entity.User;
import com.skillsphere.exception.DuplicateResourceException;
import com.skillsphere.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password123")
                .bio("Test bio")
                .build();
    }

    @Test
    void register_Success() {
        // Arrange
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(registerRequest.getEmail())).thenReturn(mockUserDetails);
        when(jwtService.generateToken(mockUserDetails)).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(registerRequest.getEmail(), response.getEmail());
        assertEquals(registerRequest.getName(), response.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        // Arrange
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }
}