package com.example.photoapp;

import java.io.Serializable;

public class Post implements Serializable {
    public byte[] imageData; //画像のデータ


    public Post(byte[] ImageData) {
        this.imageData = ImageData;
    }
}