package com.skillsphere.service;

import com.skillsphere.dto.skill.CreateSkillRequest;
import com.skillsphere.dto.skill.SkillDTO;
import com.skillsphere.entity.Skill;
import com.skillsphere.entity.SkillType;
import com.skillsphere.exception.DuplicateResourceException;
import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.repository.SkillRepository;
import com.skillsphere.repository.UserRepository;
import com.skillsphere.repository.UserSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSkillRepository userSkillRepository;

    @InjectMocks
    private SkillService skillService;

    private Skill testSkill;
    private CreateSkillRequest createRequest;

    @BeforeEach
    void setUp() {
        testSkill = Skill.builder()
                .id(1L)
                .name("Java")
                .category("Programming")
                .description("Object-oriented programming language")
                .build();

        createRequest = CreateSkillRequest.builder()
                .name("Java")
                .category("Programming")
                .description("Object-oriented programming language")
                .build();
    }

    @Test
    void createSkill_Success() {
        // Arrange
        when(skillRepository.existsByNameIgnoreCase(createRequest.getName())).thenReturn(false);
        when(skillRepository.save(any(Skill.class))).thenReturn(testSkill);
        when(userSkillRepository.countBySkillIdAndSkillType(1L, SkillType.TEACHING)).thenReturn(0L);
        when(userSkillRepository.countBySkillIdAndSkillType(1L, SkillType.LEARNING)).thenReturn(0L);

        // Act
        SkillDTO result = skillService.createSkill(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testSkill.getName(), result.getName());
        assertEquals(testSkill.getCategory(), result.getCategory());
        verify(skillRepository).save(any(Skill.class));
    }

    @Test
    void createSkill_DuplicateName_ThrowsException() {
        // Arrange
        when(skillRepository.existsByNameIgnoreCase(createRequest.getName())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> skillService.createSkill(createRequest));
        verify(skillRepository, never()).save(any(Skill.class));
    }

    @Test
    void getSkillById_Success() {
        // Arrange
        when(skillRepository.findById(1L)).thenReturn(Optional.of(testSkill));
        when(userSkillRepository.countBySkillIdAndSkillType(1L, SkillType.TEACHING)).thenReturn(5L);
        when(userSkillRepository.countBySkillIdAndSkillType(1L, SkillType.LEARNING)).thenReturn(10L);

        // Act
        SkillDTO result = skillService.getSkillById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testSkill.getName(), result.getName());
        assertEquals(5, result.getTeacherCount());
        assertEquals(10, result.getLearnerCount());
    }

    @Test
    void getSkillById_NotFound_ThrowsException() {
        // Arrange
        when(skillRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> skillService.getSkillById(999L));
    }

    @Test
    void getAllCategories_Success() {
        // Arrange
        List<String> categories = Arrays.asList("Programming", "Design", "Music");
        when(skillRepository.findAllCategories()).thenReturn(categories);

        // Act
        List<String> result = skillService.getAllCategories();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains("Programming"));
    }

    @Test
    void searchSkills_Success() {
        // Arrange
        List<Skill> skills = Arrays.asList(testSkill);
        when(skillRepository.searchSkills("java")).thenReturn(skills);
        when(userSkillRepository.countBySkillIdAndSkillType(1L, SkillType.TEACHING)).thenReturn(0L);
        when(userSkillRepository.countBySkillIdAndSkillType(1L, SkillType.LEARNING)).thenReturn(0L);

        // Act
        List<SkillDTO> result = skillService.searchSkills("java");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
    }
}