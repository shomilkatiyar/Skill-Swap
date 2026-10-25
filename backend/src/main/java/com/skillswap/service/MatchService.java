package com.skillswap.service;

import com.skillswap.dto.MatchDto;
import com.skillswap.entity.UserSkillOffered;
import com.skillswap.entity.UserSkillWanted;
import com.skillswap.repository.ReviewRepository;
import com.skillswap.repository.UserSkillOfferedRepository;
import com.skillswap.repository.UserSkillWantedRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Simple matching: for every skill the current user WANTS, find other users
 * who OFFER that same skill. Sorted by the teacher's average rating (best first).
 */
@Service
public class MatchService {

    private final UserSkillWantedRepository wantedRepository;
    private final UserSkillOfferedRepository offeredRepository;
    private final ReviewRepository reviewRepository;

    public MatchService(UserSkillWantedRepository wantedRepository,
                         UserSkillOfferedRepository offeredRepository,
                         ReviewRepository reviewRepository) {
        this.wantedRepository = wantedRepository;
        this.offeredRepository = offeredRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<MatchDto> findMatchesForUser(UUID userId) {
        List<UserSkillWanted> wants = wantedRepository.findByUserId(userId);

        return wants.stream()
                .flatMap(w -> offeredRepository.findBySkillId(w.getSkill().getId()).stream())
                .filter(offer -> !offer.getUser().getId().equals(userId))
                .map(this::toMatchDto)
                .sorted((a, b) -> {
                    double ra = a.getTeacherRating() == null ? 0 : a.getTeacherRating();
                    double rb = b.getTeacherRating() == null ? 0 : b.getTeacherRating();
                    return Double.compare(rb, ra);
                })
                .collect(Collectors.toList());
    }

    private MatchDto toMatchDto(UserSkillOffered offer) {
        MatchDto dto = new MatchDto();
        dto.setTeacherId(offer.getUser().getId());
        dto.setTeacherName(offer.getUser().getName());
        dto.setTeacherBio(offer.getUser().getBio());
        dto.setSkillId(offer.getSkill().getId());
        dto.setSkillName(offer.getSkill().getName());
        dto.setLevel(offer.getLevel().name());

        var reviews = reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(offer.getUser().getId());
        double avg = reviews.stream().mapToInt(r -> r.getRating()).average().orElse(0.0);
        dto.setTeacherRating(reviews.isEmpty() ? null : Math.round(avg * 10.0) / 10.0);

        return dto;
    }
}
