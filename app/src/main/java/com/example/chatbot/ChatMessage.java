package com.example.chatbot;

public class ChatMessage {
    public enum AUTHOR_TYPE {
        userauthorType,
        aiauthorType,
    }

    private AUTHOR_TYPE author;

    private String message;

    public ChatMessage(String message, AUTHOR_TYPE author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AUTHOR_TYPE getAuthor() {
        return author;
    }
}
