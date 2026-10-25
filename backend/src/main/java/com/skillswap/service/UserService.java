package com.skillswap.service;

import com.skillswap.dto.*;
import com.skillswap.entity.*;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSkillOfferedRepository offeredRepository;
    private final UserSkillWantedRepository wantedRepository;
    private final SkillService skillService;
    private final ReviewRepository reviewRepository;

    public UserService(UserRepository userRepository, UserSkillOfferedRepository offeredRepository,
                        UserSkillWantedRepository wantedRepository, SkillService skillService,
                        ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.offeredRepository = offeredRepository;
        this.wantedRepository = wantedRepository;
        this.skillService = skillService;
        this.reviewRepository = reviewRepository;
    }

    public UserDto getProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        List<SkillMiniDto> offered = offeredRepository.findByUserId(userId).stream()
                .map(o -> new SkillMiniDto(o.getSkill().getId(), o.getSkill().getName(),
                        o.getSkill().getCategory(), o.getLevel().name()))
                .collect(Collectors.toList());

        List<SkillMiniDto> wanted = wantedRepository.findByUserId(userId).stream()
                .map(w -> new SkillMiniDto(w.getSkill().getId(), w.getSkill().getName(),
                        w.getSkill().getCategory(), null))
                .collect(Collectors.toList());

        UserDto dto = UserMapper.toDto(user, offered, wanted);

        List<Review> reviews = reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(userId);
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        dto.setAverageRating(reviews.isEmpty() ? null : Math.round(avg * 10.0) / 10.0);

        return dto;
    }

    @Transactional
    public void updateBioLocation(UUID userId, String bio, String location) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        if (bio != null) user.setBio(bio);
        if (location != null) user.setLocation(location);
        userRepository.save(user);
    }

    @Transactional
    public void addOfferedSkill(UUID userId, AddSkillRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        Skill skill = skillService.findOrCreate(req.getSkillName(), req.getCategory());
        SkillLevel level = req.getLevel() != null
                ? SkillLevel.valueOf(req.getLevel().toUpperCase())
                : SkillLevel.INTERMEDIATE;
        offeredRepository.save(new UserSkillOffered(user, skill, level));
    }

    @Transactional
    public void addWantedSkill(UUID userId, AddSkillRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        Skill skill = skillService.findOrCreate(req.getSkillName(), req.getCategory());
        wantedRepository.save(new UserSkillWanted(user, skill));
    }

    @Transactional
    public void removeOfferedSkill(UUID userId, UUID skillId) {
        offeredRepository.deleteByUserIdAndSkillId(userId, skillId);
    }

    @Transactional
    public void removeWantedSkill(UUID userId, UUID skillId) {
        wantedRepository.deleteByUserIdAndSkillId(userId, skillId);
    }
}
