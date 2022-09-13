package com.example.final1.servises.bookService.api;

import com.example.final1.servises.bookService.impl.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();
    List<Book> findByPersonId(int id);
    Book findById(int id);
    void save(Book book);
    void addOwner(int bookId, int personId);
}
