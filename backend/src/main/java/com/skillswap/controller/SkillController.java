package com.skillswap.controller;

import com.skillswap.dto.SkillDto;
import com.skillswap.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<SkillDto>> search(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(skillService.search(q));
    }
}
