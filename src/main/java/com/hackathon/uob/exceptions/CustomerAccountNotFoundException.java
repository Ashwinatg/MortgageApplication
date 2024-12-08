package com.hackathon.uob.exceptions;

public class CustomerAccountNotFoundException extends RuntimeException {

    private final String message;

    public CustomerAccountNotFoundException(String message) {
        this.message=message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
