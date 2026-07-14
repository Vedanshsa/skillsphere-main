package com.skillsphere.service;

import com.skillsphere.dto.skill.AddUserSkillRequest;
import com.skillsphere.dto.skill.CreateSkillRequest;
import com.skillsphere.dto.skill.SkillDTO;
import com.skillsphere.dto.skill.UserSkillDTO;
import com.skillsphere.dto.user.UserDTO;
import com.skillsphere.entity.Skill;
import com.skillsphere.entity.SkillType;
import com.skillsphere.entity.User;
import com.skillsphere.entity.UserSkill;
import com.skillsphere.exception.BadRequestException;
import com.skillsphere.exception.DuplicateResourceException;
import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.repository.SkillRepository;
import com.skillsphere.repository.UserRepository;
import com.skillsphere.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;

    // ========== Skill CRUD ==========

    @Transactional
    public SkillDTO createSkill(CreateSkillRequest request) {
        if (skillRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Skill already exists: " + request.getName());
        }

        Skill skill = Skill.builder()
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .build();

        skillRepository.save(skill);
        return mapToDTO(skill);
    }

    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SkillDTO getSkillById(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));
        return mapToDTO(skill);
    }

    public List<SkillDTO> getSkillsByCategory(String category) {
        return skillRepository.findByCategoryIgnoreCase(category).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllCategories() {
        return skillRepository.findAllCategories();
    }

    public List<SkillDTO> searchSkills(String query) {
        return skillRepository.searchSkills(query).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ========== User-Skill Management ==========

    @Transactional
    public UserSkillDTO addUserSkill(Long userId, AddUserSkillRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        // Check if already exists
        if (userSkillRepository.existsByUserIdAndSkillIdAndSkillType(
                userId, request.getSkillId(), request.getSkillType())) {
            throw new DuplicateResourceException(
                    "You already have this skill marked as " + request.getSkillType());
        }

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .skillType(request.getSkillType())
                .proficiencyNote(request.getProficiencyNote())
                .build();

        userSkillRepository.save(userSkill);

        return UserSkillDTO.builder()
                .skillId(skill.getId())
                .skillName(skill.getName())
                .category(skill.getCategory())
                .proficiencyNote(request.getProficiencyNote())
                .build();
    }

    @Transactional
    public void removeUserSkill(Long userId, Long skillId, SkillType skillType) {
        if (!userSkillRepository.existsByUserIdAndSkillIdAndSkillType(userId, skillId, skillType)) {
            throw new ResourceNotFoundException("User skill not found");
        }
        userSkillRepository.deleteByUserIdAndSkillIdAndSkillType(userId, skillId, skillType);
    }

    // ========== Matching ==========

    public List<UserDTO> findTeachersForSkill(Long skillId) {
        return userSkillRepository.findBySkillIdAndSkillType(skillId, SkillType.TEACHING)
                .stream()
                .map(us -> mapUserToDTO(us.getUser()))
                .collect(Collectors.toList());
    }

    public List<UserDTO> findLearnersForSkill(Long skillId) {
        return userSkillRepository.findBySkillIdAndSkillType(skillId, SkillType.LEARNING)
                .stream()
                .map(us -> mapUserToDTO(us.getUser()))
                .collect(Collectors.toList());
    }

    public List<UserDTO> findPotentialTeachersForUser(Long userId) {
        return userSkillRepository.findTeachersForUserLearningSkills(userId)
                .stream()
                .map(us -> mapUserToDTO(us.getUser()))
                .distinct()
                .collect(Collectors.toList());
    }

    // ========== Mappers ==========

    private SkillDTO mapToDTO(Skill skill) {
        long teacherCount = userSkillRepository.countBySkillIdAndSkillType(skill.getId(), SkillType.TEACHING);
        long learnerCount = userSkillRepository.countBySkillIdAndSkillType(skill.getId(), SkillType.LEARNING);

        return SkillDTO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .description(skill.getDescription())
                .teacherCount((int) teacherCount)
                .learnerCount((int) learnerCount)
                .build();
    }

    private UserDTO mapUserToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }
}