package com.example.chatx5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Group_chat extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;

    ArrayList<Message> group_messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        group_messages=new ArrayList<>();
        DatabaseReference reference=database.getReference().child("RandomChat");
    }
}