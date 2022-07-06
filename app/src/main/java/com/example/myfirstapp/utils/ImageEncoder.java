package com.example.myfirstapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.example.myfirstapp.R;

import java.util.Base64;

public class ImageEncoder {
    private BitmapToByteArrayHelper bitmapToByteArrayHelper;
    private Context context;

    public ImageEncoder(BitmapToByteArrayHelper bitmapToByteArrayHelper, Context context) {
        this.bitmapToByteArrayHelper = bitmapToByteArrayHelper;
        this.context = context;
    }

    public String bitmapToBase64String(Bitmap bitmap) {
        byte[] arr = bitmapToByteArrayHelper.getByteArrayFromBitmap(bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String(Base64.getEncoder().encode(arr));
        }
        return "error";
    }

    public Bitmap base64StringToBitmap(String base64String) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            byte[] decoded = Base64.getDecoder().decode(base64String.getBytes());
            return bitmapToByteArrayHelper.getBitmapFromByteArray(decoded);
        }
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_close);
    }
}
