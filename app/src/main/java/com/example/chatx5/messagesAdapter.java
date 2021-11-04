package com.example.chatx5;



import static com.example.chatx5.Chats_activity.rImage;
import static com.example.chatx5.Chats_activity.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int Item_send=1;
    int Item_received=2;

    public messagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==Item_send){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.reciever_layout_item,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages=messagesArrayList.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder= (SenderViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
//            Picasso.get().load(sImage).into(viewHolder.ProfileImageS);
        }
        else{
            ReceiverViewHolder viewHolder= (ReceiverViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
//            Picasso.get().load(rImage).into(viewHolder.ProfileImageR);
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderID())){
            return Item_send;
        }
        else {
            return Item_received;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ProfileImageS;
        TextView txtmessage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
//            ProfileImageS=itemView.findViewById(R.id.profile_imageS);
            txtmessage=itemView.findViewById(R.id.txtmessageS);
        }
    }
    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ProfileImageR;
        TextView txtmessage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
//            ProfileImageR=itemView.findViewById(R.id.profile_imageR);
            txtmessage=itemView.findViewById(R.id.txtmessageR);
        }
    }
}