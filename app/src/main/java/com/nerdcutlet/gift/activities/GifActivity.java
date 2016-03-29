package com.nerdcutlet.gift.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.models.FavouriteGif;
import com.nerdcutlet.gift.other.adapters.VideoAsyncTask;
import com.nerdcutlet.gift.utils.Utils;
import com.nerdcutlet.gift.utils.VideoDownloadResponse;
import com.squareup.picasso.Picasso;

import static com.orm.SugarRecord.save;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

@DeepLink({"https://giphy.com/gifs/{giphyType1}", "https://giphy.com/gifs/{giphyType2}/html5",
        "https://gfycat.com/{gfycatType1}", "https://zippy.gfycat.com/{gfycatType2}", "https://giant.gfycat.com/{gfycatType3}"})
public class GifActivity extends Activity implements VideoDownloadResponse {

    public final static String LOG_TAG = "GifActivity";
    Utils utils = new Utils();
    DisplayMetrics display;
    int mwidth, mheight;

    String data, url;
    String gifID;

    String localurl;

    ImageView gifImage;
    VideoView videoView;


    VideoAsyncTask task = new VideoAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);


        gifImage = (ImageView) findViewById(R.id.gif_image);
        videoView = (VideoView) findViewById(R.id.videoView1);

        View gifblankview = (View) findViewById(R.id.gif_blank_view);
        NestedScrollView gifscrollview = (NestedScrollView) findViewById(R.id.gif_scrollview);


        //DeepLink Stuff
        if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = getIntent().getExtras();

            String giphyType1 = parameters.getString("giphyType1");     //https://giphy.com/gifs/{giphyType1}
            Log.d(LOG_TAG, "giphyType1 : " + giphyType1);

            String giphyType2 = parameters.getString("giphyType2");     //https://giphy.com/gifs/{giphyType2}/html5
            Log.d(LOG_TAG, "giphyType2 : " + giphyType2);

            String gfycatType1 = parameters.getString("gfycatType1");   //https://gfycat.com/{gfycatType1}
            Log.d(LOG_TAG, "gfycatType1 : " + gfycatType1);

            String gfycatType2 = parameters.getString("gfycatType2");   //https://zippy.gfycat.com/{gfycatType2}
            Log.d(LOG_TAG, "gfycatType2 : " + gfycatType2);

            String gfycatType3 = parameters.getString("gfycatType3");   //https://giant.gfycat.com/{gfycatType3}
            Log.d(LOG_TAG, "gfycatType3 : " + gfycatType3);


            buildGifUrl(giphyType1, giphyType2, gfycatType1, gfycatType2, gfycatType3);

        }

        //Run this only when Internal Activity Passes Data.
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras.containsKey("getId")) {
            i.getStringExtra("getRating");
            i.getStringExtra("getImportDatetime");
            i.getStringExtra("getTrendingDatetime");
            data = i.getStringExtra("getMp4");
            i.getStringExtra("getMp4Size");
            i.getStringExtra("getWebp");
            i.getStringExtra("getWebpSize");
            url = i.getStringExtra("getStillUrl");
            i.getStringExtra("getWidth");
            i.getStringExtra("getHeight");
            gifID = i.getStringExtra("getId");


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

            doStuff(data);
        }


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


        Button testb = (Button) findViewById(R.id.test_btn);
        testb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "This will Save Video Offline", Toast.LENGTH_LONG).show();


            }
        });


        Button fav_btn = (Button) findViewById(R.id.fav_btn);
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "GIF ID before save : " + gifID);


                FavouriteGif favouriteGif = new FavouriteGif(gifID.toString());
                favouriteGif.save();

            }
        });
    }

    void buildGifUrl(String giphyType1, String giphyType2, String gfycatType1, String gfycatType2, String gfycatType3) {
        String builtURL = null;

        if (giphyType1 != null) {
            builtURL = "https://media2.giphy.com/media/" + giphyType1 + "/200.mp4";
        } else if (giphyType2 != null) {
            builtURL = "https://media2.giphy.com/media/" + giphyType2 + "/200.mp4";
        } else if (gfycatType1 != null) {
            builtURL = "https://zippy.gfycat.com/" + gfycatType1 + ".mp4";

        } else if (gfycatType2 != null) {
            builtURL = "https://zippy.gfycat.com/" + gfycatType2;

        } else if (gfycatType3 != null) {
            builtURL = "https://giant.gfycat.com/" + gfycatType3;

        }

        Log.d(LOG_TAG, "builtURL : " + builtURL);
        doStuff(builtURL);
    }


    void doStuff(String data) {
        task.setData(getApplicationContext(), data);
        task.videoDownloadResponse = this;
        task.execute();
    }

    @Override
    public void localVideoUrl(String videoUrl) {
        localurl = videoUrl;
        Log.d(LOG_TAG, localurl);
        gifImage.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);

        //Creating MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        //specify the location of media file
        Uri uri = Uri.parse(localurl);

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}

