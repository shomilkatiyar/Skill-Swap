package com.skillswap.dto;

import java.util.UUID;

public class SkillMiniDto {
    private UUID id;
    private String name;
    private String category;
    private String level; // null for "wanted" skills

    public SkillMiniDto() {}

    public SkillMiniDto(UUID id, String name, String category, String level) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.level = level;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
