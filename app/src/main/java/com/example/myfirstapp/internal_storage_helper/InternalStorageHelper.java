package com.example.myfirstapp.internal_storage_helper;

import android.content.Context;
import android.graphics.Bitmap;

public interface InternalStorageHelper {

    /**
     * save a bitmap to internal storage
     * @param bitmapImage the bitmap
     * @param id used to create the name of the image to be saved
     * @param context
     * @return the path to the image
     */
    public String saveToInternalStorage(Bitmap bitmapImage, int id, Context context);
}
