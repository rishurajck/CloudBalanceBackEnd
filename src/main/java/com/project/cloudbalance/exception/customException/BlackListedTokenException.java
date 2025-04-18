package com.project.cloudbalance.exception.customException;

public class BlackListedTokenException extends RuntimeException{
    public BlackListedTokenException(String message) {
        super(message);
    }
}
