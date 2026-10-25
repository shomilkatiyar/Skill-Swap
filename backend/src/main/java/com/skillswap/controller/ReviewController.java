package com.skillswap.controller;

import com.skillswap.dto.CreateReviewRequest;
import com.skillswap.dto.ReviewDto;
import com.skillswap.security.CurrentUser;
import com.skillswap.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final CurrentUser currentUser;

    public ReviewController(ReviewService reviewService, CurrentUser currentUser) {
        this.reviewService = reviewService;
        this.currentUser = currentUser;
    }

    @PostMapping
    public ResponseEntity<ReviewDto> create(@Valid @RequestBody CreateReviewRequest req) {
        return ResponseEntity.ok(reviewService.createReview(currentUser.get().getId(), req));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> forUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(reviewService.getReviewsForUser(userId));
    }
}
