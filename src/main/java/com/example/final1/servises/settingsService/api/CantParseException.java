package com.example.final1.servises.settingsService.api;

public class CantParseException extends RuntimeException{
    public CantParseException() {
    }

    public CantParseException(String message) {
        super(message);
    }
}
