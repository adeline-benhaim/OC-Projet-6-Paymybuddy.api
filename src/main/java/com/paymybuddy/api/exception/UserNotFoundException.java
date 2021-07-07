package com.paymybuddy.api.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String s) {
        super(s);
    }
}
