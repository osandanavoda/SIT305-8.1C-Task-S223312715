package com.example.chatbot;

import org.json.JSONArray;

public class MessageRequest {

    String userMessage;
    String chatHistory;

    public MessageRequest(String userMessage, String chatHistory) {
        this.userMessage = userMessage;
        this.chatHistory = chatHistory;
    }
}
