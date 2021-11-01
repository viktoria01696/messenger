package com.messenger.demo.exception;

public class PageNotFoundException extends RuntimeException {

    public PageNotFoundException() {
        super("Page is not found!");
    }

}
