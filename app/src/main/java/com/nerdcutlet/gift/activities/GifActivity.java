package com.nerdcutlet.gift.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.other.adapters.VideoAsyncTask;
import com.nerdcutlet.gift.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.OnClick;

public class GifActivity extends Activity {

    public final static String LOG_TAG = "GifActivity";
    Utils utils = new Utils();
    DisplayMetrics display;
    int mwidth, mheight;

    String data;



    VideoAsyncTask task = new VideoAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        ImageView gifImage = (ImageView) findViewById(R.id.gif_image);

        Intent i = getIntent();
        i.getStringExtra("getRating");
        i.getStringExtra("getImportDatetime");
        i.getStringExtra("getTrendingDatetime");
        data = i.getStringExtra("getMp4");
        i.getStringExtra("getMp4Size");
        i.getStringExtra("getWebp");
        i.getStringExtra("getWebpSize");
        String url = i.getStringExtra("getStillUrl");
        i.getStringExtra("getWidth");
        i.getStringExtra("getHeight");

        Log.d(LOG_TAG, i.getStringExtra("getRating") + " " +
                i.getStringExtra("getImportDatetime") + " " +
                i.getStringExtra("getTrendingDatetime") + " " +
                i.getStringExtra("getMp4") + " " +
                i.getStringExtra("getMp4Size") + " " +
                i.getStringExtra("getWebp") + " " +
                i.getStringExtra("getWebpSize") + " " +
                i.getStringExtra("getStillUrl") + " " +
                i.getStringExtra("getWidth") + " " +
                i.getStringExtra("getHeight") + " ");


        Picasso.with(getApplicationContext())
                .load(url)
                .placeholder(R.color.colorAccent)
                .into(gifImage);


        View gifblankview = (View) findViewById(R.id.gif_blank_view);
        NestedScrollView gifscrollview = (NestedScrollView) findViewById(R.id.gif_scrollview);
        ImageView imgview = (ImageView) findViewById(R.id.gif_image);


        display = getApplicationContext().getResources().getDisplayMetrics();
        mwidth = display.widthPixels;
        mheight = display.heightPixels;
        Log.d(LOG_TAG, "w : " + mwidth + " h : " + mheight);


        ViewGroup.LayoutParams params2 = gifblankview.getLayoutParams();
        int xhgth2 = Math.round(mheight / 3 - 50);
        Log.d(LOG_TAG, "blank view : " + xhgth2);
        params2.height = (xhgth2);
        gifblankview.requestLayout();


        ViewGroup.LayoutParams params3 = gifscrollview.getLayoutParams();
        int xhgth = Math.round(mheight * 2 / 3 - 200);
        Log.d(LOG_TAG, "scroll view : " + xhgth);

        params3.height = xhgth;
        gifscrollview.requestLayout();


        Button testb = (Button)findViewById(R.id.test_btn);
        testb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "HI",Toast.LENGTH_LONG).show();
                task.setData(getApplicationContext(), data);
                task.execute();
            }
        });

    }
}

