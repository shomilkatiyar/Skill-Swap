package com.skillswap.controller;

import com.skillswap.dto.CreateSessionRequest;
import com.skillswap.dto.SessionDto;
import com.skillswap.dto.UpdateSessionStatusRequest;
import com.skillswap.security.CurrentUser;
import com.skillswap.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final CurrentUser currentUser;

    public SessionController(SessionService sessionService, CurrentUser currentUser) {
        this.sessionService = sessionService;
        this.currentUser = currentUser;
    }

    @PostMapping
    public ResponseEntity<SessionDto> create(@Valid @RequestBody CreateSessionRequest req) {
        return ResponseEntity.ok(sessionService.createSession(currentUser.get().getId(), req));
    }

    @GetMapping
    public ResponseEntity<List<SessionDto>> mySessions() {
        return ResponseEntity.ok(sessionService.getSessionsForUser(currentUser.get().getId()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SessionDto> updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateSessionStatusRequest req) {
        return ResponseEntity.ok(sessionService.updateStatus(id, currentUser.get().getId(), req.getStatus()));
    }
}
