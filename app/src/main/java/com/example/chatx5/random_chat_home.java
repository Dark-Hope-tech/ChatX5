package com.example.chatx5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatx5.Activity.Chats_activity;
import com.example.chatx5.Activity.home_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class random_chat_home extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    random_adapter rand_adpt;
    RecyclerView random_recycler_view;
    ArrayList<user> userArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_chat_home);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        userArrayList=new ArrayList<>();

        DatabaseReference reference=database.getReference("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    user user=dataSnapshot.getValue(user.class);
                    userArrayList.add(user);
                }
                rand_adpt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        random_recycler_view=findViewById(R.id.random_chat_recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        random_recycler_view.setLayoutManager(linearLayoutManager);
        rand_adpt= new random_adapter(random_chat_home.this, userArrayList);
        random_recycler_view.setAdapter(rand_adpt);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(random_chat_home.this, home_activity.class));
    }
}