package com.example.chatx5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    ImageView imageView;
    TextView yes_btn,no_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        imageView=findViewById(R.id.log_out_btn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog= new Dialog(home_activity.this,R.style.Dioluge);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.show();
                yes_btn=dialog.findViewById(R.id.Yes_btn);
                no_btn=dialog.findViewById(R.id.no_btn);

                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(home_activity.this,log_in_activity.class));
                    }
                });

                no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });


        if(auth.getCurrentUser()==null){
            startActivity(new Intent(home_activity.this, Registration.class));
        }
    }
}