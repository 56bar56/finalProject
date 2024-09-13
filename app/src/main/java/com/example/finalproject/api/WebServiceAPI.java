package com.example.finalproject.api;

import com.example.finalproject.items.Contact;
import com.example.finalproject.items.ContactForCreate;
import com.example.finalproject.items.AddNewContact;
import com.example.finalproject.items.MessageToGet;
import com.example.finalproject.items.UserToGet;
import com.example.finalproject.items.UserToPost;
import com.example.finalproject.items.messageContent;
import com.example.finalproject.items.loginInfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @POST("Tokens")
    Call<ResponseBody> getTokenFromServer(@Body loginInfo info);
    @POST("Tokens")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getTokenFromServerFireBase(@Header("authorization") String authorization,@Body loginInfo info);
    @POST("Users")
    Call<ResponseBody> postUser(@Body UserToPost user);

    @GET("Users/{id}")
    @Headers("Content-Type: application/json")
    Call<UserToGet> getUser(@Header("authorization") String authorization, @Path("id") String id);

    @GET("Chats")
    @Headers("Content-Type: application/json")
    Call<List<Contact>> getChats(@Header("authorization") String authorization);

    @POST("Chats")
    @Headers("Content-Type: application/json")
    Call<AddNewContact> postChat(@Header("authorization") String authorization, @Body ContactForCreate user);

    @POST("Chats/{id}/Messages")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> postMessage(@Header("authorization") String authorization, @Path("id") String id, @Body messageContent mes);
    @GET("Chats/{id}/Messages")
    @Headers("Content-Type: application/json")
    Call<List<MessageToGet>> getMessages(@Header("authorization") String authorization, @Path("id") String id);


    Call<ResponseBody> getTokenFromServer(String username, String password);
}

