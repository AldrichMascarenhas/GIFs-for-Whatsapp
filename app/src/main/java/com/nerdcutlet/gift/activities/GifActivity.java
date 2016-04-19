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
import android.support.v4.widget.TextViewCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@DeepLink({"https://giphy.com/gifs/{giphyType1}", "https://giphy.com/gifs/{giphyType2}/html5",
        "https://gfycat.com/{gfycatType1}", "https://zippy.gfycat.com/{gfycatType2}", "https://giant.gfycat.com/{gfycatType3}"})
public class GifActivity extends Activity implements VideoDownloadResponse, CategoryFilterFragment.OnFilterSelectedListener {

    public final static String LOG_TAG = "GifActivity";

    Utils utils = new Utils();
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

    //Colors
    int color;

    @Bind(R.id.linear_container_gif)
    LinearLayout linearLayout;

    @Bind(R.id.textview_gif_title)
    TextView textViewGifTitle;

    @Bind(R.id.button_gif_favourite)
    Button buttonGifFavourite;

    @OnClick(R.id.button_gif_favourite)
    public void addToFavourite() {

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


    @Bind(R.id.button_gif_savetogallery)
    Button buttonSaveToGallery;

    @OnClick(R.id.button_gif_savetogallery)
    public void saveToGallery() {
        Toast.makeText(getApplicationContext(), "This will Save Video Offline", Toast.LENGTH_LONG).show();

    }

    @Bind(R.id.fab_gif_share)
    FloatingActionButton fabGifShare;

    @OnClick(R.id.fab_gif_share)
    public void shareGif() {
        String url = "https://giphy.com/gifs/" + gifId;

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_gif_text)));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        ButterKnife.bind(this);


        gifImage = (ImageView) findViewById(R.id.gif_image);
        videoView = (VideoView) findViewById(R.id.videoView1);

        videoView.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.gif_progress);

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

            color = i.getIntExtra("backgroundColor", 0xFFFF0000);

            Log.d(LOG_TAG, "px to dp : " + utils.convertPixelsToDp(200, getApplicationContext()));

            if (typeOfData.equals("gif")) {
                textViewGifTitle.setText("Gif");

            } else if (typeOfData.equals("sticker")) {
                textViewGifTitle.setText("Sticker");

            } else if (typeOfData.equals("trendingGif")) {
                textViewGifTitle.setText("Trending Gif");

            }else if (typeOfData.equals("favGifs")) {
                textViewGifTitle.setText("Favourite Gif");

            }

            linearLayout.setBackgroundColor(color);
            Picasso.with(getApplicationContext())
                    .load(gifStillUrl)
                    .placeholder(R.color.colorAccent)
                    .into(gifImage);

            doStuff(gifMp4);
        }


    }

    void buildGifUrl(String giphyType1, String giphyType2, String gfycatType1, String gfycatType2, String gfycatType3) {
        String builtURL = null;

        if (giphyType1 != null) {
            canBeSaved = true;
            textViewGifTitle.setText("Giphy Gif");
            builtURL = "https://media2.giphy.com/media/" + giphyType1 + "/200.mp4";


            Picasso.with(getApplicationContext())
                    .load("https://media2.giphy.com/media/" + giphyType1 + "/200.gif")
                    .placeholder(R.color.colorAccent)
                    .into(gifImage);

        } else if (giphyType2 != null) {
            canBeSaved = true;
            textViewGifTitle.setText("Giphy Gif");
            builtURL = "https://media2.giphy.com/media/" + giphyType2 + "/200.mp4";


            Picasso.with(getApplicationContext())
                    .load("https://media2.giphy.com/media/" + giphyType2 + "/200.gif")
                    .placeholder(R.color.colorAccent)
                    .into(gifImage);


        } else if (gfycatType1 != null) {
            canBeSaved = false;
            textViewGifTitle.setText("Gfycat Gif");

            builtURL = "https://zippy.gfycat.com/" + gfycatType1 + ".mp4";

        } else if (gfycatType2 != null) {
            canBeSaved = false;
            textViewGifTitle.setText("Gfycat Gif");

            builtURL = "https://zippy.gfycat.com/" + gfycatType2;

        } else if (gfycatType3 != null) {
            canBeSaved = false;
            textViewGifTitle.setText("Gfycat Gif");

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

