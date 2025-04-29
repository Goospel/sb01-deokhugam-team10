package com.project.deokhugam.review.service;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.book.repository.BookRepository;
import com.project.deokhugam.global.exception.CustomException;
import com.project.deokhugam.global.exception.ErrorCode;
import com.project.deokhugam.review.dto.ReviewCreateRequest;
import com.project.deokhugam.review.dto.ReviewDto;
import com.project.deokhugam.review.dto.ReviewLikeDto;
import com.project.deokhugam.review.dto.ReviewUpdateRequest;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.review.mapper.ReviewMapper;
import com.project.deokhugam.review.repository.ReviewRepository;
import com.project.deokhugam.user.entity.User;
import com.project.deokhugam.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDto createReview(ReviewCreateRequest request) {
        log.info("📝 createReview 요청");
        log.info("📘 요청된 bookId: {}", request.bookId());
        log.info("👤 요청된 userId: {}", request.userId());
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        Review review = reviewMapper.toEntity(request, book, user);
        Review saved = reviewRepository.save(review);

        return reviewMapper.toDto(saved);
    }

    @Transactional
    public ReviewLikeDto likeReview(UUID reviewId, UUID requestUserId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (Boolean.TRUE.equals(review.getLiked())) {
            review.setLiked(false);
            review.setLikeCount(review.getLikeCount() - 1);
        } else {
            review.setLiked(true);
            review.setLikeCount(review.getLikeCount() + 1);
        }

        reviewRepository.save(review);

        return new ReviewLikeDto(
                review.getReviewId(),
                review.getUser().getUserId(),
                review.getLiked()
        );
    }

    @Transactional
    public ReviewDto findReview(UUID reviewId, UUID requestUserId) {
        // 1. 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 2. 리뷰 엔티티를 DTO로 변환 (요청자 ID 기반 likedByMe 세팅)
        return reviewMapper.toDto(review, requestUserId);
    }

    @Transactional
    public ReviewDto updateReview(UUID reviewId, UUID requestUserId, ReviewUpdateRequest request) {
        // 1. 리뷰 찾기
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 2. 권한 체크 (리뷰 작성자 본인만 수정 가능)
        if (!review.getUser().getUserId().equals(requestUserId)) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND); // 403 에러
        }

        // 3. 수정 진행
        review.setReviewContent(request.content());
        review.setReviewRating(request.rating().longValue()); // Integer를 Long으로 변환
        review.setUpdatedAt(LocalDateTime.now());

        // 4. 저장
        Review updated = reviewRepository.save(review);

        // 5. DTO로 변환해서 반환
        return reviewMapper.toDto(updated, requestUserId);
    }

    @Transactional
    public void deleteReview(UUID reviewId, UUID requestUserId) {
        // 1. 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 2. 권한 체크 (리뷰 작성자만 삭제 가능)
        if (!review.getUser().getUserId().equals(requestUserId)) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND); // 403
        }

        // 3. 논리 삭제 처리
        review.setDeleted(true);
        review.setUpdatedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    @Transactional
    public void hardDeleteReview(UUID reviewId, UUID requestUserId) {
        // 1. 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 2. 권한 체크
        if (!review.getUser().getUserId().equals(requestUserId)) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        // 3. 리뷰 삭제
        reviewRepository.delete(review);
    }





}
