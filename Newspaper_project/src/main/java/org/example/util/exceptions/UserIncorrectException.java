package org.example.util.exceptions;

public class UserIncorrectException extends RuntimeException{
    public UserIncorrectException(String msg) {
        super(msg);
    }
}
