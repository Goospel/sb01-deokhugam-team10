package com.example.deokhugam_team10.book.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


public record BookRequest(
    UUID id,
    String title,
    String author,
    String description,
    String publisher,
    LocalDate publishedDate,
    String isbn,
    String thumbnailUrl,
    int reviewCount,
    double rating,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}

