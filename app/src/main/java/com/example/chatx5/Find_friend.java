package com.example.chatx5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatx5.Activity.home_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Find_friend extends AppCompatActivity {
    EditText editText;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String email;
    TextView txv,friend_name,friend_email;
    ImageView button,saddog;
    LinearLayout user_found;
    boolean isFound=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        editText=findViewById(R.id.search_friend);
        txv=findViewById(R.id.no_user);
        user_found=findViewById(R.id.user_found);
        user_found.setVisibility(LinearLayout.INVISIBLE);
        button=findViewById(R.id.search);
        friend_name=findViewById(R.id.friend_name);
        friend_email=findViewById(R.id.friend_email);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        saddog=findViewById(R.id.saddog);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=editText.getText().toString();
                searh_friend(email);
            }
        });
    }

    private void searh_friend(String mail) {
        DatabaseReference reference=database.getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    user us=dataSnapshot.getValue(user.class);
                    String com=us.getMail().toString();
                    if(com.equals(mail)){
                        txv.setVisibility(TextView.INVISIBLE);
                        saddog.setVisibility(ImageView.INVISIBLE);
                        user_found.setVisibility(LinearLayout.VISIBLE);
                        friend_name.setText(us.getName());
                        friend_email.setText(us.getMail());
                        user_found.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference reference1=database.getReference().child("friends").child("friend_req").child(us.getUid()).child(auth.getUid());
                                reference1.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Find_friend.this, "Friend Request sent Successfully", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(Find_friend.this, "Error in sending req", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        editText.setText("");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Find_friend.this, home_activity.class));
    }
}