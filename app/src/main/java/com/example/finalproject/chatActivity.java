package com.example.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.finalproject.adapters.MessageListAdapter;
import com.example.finalproject.api.UsersApiToken;
import com.example.finalproject.api.WebServiceAPI;
import com.example.finalproject.items.Contact;
import com.example.finalproject.items.MessageToGet;
import com.example.finalproject.items.messageContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chatActivity extends AppCompatActivity {

    private FloatingActionButton btnBack;
    private ImageView profileImg;
    private TextView displayName;
    private FloatingActionButton btnSend;
    private EditText inputMes;
    private AppDB db;
    private PostDao postDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UsersApiToken user;
    private MyService myService;

    protected void onCreate(Bundle savedInstanceState) {
        myService = MyService.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        // Retrieve the extras from the intent
        Intent intent = getIntent();
        String contactId = intent.getStringExtra("contactId");
        //String contactUsername = intent.getStringExtra("contactUsername");
        String contactName = intent.getStringExtra("contactName");
        String contactImg = intent.getStringExtra("contactImg");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        myService.setContactUserName(intent.getStringExtra("contactUserName"));
        // Find views by their IDs
        displayName = findViewById(R.id.displayName);
        profileImg = findViewById(R.id.profileImg);

        // Set the values
        displayName.setText(contactName);
        try {
            byte[] decodedBytes = android.util.Base64.decode(contactImg, android.util.Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            profileImg.setImageBitmap(decodedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RecyclerView lstMessages = findViewById(R.id.lstMessages);
        final MessageListAdapter adapter = new MessageListAdapter(this, username);
        lstMessages.setAdapter(adapter);
        lstMessages.setLayoutManager(new LinearLayoutManager(this));

        // Makes sure to show it from the latest message
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lstMessages.setLayoutManager(layoutManager);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        List<DbObject> DbObj = postDao.index();
        List<MessageToGet> msgs;
        int index = -1;
        for (int i = 0; i < DbObj.size(); i++) {
            if (contactName.equals(DbObj.get(i).getContactName().getUser().getDisplayName())) {
                index = i;
            }
        }
        msgs = DbObj.get(index).getMsgList();

        adapter.setMessages(msgs);
        myService.setOurAdapter(adapter);

        // Makes sure to show it from the latest message
        int lastPosition;
        if (msgs == null) {
            lastPosition = 0;
        } else {
            lastPosition = msgs.size() - 1;

        }
        lstMessages.scrollToPosition(lastPosition);


        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            myService.setContactUserName(intent.getStringExtra(""));
            finish();
        });


        btnSend = findViewById(R.id.btnSend);
        user = new UsersApiToken();
        retrofit = new Retrofit.Builder()
                .baseUrl(globalVars.server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        inputMes = findViewById(R.id.etMessageInput);

        int finalIndex = index;
        btnSend.setOnClickListener(v -> {
            String newMessage = inputMes.getText().toString();
            if (newMessage.isEmpty()) {
                Toast.makeText(chatActivity.this, "type the a message!", Toast.LENGTH_LONG).show(); //error message for server
            } else {
                Callback<ResponseBody> callbackPostMes = new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String token = response.body().string();
                            String authorizationHeader = "Bearer " + token;

                            Call<ResponseBody> call2 = webServiceAPI.postMessage(authorizationHeader, contactId, new messageContent(newMessage));
                            call2.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response2) {

                                    if (!response.isSuccessful()) {
                                        Toast.makeText(chatActivity.this, "cant send msg", Toast.LENGTH_LONG).show();
                                    } else {
                                        updateDb(DbObj.get(finalIndex), username, password, contactId, adapter);
                                        inputMes.setText("");
                                        updateChatTime(username, password, contactId, DbObj);
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
                };
                user.getMessages(username, password, callbackPostMes);

            }
        });
    }

    public void updateDb(DbObject dbObj, String username, String password, String contactId, MessageListAdapter adapter) {

        //first of all delete the chat from the db and then add the new chat - update
        //postDao.delete(dbObj);
        Callback<ResponseBody> getAllMessagesCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;
                    Call<List<MessageToGet>> call2 = webServiceAPI.getMessages(authorizationHeader, contactId);
                    call2.enqueue(new Callback<List<MessageToGet>>() {
                        @Override
                        public void onResponse(Call<List<MessageToGet>> call2, Response<List<MessageToGet>> response2) {
                            dbObj.setMsgList(response2.body());
                            postDao.update(dbObj);
                            List<MessageToGet> msgs;
                            msgs = dbObj.getMsgList();
                            adapter.setMessages(msgs);
                            myService.setOurAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<List<MessageToGet>> call2, Throwable t) {
                            Toast.makeText(chatActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(chatActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getMessages(username, password, getAllMessagesCallback);

    }

    public void updateChatTime(String username, String password, String contactId, List<DbObject> DbObj) {
        Callback<ResponseBody> getAllMessagesCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;
                    Call<List<Contact>> call2 = webServiceAPI.getChats(authorizationHeader);
                    call2.enqueue(new Callback<List<Contact>>() {
                        @Override
                        public void onResponse(Call<List<Contact>> call2, Response<List<Contact>> response2) {
                            List<Contact> l = response2.body();
                            int index = -1;
                            if (l != null) {
                                for (int i = 0; i < l.size(); i++) {
                                    if (l.get(i).getId().equals(contactId)) {
                                        index = i;
                                    }
                                }
                                DbObject newDb = DbObj.get(index);
                                postDao.delete(DbObj.get(index));
                                Contact c = newDb.getContactName();
                                c.setLastMessage(l.get(index).getLastMessage());
                                newDb.setContactName(c);
                                postDao.insert(newDb);
                            }

                        }

                        @Override
                        public void onFailure(Call<List<Contact>> call2, Throwable t) {
                            Toast.makeText(chatActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(chatActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getMessages(username, password, getAllMessagesCallback);
    }

    /**
     * Function get userename that you just send him a meesage or got message by him, and get it the
     * top of the contact list.
     *
     * @param username the username
     */
    public void moveContactToFirst(String username, List<Contact> contacts) {
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            if (contact.getUser().getUsername().equals(username)) {
                contacts.remove(i);
                contacts.add(0, contact);
                break;
            }
        }
    }
}
