package com.example.finalproject;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.finalproject.items.Contact;
import com.example.finalproject.items.MessageToGet;

import java.util.List;

@Entity
public class DbObject {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Contact contactName;
    private List<MessageToGet> msgList;

    public DbObject( Contact contactName, List<MessageToGet> msgList) {
        this.contactName = contactName;
        this.msgList = msgList;
    }

    public void setContactName(Contact contactName) {
        this.contactName = contactName;
    }

    public void setMsgList(List<MessageToGet> msgList) {
        this.msgList = msgList;
    }

    public Contact getContactName() {
        return contactName;
    }

    public List<MessageToGet> getMsgList() {
        return msgList;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DbObject{" +
                "contactName=" + contactName +
                ", msgList=" + msgList +
                '}';
    }
}

