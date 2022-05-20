package com.example.myfirstapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapToByteArrayHelper {

    public BitmapToByteArrayHelper() {

    }

    public byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public Bitmap getBitmapFromByteArray(byte[] bitmapByteArray) {
        return BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.length);
    }

}
