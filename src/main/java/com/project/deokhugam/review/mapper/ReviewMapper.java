package com.project.deokhugam.review.mapper;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.review.dto.ReviewCreateRequest;
import com.project.deokhugam.review.dto.ReviewDto;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewCreateRequest dto, Book book, User user) {
        return Review.builder()
                .book(book)
                .user(user)
                .reviewContent(dto.content())
                .reviewRating(Long.valueOf(dto.rating()))
                .likeCount(0L)
                .commentCount(0L)
                .reviewRank(0L)
                .reviewScore(0L)
                .liked(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getReviewId())
                .bookId(review.getBook().getBookId())
                .bookTitle(review.getBook().getTitle())
                .bookThumbnailUrl(review.getBook().getThumbnailUrl())
                .userId(review.getUser().getUserId())
                .userNickname(review.getUser().getNickname())
                .content(review.getReviewContent())
                .rating(review.getReviewRating().intValue())
                .likeCount(review.getLikeCount().intValue())
                .commentCount(review.getCommentCount().intValue())
                .likedByMe(review.getLiked())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

}
