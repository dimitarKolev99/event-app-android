package com.example.myfirstapp.internal_storage_helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.myfirstapp.AddEditEventActivity;
import com.example.myfirstapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalStorageHelperImpl implements InternalStorageHelper{
    private InternalStorageHelperImpl internalStorageHelper;

    public InternalStorageHelperImpl() { }

    @Override
    public String saveToInternalStorage(Bitmap bitmapImage, int id, Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        String name = "img" + String.valueOf(id) + ".jpg";
        File myPath = new File(directory, name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public void loadImageFromStorage(String path, String imgName, ImageView imageView, Context context)
    {
        File f = new File(path, imgName);
        Picasso.with(context).load(f).fit().centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_close)
                .into(imageView);
    }
}
