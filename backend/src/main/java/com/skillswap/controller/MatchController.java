package com.skillswap.controller;

import com.skillswap.dto.MatchDto;
import com.skillswap.security.CurrentUser;
import com.skillswap.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;
    private final CurrentUser currentUser;

    public MatchController(MatchService matchService, CurrentUser currentUser) {
        this.matchService = matchService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public ResponseEntity<List<MatchDto>> getMatches() {
        return ResponseEntity.ok(matchService.findMatchesForUser(currentUser.get().getId()));
    }
}
