package com.zubiisoft.instazub.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class Room {
    private String idRoom;
    private ArrayList<String> participants;
    private ArrayList<Map<String, String>> messages;
    private Map<String, String> lastMessage;

    public Room() {}
    public Room(String idRoom, ArrayList<String> participants, ArrayList<Map<String, String>> messages, Map<String, String> lastMessage) {
        this.idRoom = idRoom;
        this.participants = participants;
        this.messages = messages;
        this.lastMessage = lastMessage;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public ArrayList<Map<String, String>> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Map<String, String>> messages) {
        this.messages = messages;
    }

    public Map<String, String> getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Map<String, String> lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "Room{" +
                "idRoom='" + idRoom + '\'' +
                ", participants=" + participants +
                ", messages=" + messages +
                ", lastMessage=" + lastMessage +
                '}';
    }
}

