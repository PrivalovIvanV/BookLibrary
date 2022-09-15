package com.example.final1.servises.personService.api;

public class PersonNotFoundException extends RuntimeException{


    public PersonNotFoundException(String message) {
        super(message);
    }
}
