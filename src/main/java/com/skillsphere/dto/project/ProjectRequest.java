package com.skillsphere.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotBlank(message = "Project name is required")
    @Size(max = 150, message = "Project name cannot exceed 150 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 500, message = "Repository URL cannot exceed 500 characters")
    @Pattern(regexp = "^https?://.+", message = "Repository URL must start with http:// or https://")
    private String repoUrl;

    @Size(max = 500, message = "Live URL cannot exceed 500 characters")
    @Pattern(regexp = "^https?://.+", message = "Live URL must start with http:// or https://")
    private String liveUrl;

    @Size(max = 500, message = "Tech stack cannot exceed 500 characters")
    private String techStack;
}
