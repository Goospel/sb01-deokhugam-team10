package com.project.deokhugam.review.dto;

import java.util.UUID;

public record ReviewLikeDto(
        UUID reviewId,
        UUID userId,
        boolean liked
) {}
