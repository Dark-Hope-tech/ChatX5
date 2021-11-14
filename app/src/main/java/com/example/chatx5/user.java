package com.example.chatx5;

import android.widget.ImageView;

import java.util.ArrayList;

public class user {
    String uid;
    String name;
    String mail;
    String ImageURI;
    String status;
    String key;
    public user() {

    }
    public user(String uid, String name, String mail, String imageURI,String status) {
        this.uid = uid;
        this.name = name;
        this.mail = mail;
        this.status=status;
        this.ImageURI = imageURI;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setKey(String key) { this.key = key; }
    public String getKey() { return key; }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImageURI() {
        return ImageURI;
    }

    public void setImageURI(String imageURI) {
        ImageURI = imageURI;
    }
}
