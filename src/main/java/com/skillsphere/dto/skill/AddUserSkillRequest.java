package com.skillsphere.dto.skill;

import com.skillsphere.entity.SkillType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUserSkillRequest {

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    @NotNull(message = "Skill type is required (TEACHING or LEARNING)")
    private SkillType skillType;

    private String proficiencyNote;
}