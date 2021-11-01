package com.messenger.demo.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String login) {
        super(String.format("User %s is not found!", login));
    }

    public StudentNotFoundException() {
        super("User is not found!");
    }

}
