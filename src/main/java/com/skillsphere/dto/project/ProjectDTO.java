package com.skillsphere.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private Long ownerId;
    private String name;
    private String description;
    private String repoUrl;
    private String liveUrl;
    private String techStack;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
