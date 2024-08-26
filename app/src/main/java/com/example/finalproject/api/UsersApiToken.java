package com.example.finalproject.api;

import com.example.finalproject.globalVars;
import com.example.finalproject.items.UserToPost;
import com.example.finalproject.items.messageContent;
import com.example.finalproject.items.loginInfo;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersApiToken {
    private final Retrofit retrofit;
    private final WebServiceAPI webServiceAPI;


    public UsersApiToken() {
        retrofit = new Retrofit.Builder()
                .baseUrl(globalVars.server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getTokenWithFireBase(String username, String password, String token) {
        String authorizationHeader = "Bearer " + token;
        Call<ResponseBody> call = webServiceAPI.getTokenFromServerFireBase(authorizationHeader,new loginInfo(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String tokenRec = response.body().string();
                    System.out.println(tokenRec);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void postUser(UserToPost user,Callback<ResponseBody> callback ) {
        Call<ResponseBody> call = webServiceAPI.postUser(user);
        call.enqueue(callback);
    }

    public void getUser(String username, String password,Callback<ResponseBody> callback ) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(callback);
    }

    public void getChats(String username, String password,Callback<ResponseBody> callback ) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(callback);
    }

    public void postChat(String username, String password, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(callback);
    }

    public void postMessage(String username, String password, String id, String msg) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;

                    Call<ResponseBody> call2 = webServiceAPI.postMessage(authorizationHeader, id, new messageContent(msg));
                    call2.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response2) {
                            try {
                                String serverReturn = response2.body().string();
                                System.out.println(serverReturn);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call1, Throwable t) {
                            System.out.println("filed");
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("filed");
            }
        });
    }

    public void getMessages(String username, String password, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(callback);

       /* call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;

                    Call<List<MessageToGet>> call2 = webServiceAPI.getMessages(authorizationHeader, id);
                    call2.enqueue(new Callback<List<MessageToGet>>() {
                        @Override
                        public void onResponse(Call<List<MessageToGet>> call2, Response<List<MessageToGet>> response2) {
                            List<MessageToGet> serverReturn = response2.body();
                            System.out.println(serverReturn);
                        }

                        @Override
                        public void onFailure(Call<List<MessageToGet>> call2, Throwable t) {
                            System.out.println("filed");
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("filed");
            }
        });*/
    }
}

