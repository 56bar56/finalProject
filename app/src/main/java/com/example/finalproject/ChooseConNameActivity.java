package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.finalproject.api.UsersApiToken;
import com.example.finalproject.api.WebServiceAPI;
import com.example.finalproject.items.AddNewContact;
import com.example.finalproject.items.Contact;
import com.example.finalproject.items.ContactForCreate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChooseConNameActivity extends AppCompatActivity {

    private EditText editConName;
    private Button buttonSave;
    private FloatingActionButton buttonCancel;
    private AppDB db;
    private PostDao postDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UsersApiToken user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_con_name);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        user = new UsersApiToken();
        retrofit = new Retrofit.Builder()
                .baseUrl(globalVars.server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        editConName = findViewById(R.id.editNewConName);
        buttonSave = findViewById(R.id.btnSaveCon);
        buttonCancel = findViewById(R.id.btnCancelAdd);

        buttonSave.setOnClickListener(v -> {
            Intent intentFromChats = getIntent();
            String contactName = editConName.getText().toString();
            if (contactName.isEmpty()) {
                Toast.makeText(ChooseConNameActivity.this, "type the contact's username!", Toast.LENGTH_LONG).show(); //error message for server
            } else {
                boolean ifAlreadyInside = checkIfInside(contactName);
                if (ifAlreadyInside) {
                    Callback<ResponseBody> postContactCallback = new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String token = response.body().string();
                                String authorizationHeader = "Bearer " + token;

                                Call<AddNewContact> call2 = webServiceAPI.postChat(authorizationHeader, new ContactForCreate(contactName));
                                call2.enqueue(new Callback<AddNewContact>() {
                                    @Override
                                    public void onResponse(Call<AddNewContact> call2, Response<AddNewContact> response2) {
                                        if (!response2.isSuccessful()) {
                                            Toast.makeText(ChooseConNameActivity.this, "no such username", Toast.LENGTH_LONG).show(); //error message for server

                                        } else {
                                            PutContactsInDb(intentFromChats.getStringExtra("username"), intentFromChats.getStringExtra("password")); //call to the function that get all contacts from the server into our db
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<AddNewContact> call2, Throwable t) {
                                        Toast.makeText(ChooseConNameActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show(); //error message for server
                                    }
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ChooseConNameActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show(); //error message for server
                        }
                    };
                    user.postChat(intentFromChats.getStringExtra("username"), intentFromChats.getStringExtra("password"), postContactCallback);
                }
                else{
                    Toast.makeText(ChooseConNameActivity.this, "you already have a chat with that user", Toast.LENGTH_LONG).show(); //error message for server
                }
            }

        });

        buttonCancel.setOnClickListener(v -> finish());
    }
    public boolean checkIfInside(String contactName){
        List<DbObject> l = postDao.index();
        boolean flag = false;
        for(int i = 0; i < l.size(); i++){
            if(l.get(i).getContactName().getUser().getUsername().equals(contactName)){
                flag = true;
            }
        }
        return !flag;
    }

    /**
     * this function call the server and take from him the contacts and put in our local db.
     * @param username our username
     * @param password our password
     */
    public void PutContactsInDb(String username, String password) {
        Callback<ResponseBody> getContactsCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;
                    Call<List<Contact>> call2 = webServiceAPI.getChats(authorizationHeader);
                    call2.enqueue(new Callback<List<Contact>>() {
                        @Override
                        public void onResponse(Call<List<Contact>> call2, Response<List<Contact>> response2) {
                            List<Contact> serverReturn = response2.body();
                            //creat a dcObject for the inseration
                            List<DbObject> existingData = postDao.index();  // Retrieve existing data from the database

                            for (Contact newData : serverReturn) {
                                boolean found = false;

                                for (DbObject existingRecord : existingData) {
                                    if (newData.getUser().getUsername().equals(existingRecord.getContactName().getUser().getUsername())) {  // Compare using unique identifier
                                        found = true;
                                        break;
                                    }
                                }

                                if (!found) {
                                    // Insert new record
                                    DbObject newObj = new DbObject(newData, null);
                                    postDao.insert(newObj);
                                }
                            }
                            finish();
                        }

                        @Override
                        public void onFailure(Call<List<Contact>> call2, Throwable t) {
                            Toast.makeText(ChooseConNameActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show(); //error message for server
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ChooseConNameActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show(); //error message for server
            }
        };
        user.getChats(username, password, getContactsCallback);
    }
}
