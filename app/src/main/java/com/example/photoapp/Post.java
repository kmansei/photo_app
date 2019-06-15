package com.example.photoapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    public Bitmap bmp; //画像のビットデータ

    public Post(Bitmap bmp) {
        this.bmp = bmp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBitmap()
    }
}
