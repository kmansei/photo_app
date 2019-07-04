package com.example.photoapp;

import java.io.Serializable;

public class Post implements Serializable {
    public byte[] imageData; //画像のデータ
    public String user_name;


    public Post(byte[] ImageData, String name) {
        this.imageData = ImageData;
        this.user_name = name;
    }
}