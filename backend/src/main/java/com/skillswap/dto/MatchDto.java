package com.skillswap.dto;

import java.util.UUID;

public class MatchDto {
    private UUID teacherId;
    private String teacherName;
    private String teacherBio;
    private Double teacherRating;
    private UUID skillId;
    private String skillName;
    private String level;

    public UUID getTeacherId() { return teacherId; }
    public void setTeacherId(UUID teacherId) { this.teacherId = teacherId; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getTeacherBio() { return teacherBio; }
    public void setTeacherBio(String teacherBio) { this.teacherBio = teacherBio; }
    public Double getTeacherRating() { return teacherRating; }
    public void setTeacherRating(Double teacherRating) { this.teacherRating = teacherRating; }
    public UUID getSkillId() { return skillId; }
    public void setSkillId(UUID skillId) { this.skillId = skillId; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
