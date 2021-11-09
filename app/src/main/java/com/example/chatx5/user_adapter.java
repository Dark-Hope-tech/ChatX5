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
import com.example.chatx5.Activity.home_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_adapter extends RecyclerView.Adapter<user_adapter.Viewholdler> {
    Context home_activit;
    ArrayList<user> userArrayList;

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
        Picasso.get().load(use.ImageURI).into(holder.userprofile);
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
    }
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    class Viewholdler extends RecyclerView.ViewHolder {
        CircleImageView userprofile;
        TextView user_name;
        TextView user_status;
        public Viewholdler(@NonNull View itemView) {
            super(itemView);
            userprofile=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            user_status= itemView.findViewById(R.id.user_status);
        }
    }
}
