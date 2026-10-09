package com.skillsphere.controller;

import com.skillsphere.dto.project.ProjectDTO;
import com.skillsphere.dto.project.ProjectRequest;
import com.skillsphere.entity.User;
import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.repository.UserRepository;
import com.skillsphere.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;

    // My projects (paginated, newest first)
    @GetMapping("/me")
    public ResponseEntity<Page<ProjectDTO>> getMyProjects(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(projectService.listForUser(currentUserId(userDetails), pageable));
    }

    // Another user's projects (read-only, authenticated)
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ProjectDTO>> getUserProjects(
            @PathVariable Long userId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(projectService.listForUser(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProjectRequest request) {
        ProjectDTO created = projectService.create(currentUserId(userDetails), request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.update(currentUserId(userDetails), projectId, request));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long projectId) {
        projectService.delete(currentUserId(userDetails), projectId);
        return ResponseEntity.noContent().build();
    }

    private Long currentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getId();
    }
}
