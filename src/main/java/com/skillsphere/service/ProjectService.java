package com.skillsphere.service;

import com.skillsphere.dto.project.ProjectDTO;
import com.skillsphere.dto.project.ProjectRequest;
import com.skillsphere.entity.Project;
import com.skillsphere.entity.User;
import com.skillsphere.exception.BadRequestException;
import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.repository.ProjectRepository;
import com.skillsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    static final int MAX_PROJECTS_PER_USER = 50;

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<ProjectDTO> listForUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return projectRepository.findByUserId(userId, pageable).map(this::toDto);
    }

    @Transactional
    public ProjectDTO create(Long userId, ProjectRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (projectRepository.countByUserId(userId) >= MAX_PROJECTS_PER_USER) {
            throw new BadRequestException("Project limit reached (" + MAX_PROJECTS_PER_USER + ")");
        }

        Project project = Project.builder()
                .user(user)
                .name(request.getName().trim())
                .description(request.getDescription())
                .repoUrl(request.getRepoUrl())
                .liveUrl(request.getLiveUrl())
                .techStack(request.getTechStack())
                .build();

        return toDto(projectRepository.save(project));
    }

    @Transactional
    public ProjectDTO update(Long userId, Long projectId, ProjectRequest request) {
        Project project = findOwned(userId, projectId);
        project.setName(request.getName().trim());
        project.setDescription(request.getDescription());
        project.setRepoUrl(request.getRepoUrl());
        project.setLiveUrl(request.getLiveUrl());
        project.setTechStack(request.getTechStack());
        return toDto(projectRepository.save(project));
    }

    @Transactional
    public void delete(Long userId, Long projectId) {
        projectRepository.delete(findOwned(userId, projectId));
    }

    /** Object-level authorization: a project that exists but belongs to someone else is reported as not found. */
    private Project findOwned(Long userId, Long projectId) {
        return projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    private ProjectDTO toDto(Project p) {
        return ProjectDTO.builder()
                .id(p.getId())
                .ownerId(p.getUser().getId())
                .name(p.getName())
                .description(p.getDescription())
                .repoUrl(p.getRepoUrl())
                .liveUrl(p.getLiveUrl())
                .techStack(p.getTechStack())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
