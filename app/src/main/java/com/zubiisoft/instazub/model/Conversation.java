package com.zubiisoft.instazub.model;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Conversation {
    private String idRoom;
    private String friendUid;
    private String avatar;
    private String name;
    private String lastMessage;

    public Conversation() {};

    public Conversation(String idRoom, String friendUid, String avatar, String name,
                        String lastMessage) {
        this.idRoom = idRoom;
        this.friendUid = friendUid;
        this.avatar = avatar;
        this.name = name;
        this.lastMessage = lastMessage;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setRoomId(String idRoom) {
        this.idRoom = idRoom;
    }

    public String getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(String friendUid) {
        this.friendUid = friendUid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @NonNull
    @Override
    public String toString() {
        return "Conversation{" +
                "roomId='" + idRoom + '\'' +
                ", friendUid='" + friendUid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                '}';
    }
}
