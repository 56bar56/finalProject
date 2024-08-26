package com.example.finalproject.items;

public class Contact{
    private String id;
    private UserToGet user;
    private MessageLast lastMessage;

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(UserToGet user) {
        this.user = user;
    }

    public void setLastMessage(MessageLast lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getId() {
        return id;
    }

    public UserToGet getUser() {
        return user;
    }

    public MessageLast getLastMessage() {
        return lastMessage;
    }

    public Contact(String id, UserToGet user, MessageLast lastMessage) {
        this.id = id;
        this.user = user;
        this.lastMessage = lastMessage;
    }
}
