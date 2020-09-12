package com.zubiisoft.instazub.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Post {

    private String uid;
    private String avatar;
    private String name;
    private String time;
    private ArrayList<String> contents;

    public Post() {}
    public Post(String uid, String avatar, String name, String time, ArrayList<String> contents) {
        this.uid = uid;
        this.avatar = avatar;
        this.name = name;
        this.time = time;
        this.contents = contents;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    @NotNull
    @Override
    public String toString() {
        return "Post{" +
                "uid='" + uid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", contents=" + contents +
                '}';
    }
}
