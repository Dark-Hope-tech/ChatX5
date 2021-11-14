package com.example.chatx5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.chatx5.Activity.home_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Friend_Ac extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView friend_ac_RV;
    ArrayList<String> pendingFriendReq;
    friend_req_adapter frnd_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
//        friend_ac_RV=findViewById(R.id.recycler_friend_ac);
        pendingFriendReq=new ArrayList<>();
        friend_ac_RV=findViewById(R.id.recycler_friend_ac);
        friend_ac_RV.setLayoutManager(new LinearLayoutManager(this));

        System.out.println("uid ="+ auth.getUid());
        DatabaseReference reference=database.getReference().child("friends").child("friend_req").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    pendingFriendReq.add(dataSnapshot.getKey());
                }
                System.out.println(pendingFriendReq);
                frnd_adapter=new friend_req_adapter(Friend_Ac.this,pendingFriendReq);
                friend_ac_RV.setAdapter(frnd_adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Log.d("array",pendingFriendReq.get(0));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Friend_Ac.this, home_activity.class));
    }
}