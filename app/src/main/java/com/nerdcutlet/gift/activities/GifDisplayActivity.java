package com.nerdcutlet.gift.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.nerdcutlet.gift.fragments.FilterFragment;
import com.nerdcutlet.gift.models.giphy.Datum;
import com.nerdcutlet.gift.models.giphy.GIFModelMain;
import com.nerdcutlet.gift.network.GiphyApi;
import com.nerdcutlet.gift.network.GiphyApiInterface;
import com.nerdcutlet.gift.other.AsyncHttpTask;
import com.nerdcutlet.gift.other.adapters.MyRecyclerAdapter;
import com.nerdcutlet.gift.utils.AsyncTaskResponse;
import com.nerdcutlet.gift.views.OnItemClickListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class GifDisplayActivity extends AppCompatActivity implements FilterFragment.OnFilterSelectedListener, AsyncTaskResponse {

    public static final String LOG_TAG = "GifDisplayActivity";
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;

    AsyncHttpTask task = new AsyncHttpTask();

    String searchData;
    String typeOfData;
    String rating = "g";
    int limit = 50;

    List<Datum> datums;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            searchData = intent.getStringExtra("search_param");
            typeOfData = intent.getStringExtra("typeOfData");
            Log.d(LOG_TAG, "searchData : " + searchData + " " + "typeOfData : " + typeOfData);
        }



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
                gifIntent.putExtra("getRating",selectedDatum.getRating());
                gifIntent.putExtra("getImportDatetime",selectedDatum.getImportDatetime());
                gifIntent.putExtra("getTrendingDatetime",selectedDatum.getTrendingDatetime());

                gifIntent.putExtra("getMp4", selectedDatum.getImages().getFixedHeight().getMp4());
                gifIntent.putExtra("getMp4Size", selectedDatum.getImages().getFixedHeight().getMp4Size());
                gifIntent.putExtra("getWebp",selectedDatum.getImages().getFixedHeight().getWebp() );
                gifIntent.putExtra("getWebpSize",selectedDatum.getImages().getFixedHeight().getWebpSize() );

                gifIntent.putExtra("getStillUrl",selectedDatum.getImages().getFixedHeightStill().getUrl());
                gifIntent.putExtra("getWidth", selectedDatum.getImages().getFixedHeight().getWidth());
                gifIntent.putExtra("getHeight", selectedDatum.getImages().getFixedHeight().getHeight());

                gifIntent.putExtra("typeOfData", typeOfData);
                gifIntent.putExtra("searchData", searchData);
                startActivity(gifIntent);
            }
        });

        mRecyclerView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        task.setData(searchData, typeOfData, rating, limit);
        task.asyncTaskResponse = this;
        task.execute();


    }


    public void onFilterSelected(RadioButton filter, RadioButton limit) {

        Toast.makeText(getApplicationContext(), "SELECTED " + filter.getText() + " " + limit.getText(), Toast.LENGTH_LONG).show();

    }
    public void processFinish(List<Datum> p){
        datums = p;
        adapter.setmGIFDataList(p);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
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
            FilterFragment dialogFragment = new FilterFragment();
            dialogFragment.show(fm, "Fragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
