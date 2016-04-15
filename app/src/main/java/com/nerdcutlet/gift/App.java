package com.nerdcutlet.gift;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.orm.SugarContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by Aldrich on 11-04-2016.
 */
public class App extends Application {

    public final static String LOG_TAG = "Connection Class";

    private ConnectionClassManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;

    private String mURL = "https://raw.githubusercontent.com/AldrichMascarenhas/GIFs-for-Whatsapp/fe31ebf538e0affdec5723da54050f527d1d9389/NetworkConnection/test_img.jpg";
    private int mTries = 0;
    public ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;

    public ConnectionQuality getmConnectionClass() {
        return mConnectionClass;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SugarContext.init(this);

        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mListener = new ConnectionChangedListener();
        mConnectionClassManager.register(mListener);

        //TODO: Reduce image size.
        //startCheck();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    public void startCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.d(LOG_TAG, "No internet");
            return;
        }
        new DownloadImage().execute(mURL);


    }

    /**
     * Listener to update the UI upon connectionclass change.
     */
    private class ConnectionChangedListener
            implements ConnectionClassManager.ConnectionClassStateChangeListener {

        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
            Log.d("network", "mConnectionClass : " + mConnectionClass.toString());

            Log.d("network", "ConnectionQuality : " + ConnectionClassManager
                    .getInstance().getCurrentBandwidthQuality());

            Log.d("network", "downloadKBitsPerSecond : " + ConnectionClassManager
                    .getInstance().getDownloadKBitsPerSecond());
        }

    }

    private class DownloadImage extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDeviceBandwidthSampler.startSampling();
            Log.d(LOG_TAG, "Profiling Started");

        }


        @Override
        protected Void doInBackground(String... url) {
            String imageURL = url[0];
            try {
                // Open a stream to download the image from our URL.
                URLConnection connection = new URL(imageURL).openConnection();
                connection.setUseCaches(false);
                connection.connect();
                InputStream input = connection.getInputStream();
                try {
                    byte[] buffer = new byte[1024];

                    // Do some busy waiting while the stream is open.
                    while (input.read(buffer) != -1) {
                    }
                } finally {
                    input.close();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while downloading image.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mDeviceBandwidthSampler.stopSampling();
            Log.d(LOG_TAG, "Profiling Ended");

            // Retry for up to 10 times until we find a ConnectionClass.
            if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 5) {
                mTries++;
                new DownloadImage().execute(mURL);
            }

        }
    }


}
