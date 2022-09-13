package com.example.final1.servises.personService.api;

public class UserNotAuthException extends Exception{
    public UserNotAuthException() {
    }

    public UserNotAuthException(String message) {
        super(message);
    }
}
