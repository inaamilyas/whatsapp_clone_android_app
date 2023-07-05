package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Adapter.ChatAdapter;
import com.example.whatsappclone.Models.MessagesModel;
import com.example.whatsappclone.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

       final String senderID = auth.getUid();
        String receiverID = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("userProfilePic");

        binding.chatUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.profileImagechat);


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messagesModels, this, receiverID);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderID + receiverID;
        final String receiverRoom = receiverID + senderID;

        
        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren() ) {
                   MessagesModel model = snapshot1.getValue(MessagesModel.class);

                   model.setMessageId(snapshot1.getKey());

                   messagesModels.add(model);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.etmessage.getText().toString();
                final MessagesModel model = new MessagesModel(senderID, message);
                model.getTimestamp(new Date().getTime());

                binding.etmessage.setText("");


                database.getReference().child("Chats").child(senderRoom).push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("Chats").child(receiverRoom).push()
                                        .setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                            }
                                        });
                            }
                        });
            }
        });
    }
}