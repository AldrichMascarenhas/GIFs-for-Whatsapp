package com.nerdcutlet.gift.activities;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;

public class GifActivity extends Activity{

    public final static String LOG_TAG = "GifActivity";
    Utils utils = new Utils();
    DisplayMetrics display;
    int mwidth, mheight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        View gifblankview = (View)findViewById(R.id.gif_blank_view);
        NestedScrollView gifscrollview = (NestedScrollView)findViewById(R.id.gif_scrollview);
        ImageView imgview = (ImageView)findViewById(R.id.gif_image);



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
        int xhgth = Math.round(mheight * 2/3 - 200);
        Log.d(LOG_TAG, "scroll view : " + xhgth);

        params3.height = xhgth;
        gifscrollview.requestLayout();


        //gifblankview.setLayoutParams(new AppBarLayout.LayoutParams(Math.round(mheight / 3), mwidth));

        //gifscrollview.setLayoutParams(new CoordinatorLayout.LayoutParams(Math.round(mheight * 2 / 3 - dp), mwidth));

    }
}

