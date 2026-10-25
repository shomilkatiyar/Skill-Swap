package com.skillswap.service;

import com.skillswap.dto.CreateReviewRequest;
import com.skillswap.dto.ReviewDto;
import com.skillswap.entity.Review;
import com.skillswap.entity.Session;
import com.skillswap.entity.SessionStatus;
import com.skillswap.entity.User;
import com.skillswap.exception.ApiException;
import com.skillswap.repository.ReviewRepository;
import com.skillswap.repository.SessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SessionRepository sessionRepository;

    public ReviewService(ReviewRepository reviewRepository, SessionRepository sessionRepository) {
        this.reviewRepository = reviewRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public ReviewDto createReview(UUID reviewerId, CreateReviewRequest req) {
        Session session = sessionRepository.findById(req.getSessionId())
                .orElseThrow(() -> new ApiException("Session not found", HttpStatus.NOT_FOUND));

        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new ApiException("You can only review completed sessions", HttpStatus.BAD_REQUEST);
        }

        boolean isTeacher = session.getTeacher().getId().equals(reviewerId);
        boolean isLearner = session.getLearner().getId().equals(reviewerId);
        if (!isTeacher && !isLearner) {
            throw new ApiException("Not authorized to review this session", HttpStatus.FORBIDDEN);
        }

        if (reviewRepository.findBySessionId(session.getId()).isPresent()) {
            throw new ApiException("This session has already been reviewed", HttpStatus.CONFLICT);
        }

        User reviewer = isTeacher ? session.getTeacher() : session.getLearner();
        User reviewee = isTeacher ? session.getLearner() : session.getTeacher();

        Review review = new Review();
        review.setSession(session);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setRating(req.getRating());
        review.setComment(req.getComment());
        review = reviewRepository.save(review);

        return toDto(review);
    }

    public List<ReviewDto> getReviewsForUser(UUID userId) {
        return reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    private ReviewDto toDto(Review r) {
        ReviewDto dto = new ReviewDto();
        dto.setId(r.getId());
        dto.setSessionId(r.getSession().getId());
        dto.setReviewerName(r.getReviewer().getName());
        dto.setRating(r.getRating());
        dto.setComment(r.getComment());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
