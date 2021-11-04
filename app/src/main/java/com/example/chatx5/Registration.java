package com.example.chatx5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {

    EditText regis_name,regis_mail,regis_pass,regis_confi_pass;
    TextView regis_sign_up_btn,log_in_btn;
    CircleImageView profile_img;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri image_uri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);

        regis_name=findViewById(R.id.regis_name);
        regis_mail=findViewById(R.id.regis_mail);
        regis_pass=findViewById(R.id.regis_pass);
        regis_confi_pass=findViewById(R.id.regis_confirm_pass);
        regis_sign_up_btn=findViewById(R.id.regis_sign_up_btn);
        log_in_btn=findViewById(R.id.log_in);
        profile_img=findViewById(R.id.profile_image);
        String status="Codeforces fodenge bisi";


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        regis_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                String Name=regis_name.getText().toString();
                String Pass=regis_pass.getText().toString();
                String Confi_pass=regis_confi_pass.getText().toString();
                String en_mail=regis_mail.getText().toString();

                if(TextUtils.isEmpty(en_mail)||TextUtils.isEmpty(Pass)||TextUtils.isEmpty(Name)||TextUtils.isEmpty(Confi_pass)){

                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "Field cannot be Empty!!", Toast.LENGTH_SHORT).show();
                }

                else if(!en_mail.matches(emailPattern)){
                    progressDialog.dismiss();
                    regis_mail.setError("INVALID MAIL");
                    Toast.makeText(Registration.this, "INVALID EMAIL", Toast.LENGTH_SHORT).show();
                }

                else if(!Pass.equals(Confi_pass)){
                    progressDialog.dismiss();
                    regis_confi_pass.setError("INVALID PASSWORD");
                    Toast.makeText(Registration.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
                }
                else if(Pass.length()<6){
                    progressDialog.dismiss();
                    regis_pass.setError("INVALID PASSWORD");
                    Toast.makeText(Registration.this, "Password length must be greater than 6", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(en_mail,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());

                                if(image_uri!=null){
                                    storageReference.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI=uri.toString();
                                                        user us=new user(auth.getUid(),Name,en_mail,imageURI,status);
                                                        reference.setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    progressDialog.dismiss();
                                                                    startActivity(new Intent(Registration.this, home_activity.class));
                                                                }
                                                                else{
                                                                    Toast.makeText(Registration.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else{
                                    String status="Codeforces fodenge bisi";
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/chatx5-429c5.appspot.com/o/default_profile.jpg?alt=media&token=721cb7f5-1414-41da-93a0-d845856a374e";
                                    user us=new user(auth.getUid(),Name,en_mail,imageURI,status);
                                    reference.setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                startActivity(new Intent(Registration.this,home_activity.class));
                                            }
                                            else{
                                                Toast.makeText(Registration.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                Toast.makeText(Registration.this, "User registered Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Registration.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });


        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this, Log_in.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                image_uri=data.getData();
                profile_img.setImageURI(image_uri);
            }
        }
    }
}
