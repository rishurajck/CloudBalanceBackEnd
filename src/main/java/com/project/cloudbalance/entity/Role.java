package com.project.cloudbalance.entity;

public enum Role {
    ADMIN,
    READ_ONLY,
    CUSTOMER;

    public boolean equalsIgnoreCase(String targetRole) {
        return false;
    }
}
