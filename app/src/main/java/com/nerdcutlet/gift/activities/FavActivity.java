package com.nerdcutlet.gift.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nerdcutlet.gift.BuildConfig;
import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.fragments.FavouriteFilterFragment;
import com.nerdcutlet.gift.fragments.FilterFragment;
import com.nerdcutlet.gift.models.FavouriteGif;
import com.nerdcutlet.gift.models.giphy.Datum;
import com.nerdcutlet.gift.models.giphy.GIFModelMain;
import com.nerdcutlet.gift.network.GiphyApi;
import com.nerdcutlet.gift.network.GiphyApiInterface;
import com.nerdcutlet.gift.other.AsyncHttpTask;
import com.nerdcutlet.gift.other.adapters.MyRecyclerAdapter;
import com.nerdcutlet.gift.utils.AsyncTaskResponse;
import com.nerdcutlet.gift.views.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavActivity extends AppCompatActivity implements AsyncTaskResponse, FavouriteFilterFragment.OnFilterSelectedListener {

    public AsyncTaskResponse asyncTaskResponse = null;


    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;


    String searchData;
    int typeOfData = 3;
    List<Datum> datums;

    int favGifType;
    String favCategory;

    @Override
    public void processFinish(List<Datum> p) {
        datums.clear();
        datums = p;
        adapter.setmGIFDataList(p);
        adapter.notifyDataSetChanged();

    }


    AsyncHttpTask task = new AsyncHttpTask();

    public final static String LOG_TAG = "FavActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_fav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        adapter = new MyRecyclerAdapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Datum selectedDatum = datums.get(position);

                Intent gifIntent = new Intent(getApplicationContext(), GifActivity.class);

                gifIntent.putExtra("getId", selectedDatum.getId());
                gifIntent.putExtra("getRating", selectedDatum.getRating());
                gifIntent.putExtra("getImportDatetime", selectedDatum.getImportDatetime());
                gifIntent.putExtra("getTrendingDatetime", selectedDatum.getTrendingDatetime());

                gifIntent.putExtra("getMp4", selectedDatum.getImages().getFixedHeight().getMp4());
                gifIntent.putExtra("getMp4Size", selectedDatum.getImages().getFixedHeight().getMp4Size());
                gifIntent.putExtra("getWebp", selectedDatum.getImages().getFixedHeight().getWebp());
                gifIntent.putExtra("getWebpSize", selectedDatum.getImages().getFixedHeight().getWebpSize());

                gifIntent.putExtra("getStillUrl", selectedDatum.getImages().getFixedHeightStill().getUrl());
                gifIntent.putExtra("getWidth", selectedDatum.getImages().getFixedHeight().getWidth());
                gifIntent.putExtra("getHeight", selectedDatum.getImages().getFixedHeight().getHeight());

                startActivity(gifIntent);

            }
        });

        mRecyclerView.setAdapter(adapter);

        task.setData(getGifIdsFromDatabase(4), typeOfData); //By default show all saved, //Fav API call
        task.asyncTaskResponse = this;
        task.execute();


    }

    String getGifIdsFromDatabase(int favGifType) {
        /*
        Random - Type 0;
        Trending - Type 1;
        Received - Type 2;
        Category - Type 3;
        */
        List<FavouriteGif> books = new ArrayList<>();

        if (favGifType == 0) {
            //Todo: random gifs
        } else if (favGifType == 1) {
            books = FavouriteGif.find(FavouriteGif.class, "M_TAG = ?", "trendingGif");

        } else if (favGifType == 2) {
            books = FavouriteGif.find(FavouriteGif.class, "M_TAG = ?", "receivedGif");

        } else if (favGifType == 3) {
            books = FavouriteGif.find(FavouriteGif.class, "M_CATEGORY = ?", favCategory);
        } else if (favGifType == 4) {
            books = FavouriteGif.listAll(FavouriteGif.class);
        }
        ArrayList<String> ids = new ArrayList<>();

        if (!books.isEmpty()) {
            for (int i = 0; i < books.size(); i++) {
                ids.add(books.get(i).getmGifID());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No fav gifs", Toast.LENGTH_LONG).show();
        }


        Log.d(LOG_TAG, "Array List Size  : " + ids.size());
        String listString = "";
        for (int i = 0; i < ids.size(); i++) {
            listString = listString + "," + ids.get(i);
        }
        listString = listString.replaceFirst(",", "");
        Log.d(LOG_TAG, "String : " + listString);

        return listString;
    }

    @Override
    public void onFilterSelected(int typeOfData, String searchData) {

        favGifType = typeOfData;
        favCategory = searchData;
        String data = getGifIdsFromDatabase(favGifType);

        AsyncHttpTask task = new AsyncHttpTask();


        task.setData(data, typeOfData); //By default show all saved, //Fav API call
        task.asyncTaskResponse = this;
        task.execute();

        //Toast.makeText(getApplicationContext(), searchData + " " + typeOfData, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourite_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            FragmentManager fm = getFragmentManager();
            FavouriteFilterFragment dialogFragment = new FavouriteFilterFragment();
            dialogFragment.show(fm, "Fragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
