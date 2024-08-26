package com.example.finalproject.items;


public class MessageToGet {
    private String id;
    private String created;
    private String content;
    private SenderName sender;

    public MessageToGet(String id, String created, String content, SenderName sender) {
        this.id = id;
        this.created = created;
        this.content = content;
        this.sender = sender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(SenderName sender) {
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getContent() {
        return content;
    }

    public SenderName getSender() {
        return sender;
    }
}

