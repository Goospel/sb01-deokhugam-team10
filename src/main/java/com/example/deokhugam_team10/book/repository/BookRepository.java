package com.example.deokhugam_team10.book.repository;

import com.example.deokhugam_team10.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
