package com.paymybuddy.api.exception;

public class ConnectionAlreadyExistException extends RuntimeException {

    public ConnectionAlreadyExistException(String s) {
        super(s);
    }
}