package com.project.cloudbalance.exception.customException;

public class AccountsNotFound extends RuntimeException {
    public AccountsNotFound(String message) {
        super(message);
    }
}
