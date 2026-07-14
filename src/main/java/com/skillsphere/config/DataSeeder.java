package com.skillsphere.config;

import com.skillsphere.entity.Skill;
import com.skillsphere.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final SkillRepository skillRepository;

    @Override
    public void run(String... args) {
        if (skillRepository.count() == 0) {
            log.info("Seeding initial skills...");
            seedSkills();
            log.info("Seeding complete!");
        }
    }

    private void seedSkills() {
        List<Skill> skills = Arrays.asList(
            // Programming
            Skill.builder().name("Java").category("Programming").description("Object-oriented programming language").build(),
            Skill.builder().name("Python").category("Programming").description("Versatile scripting and programming language").build(),
            Skill.builder().name("JavaScript").category("Programming").description("Web development language").build(),
            Skill.builder().name("Spring Boot").category("Programming").description("Java framework for building web applications").build(),
            Skill.builder().name("React").category("Programming").description("JavaScript library for building user interfaces").build(),
            Skill.builder().name("SQL").category("Programming").description("Database query language").build(),
            
            // Design
            Skill.builder().name("UI/UX Design").category("Design").description("User interface and experience design").build(),
            Skill.builder().name("Figma").category("Design").description("Collaborative design tool").build(),
            Skill.builder().name("Graphic Design").category("Design").description("Visual communication and design").build(),
            
            // Languages
            Skill.builder().name("English").category("Languages").description("English language speaking and writing").build(),
            Skill.builder().name("Spanish").category("Languages").description("Spanish language speaking and writing").build(),
            Skill.builder().name("Mandarin").category("Languages").description("Mandarin Chinese language").build(),
            
            // Music
            Skill.builder().name("Guitar").category("Music").description("Acoustic and electric guitar").build(),
            Skill.builder().name("Piano").category("Music").description("Piano and keyboard").build(),
            Skill.builder().name("Music Production").category("Music").description("Digital audio workstation and production").build(),
            
            // Business
            Skill.builder().name("Public Speaking").category("Business").description("Presentation and communication skills").build(),
            Skill.builder().name("Marketing").category("Business").description("Digital and traditional marketing").build(),
            Skill.builder().name("Excel").category("Business").description("Spreadsheets and data analysis").build()
        );

        skillRepository.saveAll(skills);
    }
}