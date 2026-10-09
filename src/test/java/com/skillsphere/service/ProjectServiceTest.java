package com.skillsphere.service;

import com.skillsphere.dto.project.ProjectDTO;
import com.skillsphere.dto.project.ProjectRequest;
import com.skillsphere.entity.Project;
import com.skillsphere.entity.User;
import com.skillsphere.exception.BadRequestException;
import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.repository.ProjectRepository;
import com.skillsphere.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private User owner;
    private ProjectRequest request;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(1L).email("owner@example.com").name("Owner").password("x").build();
        request = ProjectRequest.builder()
                .name("  SkillSphere  ")
                .description("Skill exchange platform")
                .repoUrl("https://github.com/example/skillsphere")
                .techStack("Java, Spring Boot")
                .build();
    }

    @Test
    void create_Success_TrimsNameAndSetsOwner() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(projectRepository.countByUserId(1L)).thenReturn(0L);
        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> {
            Project p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        ProjectDTO result = projectService.create(1L, request);

        assertEquals(10L, result.getId());
        assertEquals(1L, result.getOwnerId());
        assertEquals("SkillSphere", result.getName());
    }

    @Test
    void create_RejectsWhenLimitReached() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(projectRepository.countByUserId(1L)).thenReturn((long) ProjectService.MAX_PROJECTS_PER_USER);

        assertThrows(BadRequestException.class, () -> projectService.create(1L, request));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void update_NotOwner_ReportsNotFound() {
        when(projectRepository.findByIdAndUserId(10L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.update(2L, 10L, request));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void delete_NotOwner_ReportsNotFound() {
        when(projectRepository.findByIdAndUserId(10L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.delete(2L, 10L));
        verify(projectRepository, never()).delete(any());
    }

    @Test
    void delete_Owner_Deletes() {
        Project project = Project.builder().id(10L).user(owner).name("P").build();
        when(projectRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(project));

        projectService.delete(1L, 10L);

        verify(projectRepository).delete(project);
    }

    @Test
    void list_UnknownUser_ThrowsNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> projectService.listForUser(99L, org.springframework.data.domain.Pageable.unpaged()));
    }
}
