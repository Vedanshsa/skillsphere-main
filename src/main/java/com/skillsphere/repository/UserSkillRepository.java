package com.skillsphere.repository;

import com.skillsphere.entity.SkillType;
import com.skillsphere.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUserIdAndSkillType(Long userId, SkillType skillType);

    List<UserSkill> findBySkillIdAndSkillType(Long skillId, SkillType skillType);

    Optional<UserSkill> findByUserIdAndSkillIdAndSkillType(Long userId, Long skillId, SkillType skillType);

    boolean existsByUserIdAndSkillIdAndSkillType(Long userId, Long skillId, SkillType skillType);

    void deleteByUserIdAndSkillIdAndSkillType(Long userId, Long skillId, SkillType skillType);

    // Find potential matches: users who TEACH what I want to LEARN
    @Query("SELECT us FROM UserSkill us WHERE us.skill.id IN " +
           "(SELECT us2.skill.id FROM UserSkill us2 WHERE us2.user.id = :userId AND us2.skillType = 'LEARNING') " +
           "AND us.skillType = 'TEACHING' AND us.user.id != :userId")
    List<UserSkill> findTeachersForUserLearningSkills(Long userId);

    // Find mutual matches: users who teach what I learn AND learn what I teach
    @Query("SELECT DISTINCT us.user FROM UserSkill us WHERE " +
           "us.skill.id IN (SELECT us2.skill.id FROM UserSkill us2 WHERE us2.user.id = :userId AND us2.skillType = 'LEARNING') " +
           "AND us.skillType = 'TEACHING' " +
           "AND us.user.id IN (" +
           "  SELECT us3.user.id FROM UserSkill us3 WHERE " +
           "  us3.skill.id IN (SELECT us4.skill.id FROM UserSkill us4 WHERE us4.user.id = :userId AND us4.skillType = 'TEACHING') " +
           "  AND us3.skillType = 'LEARNING'" +
           ") AND us.user.id != :userId")
    List<com.skillsphere.entity.User> findMutualMatches(Long userId);

    // Count teachers for a skill
    long countBySkillIdAndSkillType(Long skillId, SkillType skillType);
}