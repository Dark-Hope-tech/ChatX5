package com.example.chatx5;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatx5.Activity.Chats_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class random_adapter extends RecyclerView.Adapter<random_adapter.Viewholdler> {
    Context context;
    ArrayList<user> userArrayList;
    String user_online,default_pic="https://firebasestorage.googleapis.com/v0/b/chatx5-429c5.appspot.com/o/f2ddf7d7a6503c6be7044a17d88efcb995ddd2287a206693caf7c9635d159c52_1%20(1).jpg?alt=media&token=fc6ecf25-baae-43e8-acaa-9fbc5f80db83";

    public random_adapter(Context context, ArrayList<user> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public Viewholdler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rand_user_row,parent, false);
        return new random_adapter.Viewholdler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholdler holder, int position) {
        user use=userArrayList.get(position);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("presence");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_online = snapshot.child(use.getUid()).getValue().toString();
                if(user_online.equals("Online") && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals(use.getUid())){
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent =new Intent(context,Chats_activity.class);
                            intent.putExtra("name","Random user");
                            intent.putExtra("ReceiverImage",default_pic);
                            intent.putExtra("uid",use.getUid());
                            context.startActivity(intent);
                        }
                    });
                }
                else{
                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                    params.height = 0;
                    holder.itemView.setLayoutParams(params);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    class Viewholdler extends RecyclerView.ViewHolder {
        CircleImageView rand_image;
        TextView rand_text;
        public Viewholdler(@NonNull View itemView) {
            super(itemView);
            rand_image=itemView.findViewById(R.id.rand_image);
            rand_text=itemView.findViewById(R.id.rand_name);
        }
    }
}
