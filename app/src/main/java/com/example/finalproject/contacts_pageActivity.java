package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.finalproject.adapters.ContactsListAdapter;
import com.example.finalproject.api.UsersApiToken;
import com.example.finalproject.api.WebServiceAPI;
import com.example.finalproject.items.Contact;
import com.example.finalproject.items.MessageLast;
import com.example.finalproject.items.MessageToGet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class contacts_pageActivity extends AppCompatActivity {
    private FloatingActionButton btnAdd;
    private FloatingActionButton btnLogout;
    private ImageView back_button;
    private List<Contact> contacts;
    private AppDB db;
    private PostDao postDao;
    private ContactsListAdapter adapter;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UsersApiToken user;
    private List<Contact> newCon;
    private MyService myService;

    private EditText searchMessage;
    private ImageView searchButton;


    /**
     * Function get userename that you just send him a meesage or got message by him, and get it the
     * top of the contact list.
     * @param username of the contact
     */
    public void moveContactToFirst(String username) {
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            if (contact.getUser().equals(username)) {
                contacts.remove(i);
                contacts.add(0, contact);
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myService= MyService.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_page);
        // Retrieve the values from the intent extras
        String username = globalVars.username;
        String password = globalVars.password;


        RecyclerView lstContacts = findViewById(R.id.lstContacts);
        adapter = new ContactsListAdapter(this);
        lstContacts.setAdapter(adapter);
        lstContacts.setLayoutManager(new LinearLayoutManager(this));


        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        List<DbObject> DbObj = postDao.index();
        contacts = new ArrayList<>();
        for(int i = 0; i < DbObj.size(); i++){
            Contact con = DbObj.get(i).getContactName();
            if(con.getLastMessage() == null){
                con.setLastMessage(new MessageLast("", "", ""));
            }
            contacts.add(con);
        }
        adapter.setContacts(contacts);
        myService.setAdapterCon(adapter);

        // Set onClickListener for the back button to go to WelcomeActivity
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(contacts_pageActivity.this, WelcomeActivity.class);
            if(getIntent().getStringExtra("CheckWithServer").equals("yes")) {
                intent.putExtra("CheckWithServer","yes");

            }
            else {
                intent.putExtra("CheckWithServer","not");
            }
            myService.setContactUserName(intent.getStringExtra(""));
            finish();
        });

        /*
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contacts_pageActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

         */

        adapter.setOnContactClickListener(new ContactsListAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                // Handle the click event for the contact item
                // Start the chat activity
                Intent intent = new Intent(getApplicationContext(), chatActivity.class);

                // Pass any necessary data to the chat activity using intent extras
                intent.putExtra("contactId", contact.getId());
                intent.putExtra("contactName", contact.getUser().getDisplayName());
                intent.putExtra("contactUserName",contact.getUser().getUsername());
                intent.putExtra("contactImg", contact.getUser().getProfilePic());
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });

        btnAdd= findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            Intent intentAddCon = new Intent(getApplicationContext(), ChooseConNameActivity.class);
            intentAddCon.putExtra("username", globalVars.username);
            intentAddCon.putExtra("password", globalVars.password);

            startActivity(intentAddCon);
        });



        btnLogout= findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            startActivity(intent);
        });

        //TODO take care of settings
        /*
        btnSettings=findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v -> {
            Intent goToSettings= new Intent(getApplicationContext(),SettingsActivity.class);
            goToSettings.putExtra("contactLayout",R.id.contactPageRelativeLayout);
            startActivity(goToSettings);

        });

         */

        String x= getIntent().getStringExtra("CheckWithServer");
        if(getIntent().getStringExtra("CheckWithServer").equals("yes")) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(globalVars.server)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            webServiceAPI = retrofit.create(WebServiceAPI.class);
            user = new UsersApiToken();
            Callback<ResponseBody> callbackForGetUserChatsInfo = new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (!response.isSuccessful()) {
                            Toast.makeText(contacts_pageActivity.this, "Incorrect username and/or password", Toast.LENGTH_LONG).show();
                        } else {
                            String token = response.body().string();
                            String authorizationHeader = "Bearer " + token;
                            Call<List<Contact>> call2 = webServiceAPI.getChats(authorizationHeader);
                            call2.enqueue(new Callback<List<Contact>>() {
                                @Override
                                public void onResponse(Call<List<Contact>> call2, Response<List<Contact>> response2) {
                                    List<Contact> serverReturn = response2.body();
                                    newCon=serverReturn;
                                    //creat a dcObject for the inseration
                                    List<DbObject> existingData = postDao.index();  // Retrieve existing data from the database
                                    for (Contact newData : serverReturn) {
                                        boolean found = false;

                                        for (DbObject existingRecord : existingData) {
                                            if (newData.getUser().getUsername().equals(existingRecord.getContactName().getUser().getUsername())) {  // Compare using unique identifier
                                                found = true;
                                                DbObject newDb = existingRecord;
                                                postDao.delete(existingRecord);
                                                Contact c = newDb.getContactName();
                                                c.setLastMessage(newData.getLastMessage());
                                                newDb.setContactName(c);
                                                postDao.insert(newDb);
                                                break;
                                            }
                                        }

                                        if (!found) {
                                            // Insert new record
                                            DbObject newObj = new DbObject(newData, null);
                                            postDao.insert(newObj);
                                        }
                                    }
                                    getAllMessagesForLogin(globalVars.username, globalVars.password);
                                    //adapter.setContacts(newCon);
                                    onResume();
                                }

                                @Override
                                public void onFailure(Call<List<Contact>> call2, Throwable t) {
                                    Toast.makeText(contacts_pageActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(contacts_pageActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                }
            };
            user.getChats(globalVars.username, globalVars.password, callbackForGetUserChatsInfo);
        }

        // Initialize search components
        searchMessage = findViewById(R.id.search_message);
        searchButton = findViewById(R.id.search_icon);

        // Click listener for search button
        searchButton.setOnClickListener(v -> performSearch());

        // Show the whole contact list by default
        performSearch();

    }

    private void performSearch() {
        String query = searchMessage.getText().toString().trim().toLowerCase();

        if (query.isEmpty()) {
            // If search is empty, show the whole list
            adapter.setContacts(contacts);
        } else {
            // Filter contacts by display name (partial match)
            List<Contact> filteredContacts = new ArrayList<>();
            for (Contact contact : contacts) {
                if (contact.getUser().getDisplayName().toLowerCase().contains(query)) {
                    filteredContacts.add(contact);
                }
            }

            // Show the filtered list
            adapter.setContacts(filteredContacts);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<DbObject> dbObjects = postDao.index();
        List<Contact> chats = new ArrayList<>();

        for (DbObject dbObject : dbObjects) {
            Contact contact = dbObject.getContactName();
            if (contact != null) {
                if (contact.getLastMessage() == null) {
                    contact.setLastMessage(new MessageLast("", "", ""));
                }
                chats.add(contact);
            }
        }

        // Update the adapter with the contact list
        adapter.setContacts(chats);

        // Perform the search operation
        performSearch();
    }
    public void getAllMessagesForLogin(String userName, String password) {
        Callback<ResponseBody> getAllMessagesCallback =new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;
                    List<DbObject> listCon= postDao.index();
                    for (DbObject contactDb:listCon) {
                        Call<List<MessageToGet>> call2 = webServiceAPI.getMessages(authorizationHeader, contactDb.getContactName().getId());
                        call2.enqueue(new Callback<List<MessageToGet>>() {
                            @Override
                            public void onResponse(Call<List<MessageToGet>> call2, Response<List<MessageToGet>> response2) {
                                List<MessageToGet> serverReturn = response2.body();
                                contactDb.setMsgList(serverReturn);
                                postDao.update(contactDb);
                            }

                            @Override
                            public void onFailure(Call<List<MessageToGet>> call2, Throwable t) {
                                Toast.makeText(contacts_pageActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(contacts_pageActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getMessages(userName,password,getAllMessagesCallback);
    }
}