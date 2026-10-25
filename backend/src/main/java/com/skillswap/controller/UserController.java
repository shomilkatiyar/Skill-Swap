package com.skillswap.controller;

import com.skillswap.dto.AddSkillRequest;
import com.skillswap.dto.UserDto;
import com.skillswap.security.CurrentUser;
import com.skillswap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final CurrentUser currentUser;

    public UserController(UserService userService, CurrentUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok(userService.getProfile(currentUser.get().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(@RequestBody Map<String, String> body) {
        userService.updateBioLocation(currentUser.get().getId(), body.get("bio"), body.get("location"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/skills-offered")
    public ResponseEntity<Void> addOfferedSkill(@Valid @RequestBody AddSkillRequest req) {
        userService.addOfferedSkill(currentUser.get().getId(), req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/skills-wanted")
    public ResponseEntity<Void> addWantedSkill(@Valid @RequestBody AddSkillRequest req) {
        userService.addWantedSkill(currentUser.get().getId(), req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/skills-offered/{skillId}")
    public ResponseEntity<Void> removeOfferedSkill(@PathVariable UUID skillId) {
        userService.removeOfferedSkill(currentUser.get().getId(), skillId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/skills-wanted/{skillId}")
    public ResponseEntity<Void> removeWantedSkill(@PathVariable UUID skillId) {
        userService.removeWantedSkill(currentUser.get().getId(), skillId);
        return ResponseEntity.ok().build();
    }
}
