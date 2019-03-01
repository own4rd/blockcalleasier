package br.com.ownard.models;

import android.graphics.Bitmap;


public class Contact {
    private String name;
    private String phoneNumber;
    private long blockCalls;
    private Bitmap photo;


    public Contact(String name, String phoneNumber, long blockCalls) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.blockCalls = blockCalls;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getBlockCalls() {
        return blockCalls;
    }



}
