package com.paymybuddy.api.exception;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String s) {
        super(s);
    }
}
