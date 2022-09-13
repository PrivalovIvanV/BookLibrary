package com.example.final1.servises.personService.api;

public class UserNotAuthException extends RuntimeException{
    public UserNotAuthException() {
    }

    public UserNotAuthException(String message) {
        super(message);
    }
}
