package com.nerdcutlet.gift.other.adapters;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.utils.Utils;
import com.nerdcutlet.gift.utils.VideoDownloadResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Aldrich on 27-03-2016.
 */
public class VideoAsyncTask extends AsyncTask<Void, Void, Void> {

    public final static String LOG_TAG = "VideoAsyncTask";

    public VideoDownloadResponse videoDownloadResponse = null;
    String VideoUrl;
    Context context;

    Utils utils = new Utils();


    File myExternalFile;


    String filename = "myfile.mp4";
    String string = "Hello world!";


    public void setData(Context context, String url) {
        this.VideoUrl = url;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(VideoUrl)
                    .build();
            Response response = client.newCall(request).execute();
            Log.d(LOG_TAG, "Response : " + response.toString());
            Log.d(LOG_TAG, "Response : " + response.body().toString());

            //check if external storage is available and not read only
            if (!utils.isExternalStorageAvailable() || utils.isExternalStorageReadOnly()) {
                Log.d(LOG_TAG, "No Ext");
            } else {
                myExternalFile = new File(context.getExternalFilesDir(context.getString(R.string.storage_filepath)), filename);
            }
            try {

                InputStream is = response.body().byteStream();
                BufferedInputStream input = new BufferedInputStream(is);
                OutputStream output = new FileOutputStream(myExternalFile);

                byte[] data = new byte[1024];

                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();


                Log.d(LOG_TAG, "Done!");
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {

        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        videoDownloadResponse.localVideoUrl(myExternalFile.getAbsolutePath());

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
