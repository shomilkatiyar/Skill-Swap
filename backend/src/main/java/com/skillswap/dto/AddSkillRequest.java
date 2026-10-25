package com.skillswap.dto;

import jakarta.validation.constraints.NotBlank;

public class AddSkillRequest {
    @NotBlank
    private String skillName;
    private String category;
    private String level; // BEGINNER / INTERMEDIATE / EXPERT, only relevant for "offered"

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
