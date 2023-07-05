package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.example.whatsappclone.Adapter.ChatAdapter;
import com.example.whatsappclone.Models.MessagesModel;
import com.example.whatsappclone.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();

        final String senderID = FirebaseAuth.getInstance().getUid();
        binding.chatUserName.setText("Friends Group");

        final ChatAdapter adapter = new ChatAdapter(messagesModels, this);
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);


        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessagesModel model = snapshot1.getValue(MessagesModel.class);
                    messagesModels.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.etmessage.getText().toString();
                final MessagesModel model = new MessagesModel(senderID, message);
                model.setTimestamp(new Date().getTime());
                binding.etmessage.setText("");

                database.getReference()
                        .child("Group Chat").push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });
    }
}