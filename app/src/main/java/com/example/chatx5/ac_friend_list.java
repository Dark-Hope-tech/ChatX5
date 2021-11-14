package com.example.chatx5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ac_friend_list extends RecyclerView.Adapter<ac_friend_list.Viewholdler> {

    Context context;
    ArrayList<user> arrayList;

    public ac_friend_list(Context context, ArrayList<user> arrayList, user us) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Viewholdler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ac_friend_row,parent, false);
        return new ac_friend_list.Viewholdler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholdler holder, int position) {
        String us=arrayList.get(position).toString();
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("friends").child("AcceptedFriends").child(FirebaseAuth.getInstance().getUid()).child(us);
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Friend Successfully Deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Friend Not Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() { return arrayList.size(); }

    public class Viewholdler extends RecyclerView.ViewHolder {
        CircleImageView dp;
        TextView name,status;
        ImageView del;
        public Viewholdler(@NonNull View itemView) {
            super(itemView);
            dp=itemView.findViewById(R.id.ac_user_image);
            name=itemView.findViewById(R.id.ac_user_name);
            status=itemView.findViewById(R.id.ac_user_status);
            del=itemView.findViewById(R.id.ac_del_img);
        }
    }
}
