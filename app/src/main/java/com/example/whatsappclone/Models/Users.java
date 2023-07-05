package com.example.whatsappclone.Models;

public class Users {
    private String profilePic, username, email, password, userID, lastMessage, about;



    public Users(String profilePic, String username, String email, String password, String userID, String lastMessage, String about) {
        this.profilePic = profilePic;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userID = userID;
        this.lastMessage = lastMessage;
        this.about = about;
    }

    public Users() {
        //empty constructor
    }

    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }
//    public String getUserID(String key) {
//        return userID;
//    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}