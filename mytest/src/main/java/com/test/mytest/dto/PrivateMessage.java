package com.test.mytest.dto;

public class PrivateMessage {

    private String senderUsername;
    private String recipientUsername;
    private String message;

    public PrivateMessage() {
    }

    public PrivateMessage(String senderUsername, String recipientUsername, String message) {
        this.senderUsername = senderUsername;
        this.recipientUsername = recipientUsername;
        this.message = message;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
