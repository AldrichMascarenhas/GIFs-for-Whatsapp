package com.nerdcutlet.gift.other.adapters;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.nerdcutlet.gift.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Aldrich on 27-03-2016.
 */
public class VideoAsyncTask extends AsyncTask<Void, Void, Void> {

    public final static String LOG_TAG = "VideoAsyncTask";

    String VideoUrl;
    Context context;

    Utils utils = new Utils();


    File myExternalFile;

    String filepath = "MyFileStorage";
    String filename = "myfile";
    String string = "Hello world!";




    public void setData(Context context, String url) {
        this.VideoUrl = url;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {




        //check if external storage is available and not read only
        if (!utils.isExternalStorageAvailable() || utils.isExternalStorageReadOnly()) {
            Log.d(LOG_TAG, "No Ext");
        } else {
            myExternalFile = new File(context.getExternalFilesDir(filepath), filename);
        }
        try {

            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(string.getBytes());
            fos.close();
            Log.d(LOG_TAG, "Done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
