package com.example.whatsappclone.Models;

public class MessagesModel {
    String uid,message,messageId;
    long timestamp;

    public MessagesModel() {
    }

    public MessagesModel(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }

    public MessagesModel(String uid, String message, long timestamp) {
        this.uid = uid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp(long time) {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
