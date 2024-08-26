package com.example.finalproject;


import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.finalproject.items.Contact;
import com.example.finalproject.items.MessageToGet;
import java.lang.reflect.Type;
import java.util.List;

public class DbObjectConverter {
    @TypeConverter
    public static List<MessageToGet> fromJson(String value) {
        Type listType = new TypeToken<List<MessageToGet>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toJson(List<MessageToGet> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static Contact fromContactJson(String value) {
        return new Gson().fromJson(value, Contact.class);
    }

    @TypeConverter
    public static String toContactJson(Contact contact) {
        return new Gson().toJson(contact);
    }
}

