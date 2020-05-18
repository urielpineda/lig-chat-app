package com.upineda.ligchatapp.model;

public class Message {
    public static String SENT_BY_SELF = "You";

    public Message(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String message;
    private String username;
}
