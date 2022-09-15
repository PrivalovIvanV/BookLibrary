package com.example.final1.servises.bookService.api;

import com.example.final1.servises.bookService.impl.entity.Book;

public interface BookServiceExtended extends BookService {

    void update(Book book);
    void remove(int id);
    void create(Book book);

}
