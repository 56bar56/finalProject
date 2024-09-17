package com.example.finalproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.finalproject.api.UsersApiToken;
import com.example.finalproject.items.logInSave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button loginBtn, registerBtn;
    private UsersApiToken user;
    private AppDB db2;
    private LogInSaveDao logInSaveDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db2 = Room.databaseBuilder(getApplicationContext(), AppDB.class, "sveLogin").allowMainThreadQueries().build();
        logInSaveDao = db2.logInSaveDao();
        List<logInSave> userLoged = logInSaveDao.index();
        if (userLoged.size() == 0) {
            setContentView(R.layout.activity_main_page);
            loginBtn = findViewById(R.id.loginPage);
            registerBtn = findViewById(R.id.registerPage);
            loginBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), loginActivity.class)));
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //TODO fix
            registerBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), registerPage.class)));
            //}
        } else {
            globalVars.username = userLoged.get(0).getUsername();
            globalVars.password = userLoged.get(0).getPassword();
            user = new UsersApiToken();


            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(Task<String> task) {
                    if (!task.isSuccessful()) {
                        // Handle the error if necessary
                        return;
                    }
                    // Get the new FCM registration token
                    String newToken = task.getResult();
                    // Send the token to your server or handle it as needed
                    user.getTokenWithFireBase(globalVars.username, globalVars.password, newToken);
                }
            });
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            intent.putExtra("CheckWithServer", "yes");
            startActivity(intent);
        }
    }
}
