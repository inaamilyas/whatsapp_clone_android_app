package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting to full screen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Getting email and password from shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("myChattingApp", MODE_PRIVATE);
                String email = sharedPreferences.getString("userEmail", "");
                String password = sharedPreferences.getString("userPassword", "");

                if (email.equals("") && password.equals("")){
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                } else{
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 2000);

    }
}