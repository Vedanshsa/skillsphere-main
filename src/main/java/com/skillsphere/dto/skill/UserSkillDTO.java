package com.skillsphere.dto.skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillDTO {
    private Long skillId;
    private String skillName;
    private String category;
    private String proficiencyNote;
}