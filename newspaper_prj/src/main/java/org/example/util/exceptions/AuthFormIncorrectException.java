package org.example.util.exceptions;

public class AuthFormIncorrectException extends RuntimeException{
    public AuthFormIncorrectException(String msg) {
        super(msg);
    }
}
