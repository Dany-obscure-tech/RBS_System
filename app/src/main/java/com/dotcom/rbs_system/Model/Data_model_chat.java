package com.dotcom.rbs_system.Model;

public class Data_model_chat {
    private String sender;
    private String message;

    public Data_model_chat() {
    }

    public Data_model_chat(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    ////////////// getters and setters


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
