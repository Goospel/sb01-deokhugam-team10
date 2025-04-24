package com.project.deokhugam.review.controller;

import com.project.deokhugam.review.dto.ReviewCreateRequest;
import com.project.deokhugam.review.dto.ReviewDto;
import com.project.deokhugam.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
