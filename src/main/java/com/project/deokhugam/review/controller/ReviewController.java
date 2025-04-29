package com.project.deokhugam.review.controller;

import com.project.deokhugam.review.dto.ReviewCreateRequest;
import com.project.deokhugam.review.dto.ReviewDto;
import com.project.deokhugam.review.dto.ReviewLikeDto;
import com.project.deokhugam.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewCreateRequest request) {
        ReviewDto review = reviewService.createReview(request);
        return ResponseEntity
                .created(URI.create("/reviews/" + review.id()))
                .body(review);
    }

    @PostMapping("/{reviewId}/like")
    public ResponseEntity<ReviewLikeDto> likeReview(
            @PathVariable UUID reviewId,
            @RequestHeader("Deokhugam-Request-User-ID") UUID requestUserId
    ) {
        ReviewLikeDto response = reviewService.likeReview(reviewId, requestUserId);
        return ResponseEntity.ok(response);
    }
}
