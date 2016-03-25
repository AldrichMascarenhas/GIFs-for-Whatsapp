package com.nerdcutlet.gift.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nerdcutlet.gift.BuildConfig;
import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.models.giphy.Datum;
import com.nerdcutlet.gift.models.giphy.GIFModelMain;
import com.nerdcutlet.gift.models.giphy.random.RandomGIFModel;
import com.nerdcutlet.gift.network.GiphyApi;
import com.nerdcutlet.gift.network.GiphyApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    int typeOfData = 0; //0 is GIF, 1 is Sticker , 2 is Trending GIFs.

    DisplayMetrics display;
    int height =0;
    int width =0;

    GiphyApiInterface interf = GiphyApi.createService(GiphyApiInterface.class);
    Call<RandomGIFModel> randomGif = interf.getRandomGif("", "g", BuildConfig.GIPHY_API_TOKEN);


    @Bind(R.id.gif_selected_button)
    Button gifSelectedButton;

    @OnClick(R.id.gif_selected_button)
    public void gifSelected() {
        typeOfData = 0;
        gifSelectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.selected));
        stickerSelectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.notselected));
    }


    @Bind(R.id.sticker_selected_button)
    Button stickerSelectedButton;

    @OnClick(R.id.sticker_selected_button)
    public void stickerSelected() {
        typeOfData = 1;
        gifSelectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.notselected));
        stickerSelectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.selected));
    }


    @Bind(R.id.search_edittext_field)
    EditText searchEditText;


    @Bind(R.id.background_view)
    LinearLayout background_view;

    @Bind(R.id.fav_button)
    Button fav_button;

    @Bind(R.id.trending_button)
    Button trending_button;

    @OnClick(R.id.trending_button)
    void trendingButtonClick(){
        typeOfData = 2;
        sendMessage("fake_data", typeOfData);
    }

    @OnClick(R.id.random_gif_button)
    void newRandomGif(){
        FetchData(randomGif);
    }

    @Bind(R.id.random_card_image)
    ImageView randomCardImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FetchData(randomGif);


        setViewHeight(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage(searchEditText.getText().toString(), typeOfData);
                    handled = true;
                }
                return handled;
            }
        });


    }

    public void sendMessage(String searchParams, int typeOfData) {

        Intent i = new Intent(getApplicationContext(), GifDisplayActivity.class);
        i.putExtra("search_param", searchParams);
        i.putExtra("typeOfData", typeOfData);
        startActivity(i);

    }

    public void setViewHeight(Context context){
        display = context.getResources().getDisplayMetrics();
        width = display.widthPixels;
        height = display.heightPixels;
        height = height * 7 / 10;
        background_view.setLayoutParams(new FrameLayout.LayoutParams(width, height));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void FetchData(Call<RandomGIFModel> call) {
        //Since a Call instance can only be used once. Use clone to use it over again.

        call.clone().enqueue(new Callback<RandomGIFModel>() {
            @Override
            public void onResponse(Call<RandomGIFModel> call, Response<RandomGIFModel> response) {
                Log.d(LOG_TAG, "Status Code = " + response.code());

                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    RandomGIFModel result
                            = response.body();
                    Picasso.with(getApplicationContext()).load(result.getData().getFixedHeightDownsampledUrl()).into(randomCardImage);


                    Log.d(LOG_TAG, "response = " + new Gson().toJson(result));


                } else {
                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                    String X = response.body().toString();
                    Log.e(LOG_TAG, "fail" + X);

                }
            }

            @Override
            public void onFailure(Call<RandomGIFModel> call, Throwable t) {
                Log.e(LOG_TAG, "fail");

            }
        });
    }
}
