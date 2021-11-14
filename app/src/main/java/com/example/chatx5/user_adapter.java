package com.example.chatx5;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatx5.Activity.Chats_activity;
import com.example.chatx5.Activity.home_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_adapter extends RecyclerView.Adapter<user_adapter.Viewholdler> {
    Context home_activit;
    ArrayList<user> userArrayList=new ArrayList<>();

    public user_adapter(Context ctx) { this.home_activit = ctx; }
    public void setItems(ArrayList<user> emp) { userArrayList.addAll(emp); }

    public user_adapter(home_activity home_activity, ArrayList<user> userArrayList) {
        this.home_activit=home_activity;
        this.userArrayList=userArrayList;
    }

    @NonNull
    @Override
    public Viewholdler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(home_activit).inflate(R.layout.item_user_row,parent, false);
        return new Viewholdler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholdler holder, int position) {
        user use=userArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(use.getUid())){
//            holder.itemView.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0;
            holder.itemView.setLayoutParams(params);
        }
        holder.user_name.setText(use.name);
        holder.user_status.setText(use.status);
//        Picasso.get().load(use.ImageURI).into(holder.userprofile);
        Glide.with(home_activit).load(use.ImageURI).into(holder.userprofile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(home_activit, Chats_activity.class);
                intent.putExtra("name",use.getName());
                intent.putExtra("ReceiverImage",use.getImageURI());
                intent.putExtra("uid",use.getUid());
                home_activit.startActivity(intent);
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("friends").child("AcceptedFriends").child(FirebaseAuth.getInstance().getUid()).child(use.getUid());
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(home_activit, "Friend Successfully Deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(home_activit, "Friend Not Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    class Viewholdler extends RecyclerView.ViewHolder {
        CircleImageView userprofile;
        TextView user_name,user_status;
        ImageView del;
        public Viewholdler(@NonNull View itemView) {
            super(itemView);
            userprofile=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            user_status= itemView.findViewById(R.id.user_status);
            del=itemView.findViewById(R.id.del_img);
        }
    }
}
