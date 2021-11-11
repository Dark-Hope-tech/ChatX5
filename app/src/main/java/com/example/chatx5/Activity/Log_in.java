package com.example.chatx5.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.VirtualLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatx5.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Log_in extends AppCompatActivity {
    TextView sign_in;
    EditText log_in_mail;
    EditText log_in_pas;
    TextView sign_in_btn;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);

        database=FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Log_in.this, home_activity.class));
            finish();
        }
        else {
            setContentView(R.layout.activity_log_in2);
            sign_in = findViewById(R.id.sign_in_txt);
            log_in_mail = findViewById(R.id.log_in_email);
            log_in_pas = findViewById(R.id.log_in_pass);
            sign_in_btn = findViewById(R.id.sing_in_btn);
            sign_in_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    progressDialog.show();

                    String email = log_in_mail.getText().toString();
                    String password = log_in_pas.getText().toString();
                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        progressDialog.dismiss();
                        Toast.makeText(Log_in.this, "ENTER VALID DATA!!", Toast.LENGTH_SHORT).show();
                    } else if (!email.matches(emailPattern)) {
                        progressDialog.dismiss();
                        log_in_mail.setError("INVALID EMAIL");
                        Toast.makeText(Log_in.this, "INVALID EMAIL", Toast.LENGTH_SHORT).show();
                    } else if (log_in_pas.length() < 6) {
                        progressDialog.dismiss();
                        log_in_pas.setError("INVALID EMAIL");
                        Toast.makeText(Log_in.this, "Password length must be greater than 6", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(Log_in.this, home_activity.class));
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Log_in.this, "LOG IN ERROR!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
            sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Log_in.this, Registration.class);
                    startActivity(intent);
                }
            });
        }
    }
}