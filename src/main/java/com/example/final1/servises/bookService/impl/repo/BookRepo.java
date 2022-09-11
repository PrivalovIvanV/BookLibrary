package com.example.final1.servises.bookService.impl.repo;


import com.example.final1.servises.bookService.impl.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
    List<Book> findByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(String q, String a);
    Page<Book> findAllByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(String q, String a, Pageable var1);
    Optional<Book> getBookById(int id);




}
