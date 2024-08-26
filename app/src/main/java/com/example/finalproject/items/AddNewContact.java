package com.example.finalproject.items;
public class AddNewContact {
    private String id;
    private UserToGet user;

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(UserToGet user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public UserToGet getUser() {
        return user;
    }

    public AddNewContact(String id, UserToGet user) {
        this.id = id;
        this.user = user;
    }

}
