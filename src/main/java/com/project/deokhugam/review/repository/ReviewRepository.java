package com.project.deokhugam.review.repository;

import com.project.deokhugam.book.entity.Book;
import com.project.deokhugam.review.entity.Review;
import com.project.deokhugam.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsByUserAndBook(User user, Book book);
}
