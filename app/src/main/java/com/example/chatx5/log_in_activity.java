package com.example.chatx5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class log_in_activity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Intent intent=new Intent(log_in_activity.this,Log_in.class);
        startActivity((intent));
//        if(auth.getCurrentUser()==null){
//            startActivity(new Intent(log_in_activity.this,Registration.class));
//        }
    }
}