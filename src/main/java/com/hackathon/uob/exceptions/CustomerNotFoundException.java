package com.hackathon.uob.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    private final String message;

    public CustomerNotFoundException(String message) {
        this.message=message;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
