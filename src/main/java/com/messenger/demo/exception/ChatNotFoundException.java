package com.messenger.demo.exception;

public class ChatNotFoundException extends RuntimeException {

    public ChatNotFoundException() {
        super("Chat is not found!");
    }

}
