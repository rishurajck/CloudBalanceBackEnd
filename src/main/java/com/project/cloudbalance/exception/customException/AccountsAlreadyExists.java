package com.project.cloudbalance.exception.customException;

public class AccountsAlreadyExists extends RuntimeException {
    public AccountsAlreadyExists(String message) {
        super(message);
    }
}
