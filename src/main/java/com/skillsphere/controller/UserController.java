package com.skillsphere.controller;

import com.skillsphere.dto.user.UpdateUserRequest;
import com.skillsphere.dto.user.UserDTO;
import com.skillsphere.dto.user.UserProfileDTO;
import com.skillsphere.entity.User;
import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.repository.UserRepository;
import com.skillsphere.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // Get current user's profile
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserProfileDTO profile = userService.getUserProfileByEmail(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    // Update current user's profile
    @PutMapping("/me")
    public ResponseEntity<UserProfileDTO> updateCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateUserRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserProfileDTO profile = userService.updateUser(user.getId(), request);
        return ResponseEntity.ok(profile);
    }

    // Get any user's profile by ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long userId) {
        UserProfileDTO profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    // Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String name) {
        List<UserDTO> users = userService.searchUsers(name);
        return ResponseEntity.ok(users);
    }

    // Find mutual matches for current user
    @GetMapping("/matches")
    public ResponseEntity<List<UserDTO>> findMatches(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<UserDTO> matches = userService.findMutualMatches(user.getId());
        return ResponseEntity.ok(matches);
    }
}