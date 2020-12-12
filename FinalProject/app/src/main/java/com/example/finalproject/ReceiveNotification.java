package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReceiveNotification extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //COLOR SCHEMES
        Window window = ReceiveNotification.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ReceiveNotification.this, R.color.HotPink));


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(currentUser==null){
                    Toast.makeText(ReceiveNotification.this, "No user found", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ReceiveNotification.this,SignupLogin.class));
                    finish();
                }
                else{
                    if(currentUser.isEmailVerified()) {
                        Toast.makeText(ReceiveNotification.this, "User already signed in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ReceiveNotification.this, HomeActivity.class));
                        finish();
                    }
                    else{
                        Toast.makeText(ReceiveNotification.this, "Please verify your email and login.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ReceiveNotification.this, SignupLogin.class));
                        finish();
                    }
                }

            }
        }.start();
    }
}