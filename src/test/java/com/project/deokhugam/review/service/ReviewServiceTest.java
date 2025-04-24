package com.project.deokhugam.review.service;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.book.repository.BookRepository;
import com.project.deokhugam.review.dto.ReviewCreateRequest;
import com.project.deokhugam.review.dto.ReviewDto;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.review.mapper.ReviewMapper;
import com.project.deokhugam.review.repository.ReviewRepository;
import com.project.deokhugam.user.entity.User;
import com.project.deokhugam.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private ReviewMapper reviewMapper;

    @Test
    void 리뷰등록_성공_테스트() {
        UUID bookId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ReviewCreateRequest request = new ReviewCreateRequest(bookId, userId, "이 책 너무 좋았어요!", 5);

        Book mockBook = Book.builder()
                .bookId(bookId)
                .title("모킹버드")
                .thumbnailImage("http://image.url")
                .build();

        User mockUser = User.builder()
                .userId(userId)
                .nickname("테스트유저")
                .email("test@example.com")
                .build();

        Review mockReview = Review.builder()
                .reviewId(UUID.randomUUID())
                .book(mockBook)
                .user(mockUser)
                .reviewContent("이 책 너무 좋았어요!")
                .reviewRating(5L)
                .build();

        ReviewDto expectedDto = ReviewDto.builder()
                .id(mockReview.getReviewId())
                .bookId(bookId)
                .userId(userId)
                .content("이 책 너무 좋았어요!")
                .rating(5)
                .build();

        // mock 객체들이 어떤 행동을 할지 미리 지정해줌
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(reviewMapper.toEntity(request, mockBook, mockUser)).thenReturn(mockReview);
        when(reviewRepository.save(mockReview)).thenReturn(mockReview);
        when(reviewMapper.toDto(mockReview)).thenReturn(expectedDto);

        // when: 테스트 대상 메서드 실행
        ReviewDto result = reviewService.createReview(request);

        // then: 결과 검증
        assertThat(result.content()).isEqualTo("이 책 너무 좋았어요!");
        assertThat(result.rating()).isEqualTo(5);
        assertThat(result.bookId()).isEqualTo(bookId);
        assertThat(result.userId()).isEqualTo(userId);
    }
}
