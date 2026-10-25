package com.skillswap.dto;

import java.util.List;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String bio;
    private String location;
    private Integer creditsBalance;
    private Double averageRating;
    private List<SkillMiniDto> skillsOffered;
    private List<SkillMiniDto> skillsWanted;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getCreditsBalance() { return creditsBalance; }
    public void setCreditsBalance(Integer creditsBalance) { this.creditsBalance = creditsBalance; }
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public List<SkillMiniDto> getSkillsOffered() { return skillsOffered; }
    public void setSkillsOffered(List<SkillMiniDto> skillsOffered) { this.skillsOffered = skillsOffered; }
    public List<SkillMiniDto> getSkillsWanted() { return skillsWanted; }
    public void setSkillsWanted(List<SkillMiniDto> skillsWanted) { this.skillsWanted = skillsWanted; }
}
