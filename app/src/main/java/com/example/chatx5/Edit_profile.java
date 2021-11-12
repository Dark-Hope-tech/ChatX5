package com.example.chatx5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatx5.Activity.home_activity;
import com.example.chatx5.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profile extends AppCompatActivity {
    CircleImageView profile_image;
    EditText profile_name,profile_status;
    TextView profile_email,save_btn,cancel_btn;
    String name,email,status;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    Uri SelectedImgeURI;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);

        setContentView(R.layout.activity_edit_profile);
        profile_image=findViewById(R.id.profile_image);
        profile_name=findViewById(R.id.profile_name);
        profile_status=findViewById(R.id.profile_status);
        profile_email=findViewById(R.id.profile_email);
        save_btn=findViewById(R.id.save_btn);
        cancel_btn=findViewById(R.id.cancel_btn);
        auth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("user").child(auth.getUid());
        StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email=snapshot.child("mail").getValue().toString();
                name=snapshot.child("name").getValue().toString();
                status=snapshot.child("status").getValue().toString();
                String image=snapshot.child("imageURI").getValue().toString();
                profile_name.setText(name);
                profile_email.setText(email);
                profile_status.setText(status);
                Picasso.get().load(image).into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Edit_profile.this, home_activity.class));
                onBackPressed();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name=profile_name.getText().toString();
                status=profile_status.getText().toString();
                if(SelectedImgeURI!=null){
                    progressDialog.show();
                    storageReference.putFile(SelectedImgeURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageURI=uri.toString();
                                    user us=new user(auth.getUid(),name,email,finalImageURI,status);
                                    reference.setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(Edit_profile.this, "Profile Successfully Updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Edit_profile.this, home_activity.class));
                                            }
                                            else{
                                                progressDialog.dismiss();
                                                Toast.makeText(Edit_profile.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else{
                    progressDialog.show();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageURI=uri.toString();
                            user us=new user(auth.getUid(),name,email,finalImageURI,status);
                            reference.setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(Edit_profile.this, "Profile Successfully Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Edit_profile.this, home_activity.class));
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(Edit_profile.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                SelectedImgeURI=data.getData();
                profile_image.setImageURI(SelectedImgeURI);
            }
        }
    }

}