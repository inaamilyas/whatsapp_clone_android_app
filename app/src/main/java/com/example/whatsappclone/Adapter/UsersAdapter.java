package com.example.whatsappclone.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.ChatDetailActivity;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder1> {

    ArrayList<Users> list;
    Context context;

    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false);

        return new ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder1 holder1, int position) {

        Users users = list.get(position);
        holder1.userName.setText(users.getUsername()); //Displaying user name from database
        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.user).into(holder1.profileImage);


      holder1.itemView.setBackgroundColor(Color.parseColor("#adb6c4"));
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", users.getUserID());
                intent.putExtra("userProfilePic", users.getProfilePic());
                intent.putExtra("userName", users.getUsername());
                context.startActivity(intent);
            }
        });


        //Getting last message from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String senderRoom = auth.getUid() + users.getUserID();

        database.getReference().child("Chats").child(senderRoom).orderByChild("Timestamp")
                .limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                holder1.lastMessage.setText(datasnapshot.child("message")
                                        .getValue(String.class));
                            }
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder1 extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView userName, lastMessage;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.userNameList);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }
}
