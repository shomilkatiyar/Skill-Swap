package com.skillswap.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class SkillDto {
    private UUID id;
    @NotBlank
    private String name;
    private String category;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
