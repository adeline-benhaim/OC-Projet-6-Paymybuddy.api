package com.paymybuddy.api.exception;

public class ConnectionNotFoundException extends RuntimeException {

    public ConnectionNotFoundException(String s) {
        super(s);
    }
}
