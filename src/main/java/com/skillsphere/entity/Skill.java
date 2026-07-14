package com.skillsphere.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<UserSkill> userSkills = new HashSet<>();
}