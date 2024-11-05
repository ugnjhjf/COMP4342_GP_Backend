package com.example.websocketJSONdemo;

public class Message {
    private String sender;
    private String content;

    public Message() {}

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
