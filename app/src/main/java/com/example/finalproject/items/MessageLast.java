package com.example.finalproject.items;

public class MessageLast {
    private String id;
    private String created;
    private String content;

    public void setId(String id) {
        this.id = id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setContent(String content) {
        this.content = content;
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

    public MessageLast(String id, String created, String content) {
        this.id = id;
        this.created = created;
        this.content = content;
    }
}
