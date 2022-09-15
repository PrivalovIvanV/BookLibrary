package com.example.final1.servises.bookService.api;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException() {
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
