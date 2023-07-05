package com.example.whatsappclone.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Models.MessagesModel;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{


    ArrayList<MessagesModel> messagesModels;
    Context context;
    String recId;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context, String recId) {
        this.messagesModels = messagesModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent,false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messagesModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        } else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagesModel messagesModel = messagesModels.get(position);

        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMessage.setText(messagesModel.getMessage());
        } else{
            ((ReceiverViewHolder)holder).receiverMessage.setText(messagesModel.getMessage());
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("Chats")
                                        .child(senderRoom)
                                        .child(messagesModel.getMessageId())
                                        .setValue(null);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }




    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMessage, receiverTime;


        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMessage = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMessage, senderTime;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessage = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}
