package com.example.chatx5.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatx5.Edit_profile;
import com.example.chatx5.Find_friend;
import com.example.chatx5.Friend_Ac;
import com.example.chatx5.R;
import com.example.chatx5.random_chat_home;
import com.example.chatx5.user;
import com.example.chatx5.user_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

import de.hdodenhof.circleimageview.CircleImageView;

public class home_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth auth;
    RecyclerView recyclerView;
    user_adapter adapter;
    FirebaseDatabase database;
    ArrayList<user> userArrayList;
    Boolean isLogedout = false;
    TextView nav_name;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        circleImageView = findViewById(R.id.nav_profileimage);
        View header = navigationView.getHeaderView(0);
        circleImageView = header.findViewById(R.id.nav_profileimage);
        nav_name = header.findViewById(R.id.nav_name);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userArrayList = new ArrayList<>();
        ManageConnection();

        DatabaseReference ref = database.getReference().child("user").child(auth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user use = snapshot.getValue(user.class);
                nav_name.setText(use.getName());
                Glide.with(header).load(use.getImageURI()).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerView = findViewById(R.id.main_user_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference databaseReference = database.getReference().child("friends").child("AcceptedFriends").child(auth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id = dataSnapshot.getKey();
                    DatabaseReference reference = database.getReference().child("user").child(id);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            user use = snapshot.getValue(user.class);
                            userArrayList.add(use);
                            adapter = new user_adapter(home_activity.this, userArrayList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_activity.this,Find_friend.class));
            }
        });
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(home_activity.this, Registration.class));
        }

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void ManageConnection() {
        DatabaseReference ref = database.getReference("presence/" + auth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference reff = database.getReference().child("presence").child(auth.getUid());
                if (!isLogedout) {
                    reff.setValue("Online");
                    reff.onDisconnect().setValue("Offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_omegle:
                startActivity(new Intent(home_activity.this, random_chat_home.class));
                break;
            case R.id.nav_logout:
                isLogedout = true;
                DatabaseReference reff = database.getReference().child("presence").child(auth.getUid());
                reff.setValue("Offline").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(home_activity.this, Log_in.class));
                        }
                    }
                });
                break;
            case R.id.nav_editprofile:
                startActivity(new Intent(home_activity.this, Edit_profile.class));
                break;
            case R.id.nav_requests:
                startActivity(new Intent(home_activity.this,Friend_Ac.class));
                break;
        }
        return true;
    }
}