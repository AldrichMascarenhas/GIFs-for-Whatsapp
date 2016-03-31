package com.nerdcutlet.gift.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Aldrich on 25-03-2016.
 */
public class Utils {


    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public  boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public  boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }



    public  String  getDateTime(Calendar cal){
        /*
        *FORMAT :   'YYYY-mm-dd hh:mm:ss'
        *            24 hour format
        *           '2015-08-11 13:13:00' (time for 1:13pm exactly).
        *
        */
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);


        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);

        String date = year + "-" +month + "-" + dayofmonth + " " + hourofday + ":" + minute + ":" + second;

            Log.d("Utils", date);
            Log.d("Utils",  hourofday + " " + minute + " " + second);
            Log.d("Utils",  year + " " + month + " " + dayofmonth);
        return  date;
    }

}
