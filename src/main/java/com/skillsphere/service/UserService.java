package com.skillsphere.service;

import com.skillsphere.dto.skill.UserSkillDTO;
import com.skillsphere.dto.user.UpdateUserRequest;
import com.skillsphere.dto.user.UserDTO;
import com.skillsphere.dto.user.UserProfileDTO;
import com.skillsphere.entity.SkillType;
import com.skillsphere.entity.User;
import com.skillsphere.entity.UserSkill;
import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.repository.UserRepository;
import com.skillsphere.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;

    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return mapToProfileDTO(user);
    }

    public UserProfileDTO getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return mapToProfileDTO(user);
    }

    @Transactional
    public UserProfileDTO updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(request.getProfilePictureUrl());
        }

        userRepository.save(user);
        return mapToProfileDTO(user);
    }

    public List<UserDTO> searchUsers(String name) {
        return userRepository.searchByName(name).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findMutualMatches(Long userId) {
        return userSkillRepository.findMutualMatches(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private UserProfileDTO mapToProfileDTO(User user) {
        List<UserSkillDTO> teaching = userSkillRepository
                .findByUserIdAndSkillType(user.getId(), SkillType.TEACHING)
                .stream()
                .map(this::mapToUserSkillDTO)
                .collect(Collectors.toList());

        List<UserSkillDTO> learning = userSkillRepository
                .findByUserIdAndSkillType(user.getId(), SkillType.LEARNING)
                .stream()
                .map(this::mapToUserSkillDTO)
                .collect(Collectors.toList());

        return UserProfileDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .skillsTeaching(teaching)
                .skillsLearning(learning)
                .build();
    }

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    private UserSkillDTO mapToUserSkillDTO(UserSkill userSkill) {
        return UserSkillDTO.builder()
                .skillId(userSkill.getSkill().getId())
                .skillName(userSkill.getSkill().getName())
                .category(userSkill.getSkill().getCategory())
                .proficiencyNote(userSkill.getProficiencyNote())
                .build();
    }
}