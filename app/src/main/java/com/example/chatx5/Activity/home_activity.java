package com.example.chatx5.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatx5.Edit_profile;
import com.example.chatx5.R;
import com.example.chatx5.random_chat_home;
import com.example.chatx5.user;
import com.example.chatx5.user_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class home_activity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView recyclerView;
    user_adapter adapter;
    FirebaseDatabase database;
    ArrayList<user> userArrayList;
    Boolean isLogedout=false;
    TextView btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        userArrayList=new ArrayList<>();
        ManageConnection();
        btn=findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_activity.this, random_chat_home.class));
                finish();
            }
        });
        DatabaseReference reference=database.getReference("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    user user=dataSnapshot.getValue(user.class);
                    userArrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView=findViewById(R.id.main_user_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new user_adapter(home_activity.this,userArrayList);
        recyclerView.setAdapter(adapter);
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(home_activity.this, Registration.class));
        }
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Log Out");
        menu.add("Change Profile");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Log Out")){
            isLogedout=true;
            DatabaseReference reff=database.getReference().child("presence").child(auth.getUid());
            reff.setValue("Offline").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(home_activity.this,Log_in.class));
                    }
                }
            });
        }
        else if(item.getTitle().equals("Change Profile")){
            startActivity(new Intent(home_activity.this, Edit_profile.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void ManageConnection(){
        DatabaseReference ref=database.getReference("presence/"+auth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference reff=database.getReference().child("presence").child(auth.getUid());
                if(!isLogedout) {
                    reff.setValue("Online");
                    reff.onDisconnect().setValue("Offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}