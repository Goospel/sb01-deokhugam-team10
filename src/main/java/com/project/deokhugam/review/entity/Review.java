package com.project.deokhugam.review.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.user.entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "review_id", updatable = false, nullable = false)
    private UUID reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String reviewContent;

    private Long likeCount;
    private Long commentCount;
    private Long reviewRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long reviewRank;
    private Long reviewScore;
    private Boolean liked;
}

