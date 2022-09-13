package com.example.final1.servises.bookService.api;

import com.example.final1.servises.bookService.impl.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();
    List<Book> findBooksByPersonId(int id);
    Book findById(int id);
    void save(Book book);
    void addOwnerForBook(int bookId, int personId);
}
