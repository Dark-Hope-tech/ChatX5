package com.example.chatx5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class friend_req_adapter extends RecyclerView.Adapter<friend_req_adapter.Viewholdler> {
    Context context;
    ArrayList<String> pending_req;
    user use;
    public friend_req_adapter(Context context, ArrayList<String> pending_req) {
        this.context = context;
        this.pending_req = pending_req;
    }

    public Context getContext() {
        return context;
    }


    @NonNull
    @Override
    public Viewholdler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.friend_req_layout,parent, false);
        return new friend_req_adapter.Viewholdler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholdler holder, int position) {
        String us=pending_req.get(position);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("user").child(us);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                use =snapshot.getValue(user.class);
                holder.name.setText(use.name);
                holder.email.setText(use.mail);
                Glide.with(context).load(use.ImageURI).into(holder.dp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.btn_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("friends").child("AcceptedFriends").child(use.getUid()).child(FirebaseAuth.getInstance().getUid());
                reference1.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("friends").child("AcceptedFriends").child(FirebaseAuth.getInstance().getUid()).child(use.getUid());
                            reference.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("friends").child("friend_req").child(FirebaseAuth.getInstance().getUid()).child(use.getUid());
                                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(context, "Friend Successfully added", Toast.LENGTH_SHORT).show();
                                                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                                    params.height = 0;
                                                    holder.itemView.setLayoutParams(params);
                                                }else{
                                                    Toast.makeText(context, "remove value unsuccessful", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(context, "Friend Adding in other unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(context, "Error in sending req", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.btn_rej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("friends").child("friend_req").child(FirebaseAuth.getInstance().getUid()).child(use.getUid());
                ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(context, "Friend Rejected Successfully", Toast.LENGTH_SHORT).show();
                            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                            params.height = 0;
                            holder.itemView.setLayoutParams(params);
                        }else{
                            Toast.makeText(context, "remove value unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return pending_req.size();
    }

    public class Viewholdler extends RecyclerView.ViewHolder {
        CircleImageView dp;
        TextView name,email,btn_ac,btn_rej;
        public Viewholdler(@NonNull View itemView) {
            super(itemView);
            this.dp=itemView.findViewById(R.id.frnd_dp);
            this.name=itemView.findViewById(R.id.frnd_name);
            this.email=itemView.findViewById(R.id.frnd_mail);
            btn_ac=itemView.findViewById(R.id.btn_ac);
            btn_rej=itemView.findViewById(R.id.btn_rej);
        }
    }
}
