package com.nerdcutlet.gift.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.fragments.CategoryFilterFragment;
import com.nerdcutlet.gift.fragments.FilterFragment;
import com.nerdcutlet.gift.models.FavouriteGif;
import com.nerdcutlet.gift.other.VideoAsyncTask;
import com.nerdcutlet.gift.utils.Utils;
import com.nerdcutlet.gift.utils.VideoDownloadResponse;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

@DeepLink({"https://giphy.com/gifs/{giphyType1}", "https://giphy.com/gifs/{giphyType2}/html5",
        "https://gfycat.com/{gfycatType1}", "https://zippy.gfycat.com/{gfycatType2}", "https://giant.gfycat.com/{gfycatType3}"})
public class GifActivity extends Activity implements VideoDownloadResponse, CategoryFilterFragment.OnFilterSelectedListener {

    public final static String LOG_TAG = "GifActivity";
    Utils utils = new Utils();
    DisplayMetrics display;
    int mwidth, mheight;



    String gifRating, gifImportDatetime, gifTrendingDatetime, gifMp4, gifWebp, gifStillUrl;
    String gifMp4Size, gifWebpSize, gifWidth, gifHeight, gifId;
    String typeOfData;
    String searchData;


    Calendar calendar;
    String currentDate;

    String localurl;

    ImageView gifImage;
    VideoView videoView;
    ProgressBar progressBar;

    boolean canBeSaved = false;
    Bundle extras;
    VideoAsyncTask task = new VideoAsyncTask();

    String category;

    String giphyType1, giphyType2, gfycatType1, gfycatType2, gfycatType3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);


        FloatingActionButton fab_activity_gif_share = (FloatingActionButton)findViewById(R.id.fab_activity_gif_share);
        fab_activity_gif_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://giphy.com/gifs/" + gifId;

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", url);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(), "Copied to clipboard!", Toast.LENGTH_LONG).show();


            }
        });
        gifImage = (ImageView) findViewById(R.id.gif_image);
        videoView = (VideoView) findViewById(R.id.videoView1);

        progressBar = (ProgressBar) findViewById(R.id.gif_progress);
        View gifblankview = (View) findViewById(R.id.gif_blank_view);
        NestedScrollView gifscrollview = (NestedScrollView) findViewById(R.id.gif_scrollview);


        //DeepLink Stuff
        if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = getIntent().getExtras();

            giphyType1 = parameters.getString("giphyType1");     //https://giphy.com/gifs/{giphyType1}
            Log.d(LOG_TAG, "giphyType1 : " + giphyType1);

            giphyType2 = parameters.getString("giphyType2");     //https://giphy.com/gifs/{giphyType2}/html5
            Log.d(LOG_TAG, "giphyType2 : " + giphyType2);

            gfycatType1 = parameters.getString("gfycatType1");   //https://gfycat.com/{gfycatType1}
            Log.d(LOG_TAG, "gfycatType1 : " + gfycatType1);

            gfycatType2 = parameters.getString("gfycatType2");   //https://zippy.gfycat.com/{gfycatType2}
            Log.d(LOG_TAG, "gfycatType2 : " + gfycatType2);

            gfycatType3 = parameters.getString("gfycatType3");   //https://giant.gfycat.com/{gfycatType3}
            Log.d(LOG_TAG, "gfycatType3 : " + gfycatType3);


            buildGifUrl(giphyType1, giphyType2, gfycatType1, gfycatType2, gfycatType3);

        }

        //Run this only when Internal Activity Passes Data.
        Intent i = getIntent();
        extras = i.getExtras();
        if (extras.containsKey("getId")) {

            canBeSaved = true;

            gifRating = i.getStringExtra("getRating");
            gifImportDatetime = i.getStringExtra("getImportDatetime");
            gifTrendingDatetime = i.getStringExtra("getTrendingDatetime");
            gifMp4 = i.getStringExtra("getMp4");
            gifMp4Size = i.getStringExtra("getMp4Size");
            gifWebp = i.getStringExtra("getWebp");
            gifWebpSize = i.getStringExtra("getWebpSize");
            gifStillUrl = i.getStringExtra("getStillUrl");
            gifWidth = i.getStringExtra("getWidth");
            gifHeight = i.getStringExtra("getHeight");
            gifId = i.getStringExtra("getId");

            typeOfData = i.getStringExtra("typeOfData");
            searchData = i.getStringExtra("searchData");


            Picasso.with(getApplicationContext())
                    .load(gifStillUrl)
                    .placeholder(R.color.colorAccent)
                    .into(gifImage);

            doStuff(gifMp4);
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

                calendar = Calendar.getInstance();
                currentDate = utils.getDateTime(calendar);

                //String mGifID, int mTypeOfData, String mTag, String mDate
                if (canBeSaved) {

                    FragmentManager fm = getFragmentManager();
                    CategoryFilterFragment dialogFragment = new CategoryFilterFragment();
                    dialogFragment.show(fm, "Fragment");

                    //Handle Setting in OnFilterSelected

                } else {
                    Toast.makeText(getApplicationContext(), "Cannot be saved", Toast.LENGTH_LONG).show();

                }


            }
        });
    }

    void buildGifUrl(String giphyType1, String giphyType2, String gfycatType1, String gfycatType2, String gfycatType3) {
        String builtURL = null;

        if (giphyType1 != null) {
            canBeSaved = true;
            builtURL = "https://media2.giphy.com/media/" + giphyType1 + "/200.mp4";


            Picasso.with(getApplicationContext())
                    .load("https://media2.giphy.com/media/" + giphyType1 + "/200.gif")
                    .placeholder(R.color.colorAccent)
                    .into(gifImage);

        } else if (giphyType2 != null) {
            canBeSaved = true;
            builtURL = "https://media2.giphy.com/media/" + giphyType2 + "/200.mp4";


            Picasso.with(getApplicationContext())
                    .load("https://media2.giphy.com/media/" + giphyType2 + "/200.gif")
                    .placeholder(R.color.colorAccent)
                    .into(gifImage);


        } else if (gfycatType1 != null) {
            canBeSaved = false;

            builtURL = "https://zippy.gfycat.com/" + gfycatType1 + ".mp4";

        } else if (gfycatType2 != null) {
            canBeSaved = false;

            builtURL = "https://zippy.gfycat.com/" + gfycatType2;

        } else if (gfycatType3 != null) {
            canBeSaved = false;

            builtURL = "https://giant.gfycat.com/" + gfycatType3;

        }

        Log.d(LOG_TAG, "builtURL : " + builtURL);
        doStuff(builtURL);
    }

    @Override
    public void onFilterSelected(String mcategory) {
        this.category = mcategory;

        //TODO Make sure When saving the value isn't already in the database
        if (extras.containsKey("getId")) {

            FavouriteGif favouriteGif = new FavouriteGif(gifId, currentDate, category);
            favouriteGif.save();
        } else if (giphyType1 != null) {
            searchData = "receivedGif";
            FavouriteGif favouriteGif = new FavouriteGif(giphyType1, currentDate, category);
            favouriteGif.save();
        } else if (giphyType2 != null) {
            searchData = "receivedGif";
            FavouriteGif favouriteGif = new FavouriteGif(giphyType2, currentDate, category);
            favouriteGif.save();
        }
        //TODO: Snackbar here

    }


    void doStuff(String data) {
        task.setData(getApplicationContext(), data, progressBar);
        task.videoDownloadResponse = this;
        task.execute();
    }

    @Override
    public void localVideoUrl(String videoUrl) {
        localurl = videoUrl;
        Log.d(LOG_TAG, localurl);
        gifImage.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);


        //specify the location of media file
        Uri uri = Uri.parse(localurl);

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

