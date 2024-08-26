package com.example.finalproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.finalproject.adapters.ContactsListAdapter;
import com.example.finalproject.adapters.MessageListAdapter;
import com.example.finalproject.api.UsersApiToken;
import com.example.finalproject.api.WebServiceAPI;
import com.example.finalproject.items.Contact;
import com.example.finalproject.items.MessageLast;
import com.example.finalproject.items.MessageToGet;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyService extends FirebaseMessagingService {
    private String contactUserName;
    private MessageListAdapter adapter;

    public void setAdapterCon(ContactsListAdapter adapterCon) {
        this.adapterCon = adapterCon;
    }

    private ContactsListAdapter adapterCon = null;
    private boolean refreshChat = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static MyService myService;

    public void setContactUserName(String contactUserName) {
        this.contactUserName = contactUserName;
    }

    public void setOurAdapter(MessageListAdapter adapter) {
        this.adapter = adapter;
    }

    public static MyService getInstance() {
        if (myService == null) {
            myService = new MyService();
        }
        return myService;
    }

    public MyService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            createNotificationChannel();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.messi)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (!(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)) {
                notificationManager.notify(1, builder.build());
            }
            if (!(globalVars.username.equals("") && globalVars.password.equals(""))) {
                AppDB db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
                PostDao postDao = db.postDao();
                UsersApiToken user = new UsersApiToken();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(globalVars.server)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
                Callback<ResponseBody> getAllMessagesCallback = new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String token = response.body().string();
                            String authorizationHeader = "Bearer " + token;


                            Call<List<Contact>> call3 = webServiceAPI.getChats(authorizationHeader);


                            call3.enqueue(new Callback<List<Contact>>() {
                                @Override
                                public void onResponse(Call<List<Contact>> call3, Response<List<Contact>> response2) {
                                    List<Contact> l = response2.body();
                                    //creat a dcObject for the inseration
                                    List<DbObject> existingData = postDao.index();  // Retrieve existing data from the database
                                    String[] words = remoteMessage.getNotification().getTitle().split(" ");
                                    String reciveUsername = "";
                                    for (int i = 0; i < words.length; i++) {
                                        if (i > 2) {
                                            reciveUsername += words[i];
                                            if (i < words.length - 1) {
                                                reciveUsername += " ";
                                            }
                                        }
                                    }
                                    for (int i = 0; i < l.size(); i++) {
                                        if (l.get(i).getUser().getUsername().equals(reciveUsername)) {
                                            if (reciveUsername.equals(myService.contactUserName)) {
                                                refreshChat = true;
                                            }
                                            DbObject newDb = existingData.get(i);
                                            postDao.delete(existingData.get(i));
                                            Contact c = newDb.getContactName();
                                            c.setLastMessage(l.get(i).getLastMessage());
                                            newDb.setContactName(c);
                                            postDao.insert(newDb);

                                            Call<List<MessageToGet>> call2 = webServiceAPI.getMessages(authorizationHeader, newDb.getContactName().getId());
                                            call2.enqueue(new Callback<List<MessageToGet>>() {
                                                @Override
                                                public void onResponse(Call<List<MessageToGet>> call2, Response<List<MessageToGet>> response2) {
                                                    List<MessageToGet> serverReturn = response2.body();
                                                    newDb.setMsgList(serverReturn);
                                                    postDao.update(newDb);
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (refreshChat) {
                                                                myService.adapter.setMessages(serverReturn);
                                                                refreshChat = false;
                                                            }
                                                            if (myService.adapterCon != null) {
                                                                List<DbObject> DbObj = postDao.index();
                                                                List<Contact> chats = new ArrayList<>();
                                                                for(int i = 0; i < DbObj.size(); i++){
                                                                    Contact con = DbObj.get(i).getContactName();
                                                                    if(con.getLastMessage() == null){
                                                                        con.setLastMessage(new MessageLast("", "", ""));
                                                                    }
                                                                    chats.add(con);
                                                                }
                                                                myService.adapterCon.setContacts(chats);
                                                            }
                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onFailure(Call<List<MessageToGet>> call2, Throwable t) {
                                                }
                                            });


                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Contact>> call3, Throwable t) {
                                }
                            });


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                };
                user.getMessages(globalVars.username, globalVars.password, getAllMessagesCallback);
            }

        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "my Channel", importance);
            channel.setDescription("channel for whatsapp");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}