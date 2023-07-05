package com.example.whatsappclone.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whatsappclone.Adapter.UsersAdapter;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;
//    Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
//        context = getContext();
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);


        //inam
//        list.add(new Users("inam", "inam@inam.com", "12345"));
//        list.add(new Users("test", "inam@inam.com", "12345"));
//        list.add(new Users("test1", "inam@inam.com", "12345"));
//        list.add(new Users("test2", "inam@inam.com", "12345"));

        UsersAdapter adapter = new UsersAdapter(list, getContext());
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);

                    //skip logged in user to be shown in the main activty
                    if (dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) {
//                        Toast.makeText(getContext(), "Skipping user", Toast.LENGTH_SHORT).show();
                        continue;
                    }

                    users.setUserID(dataSnapshot.getKey());
                    list.add(users);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return binding.getRoot();
    }
}