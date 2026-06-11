package com.skillsphere.dto.user;

import com.skillsphere.dto.skill.UserSkillDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String profilePictureUrl;
    private List<UserSkillDTO> skillsTeaching;
    private List<UserSkillDTO> skillsLearning;
}