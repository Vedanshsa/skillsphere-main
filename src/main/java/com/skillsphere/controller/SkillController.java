package com.skillsphere.controller;

import com.skillsphere.dto.skill.AddUserSkillRequest;
import com.skillsphere.dto.skill.CreateSkillRequest;
import com.skillsphere.dto.skill.SkillDTO;
import com.skillsphere.dto.skill.UserSkillDTO;
import com.skillsphere.dto.user.UserDTO;
import com.skillsphere.entity.SkillType;
import com.skillsphere.entity.User;
import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.repository.UserRepository;
import com.skillsphere.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;
    private final UserRepository userRepository;

    // ========== Public Endpoints (Browse Skills) ==========

    @GetMapping("/browse")
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/browse/{skillId}")
    public ResponseEntity<SkillDTO> getSkill(@PathVariable Long skillId) {
        return ResponseEntity.ok(skillService.getSkillById(skillId));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(skillService.getAllCategories());
    }

    @GetMapping("/browse/category/{category}")
    public ResponseEntity<List<SkillDTO>> getSkillsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(skillService.getSkillsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SkillDTO>> searchSkills(@RequestParam String q) {
        return ResponseEntity.ok(skillService.searchSkills(q));
    }

    // ========== Protected Endpoints (Require Auth) ==========

    // Create a new skill (any authenticated user can add skills to the platform)
    @PostMapping
    public ResponseEntity<SkillDTO> createSkill(@Valid @RequestBody CreateSkillRequest request) {
        SkillDTO skill = skillService.createSkill(request);
        return new ResponseEntity<>(skill, HttpStatus.CREATED);
    }

    // Add a skill to current user's profile
    @PostMapping("/me")
    public ResponseEntity<UserSkillDTO> addSkillToProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddUserSkillRequest request) {
        User user = getUserFromDetails(userDetails);
        UserSkillDTO userSkill = skillService.addUserSkill(user.getId(), request);
        return new ResponseEntity<>(userSkill, HttpStatus.CREATED);
    }

    // Remove a skill from current user's profile
    @DeleteMapping("/me/{skillId}")
    public ResponseEntity<Void> removeSkillFromProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long skillId,
            @RequestParam SkillType type) {
        User user = getUserFromDetails(userDetails);
        skillService.removeUserSkill(user.getId(), skillId, type);
        return ResponseEntity.noContent().build();
    }

    // ========== Matching Endpoints ==========

    // Find all teachers for a specific skill
    @GetMapping("/{skillId}/teachers")
    public ResponseEntity<List<UserDTO>> findTeachers(@PathVariable Long skillId) {
        return ResponseEntity.ok(skillService.findTeachersForSkill(skillId));
    }

    // Find all learners for a specific skill
    @GetMapping("/{skillId}/learners")
    public ResponseEntity<List<UserDTO>> findLearners(@PathVariable Long skillId) {
        return ResponseEntity.ok(skillService.findLearnersForSkill(skillId));
    }

    // Find potential teachers for current user (people who teach what I want to learn)
    @GetMapping("/recommendations")
    public ResponseEntity<List<UserDTO>> getRecommendations(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromDetails(userDetails);
        return ResponseEntity.ok(skillService.findPotentialTeachersForUser(user.getId()));
    }

    // ========== Helper ==========

    private User getUserFromDetails(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}