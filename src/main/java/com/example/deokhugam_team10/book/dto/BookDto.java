package com.example.deokhugam_team10.book.dto;

import java.util.UUID;

public record BookDto(
    UUID id,
    String title,
    String author
) {

}
