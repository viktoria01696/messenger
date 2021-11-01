package com.messenger.demo.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
        super("Post is not found!");
    }

}
