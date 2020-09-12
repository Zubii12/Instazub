package com.zubiisoft.instazub.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class User {

    private String uid;
    private String firstName;
    private String lastName;
    private String avatar;
    private String createdAt;
    private String email;
    private String phone;
    private ArrayList<String> friends;
    private ArrayList<Conversation> conversations;

    public User() {}

    public User(String uid, String firstName, String lastName, String avatar, String createdAt,
                String email, String phone, ArrayList<String> friends,
                ArrayList<Conversation> conversations) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.email = email;
        this.phone = phone;
        this.friends = friends;
        this.conversations = conversations;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", friends=" + friends +
                ", conversations=" + conversations +
                '}';
    }
}
