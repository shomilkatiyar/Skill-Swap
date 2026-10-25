package com.skillswap.service;

import com.skillswap.dto.SkillDto;
import com.skillswap.entity.Skill;
import com.skillswap.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<SkillDto> search(String query) {
        List<Skill> skills = (query == null || query.isBlank())
                ? skillRepository.findAll()
                : skillRepository.findByNameContainingIgnoreCase(query);
        return skills.stream().map(this::toDto).collect(Collectors.toList());
    }

    /** Finds an existing skill by name (case-insensitive) or creates it. */
    public Skill findOrCreate(String name, String category) {
        return skillRepository.findByNameIgnoreCase(name.trim())
                .orElseGet(() -> skillRepository.save(new Skill(name.trim(), category)));
    }

    private SkillDto toDto(Skill s) {
        SkillDto dto = new SkillDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setCategory(s.getCategory());
        return dto;
    }
}
