package com.example.finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.finalproject.api.UsersApiToken;
import com.example.finalproject.api.WebServiceAPI;
import com.example.finalproject.items.Contact;
import com.example.finalproject.items.MessageToGet;
import com.example.finalproject.items.UserToGet;
import com.example.finalproject.items.logInSave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class loginActivity extends AppCompatActivity {
    Button loginBtn;
    TextView toRegister;
    private EditText editLoginUsername;
    private EditText editLoginPassword;
    private AppDB db;
    private PostDao postDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UsersApiToken user;
    private AppDB db2;
    private LogInSaveDao logInSaveDao;
    private EditText passwordEditText;
    private ImageView eyeIcon;
    private boolean isPasswordVisible = false; // Track if the password is currently visible


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        loginBtn = findViewById(R.id.logInBtn);
        toRegister = findViewById(R.id.noAccount);
        editLoginUsername = findViewById(R.id.LoginUsername);
        editLoginPassword = findViewById(R.id.LoginPassword);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        db2=Room.databaseBuilder(getApplicationContext(),AppDB.class,"sveLogin").allowMainThreadQueries().build();
        logInSaveDao=db2.logInSaveDao();
        postDao.deleteAll();
        logInSaveDao.deleteAll();
        user = new UsersApiToken();
        retrofit = new Retrofit.Builder()
                .baseUrl(globalVars.server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editLoginUsername.getText().toString();
                String password = editLoginPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    //is empty
                } else {
                    globalVars.username=username;
                    globalVars.password=password;
                    Callback<ResponseBody> callbackForGetUserInfo = new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(loginActivity.this, "Incorrect username and/or password", Toast.LENGTH_LONG).show();
                                } else {
                                    String token = response.body().string();
                                    String authorizationHeader = "Bearer " + token;
                                    logInSaveDao.insert(new logInSave(globalVars.username,globalVars.password));
                                    String funcUserName = username;
                                    Call<UserToGet> call2 = webServiceAPI.getUser(authorizationHeader, funcUserName);
                                    call2.enqueue(new Callback<UserToGet>() {
                                        @Override
                                        public void onResponse(Call<UserToGet> call2, Response<UserToGet> response2) {
                                            UserToGet serverReturn = response2.body();
                                            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                            intent.putExtra("username", serverReturn.getUsername());
                                            intent.putExtra("displayName", serverReturn.getDisplayName());
                                            intent.putExtra("profilePic", serverReturn.getProfilePic());
                                            intent.putExtra("password", password);
                                            //now we need to get the information out of the server

                                            //////////////////////////////////////////////////////
                                            dbInfo(intent, username, password);
                                            //startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<UserToGet> call2, Throwable t) {
                                            Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                        }
                    };
                    user.getUser(username, password, callbackForGetUserInfo);
                }
            }
        });
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure the intent is only created and used if SDK version condition is met
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) { //TODO fix
                    Intent newIntent = new Intent(getApplicationContext(), registerPage.class);
                    startActivity(newIntent); // Only start activity if intent is not null
                } else {
                    // Optionally handle cases for older versions if needed
                    Toast.makeText(getApplicationContext(), "Unsupported Android version", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Taking care of showing the password if clicking on the eye icon
        passwordEditText = findViewById(R.id.LoginPassword);
        eyeIcon = findViewById(R.id.eye);

        // Set onClickListener on the eye icon
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPasswordVisible) {
                    // Show password and change the eye icon
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeIcon.setImageResource(R.drawable.open_eye); // Use open eye drawable
                    isPasswordVisible = true;

                    // Revert back to password mode after 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passwordEditText.setSelection(passwordEditText.length()); // Move cursor to the end
                            eyeIcon.setImageResource(R.drawable.closed_eye); // Use closed eye drawable
                            isPasswordVisible = false;
                        }
                    }, 2000); // 2 seconds delay
                }
            }
        });

    }

    public void dbInfo(Intent intent, String username, String password) {
        Callback<ResponseBody> callbackForGetUserChatsInfo = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(loginActivity.this, "Incorrect username and/or password", Toast.LENGTH_LONG).show();
                    } else {
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
                                List<DbObject> check = postDao.index();  // Retrieve existing data from the database
                                getAllMessagesForLogin(username,password);
                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if (task.isSuccessful()) {
                                                    String newToken = task.getResult();
                                                    user.getTokenWithFireBase(globalVars.username, globalVars.password, newToken);
                                                } else {
                                                    // Handle error
                                                    Toast.makeText(loginActivity.this, "Failed to get Firebase token", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                intent.putExtra("CheckWithServer","not");
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<List<Contact>> call2, Throwable t) {
                                Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getChats(username, password, callbackForGetUserChatsInfo);
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
                                Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getMessages(userName,password,getAllMessagesCallback);
    }
}

