package com.example.final1.servises.bookService.api;

import com.example.final1.servises.bookService.impl.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll(String query);
    Book get(int book_id);
    List<Book> getAllByPersonId(int person_id);
    void addOwner(int bookId, int personId);
    void removeOwner(int book_id);
}
