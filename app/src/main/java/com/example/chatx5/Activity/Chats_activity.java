package com.example.chatx5.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatx5.ModelClass.Messages;
import com.example.chatx5.R;
import com.example.chatx5.messagesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chats_activity extends AppCompatActivity {
    public String ReceiverID,SenderID,ReceiverImage,ReceiverName,SenderRoom,ReceiverRoom;
    CircleImageView profileImage;
    TextView receiverName;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    public static String rImage;
    CardView send_btn;
    EditText editText;
    RecyclerView MessageAdapter;
    ArrayList<Messages> arrayList;
    messagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        ReceiverName=getIntent().getStringExtra("name");
        ReceiverID=getIntent().getStringExtra("uid");
        ReceiverImage=getIntent().getStringExtra("ReceiverImage");

        rImage=ReceiverImage;

        arrayList=new ArrayList<>();

        profileImage=findViewById(R.id.profile_image);
        receiverName=findViewById(R.id.Receiver_name);
        send_btn=findViewById(R.id.send_btn);
        editText=findViewById(R.id.edit_text_msg);
        SenderID=firebaseAuth.getUid();
        MessageAdapter=findViewById(R.id.message_adapter);

        Picasso.get().load(ReceiverImage).into(profileImage);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        MessageAdapter.setLayoutManager(linearLayoutManager);
        adapter=new messagesAdapter(Chats_activity.this, arrayList);
        MessageAdapter.setAdapter(adapter);

        SenderRoom=SenderID+ReceiverID;
        ReceiverRoom=ReceiverID+SenderID;

        receiverName.setText(""+ReceiverName);
        DatabaseReference reference=firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference=firebaseDatabase.getReference().child("chats").child(SenderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Messages messages=dataSnapshot.getValue(Messages.class);
                    arrayList.add(messages);
                }
//                MessageAdapter.smoothScrollToPosition(MessageAdapter.getAdapter().getItemCount()-1);
                MessageAdapter.scrollToPosition(arrayList.size()-1);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=editText.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(Chats_activity.this, "Type Something..", Toast.LENGTH_SHORT).show();
                    return;
                }
                editText.setText("");
                Date date= new Date();
                Messages messages=new Messages(message,firebaseAuth.getUid(),date.getTime());
                firebaseDatabase.getReference().child("chats")
                        .child(SenderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseDatabase.getReference().child("chats")
                                .child(ReceiverRoom)
                                .child("messages")
                                .push()
                                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });
    }
}
