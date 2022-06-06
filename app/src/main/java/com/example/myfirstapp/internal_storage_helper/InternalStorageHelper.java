package com.example.myfirstapp.internal_storage_helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public interface InternalStorageHelper {

    /**
     * save a bitmap to internal storage
     * @param bitmapImage the bitmap
     * @param id used to create the name of the image to be saved
     * @param context
     * @return the path to the image
     */
    String saveToInternalStorage(Bitmap bitmapImage, int id, Context context);

    /**
     * load the bitmap into the imageView
     * @param path
     * @param imgName
     * @param imageView
     * @param context
     */
    void loadImageFromStorage(String path, String imgName, ImageView imageView, Context context);
}
