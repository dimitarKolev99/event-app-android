package com.example.myfirstapp.async_tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.myfirstapp.internal_storage_helper.InternalStorageHelper;
import com.example.myfirstapp.internal_storage_helper.InternalStorageHelperImpl;

public class LoadImgAsyncTask extends AsyncTask<Void, Void, String> {
    private Bitmap bitmap;
    private int rand_id;
    private Context context;
    private InternalStorageHelper internalStorageHelper;

    public LoadImgAsyncTask(Bitmap bitmap, int rand_id, Context context, InternalStorageHelperImpl internalStorageHelper) {
        this.bitmap = bitmap;
        this.rand_id = rand_id;
        this.context = context;
        this.internalStorageHelper = internalStorageHelper;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return internalStorageHelper.saveToInternalStorage(bitmap, rand_id, context);
    }

    @Override
    protected void onPostExecute(String path) {
        super.onPostExecute(path);

        if (path != null) {
//                eventController.onAddButtonClicked(new Event(title, path, String.valueOf(id)));
        }
    }
}
