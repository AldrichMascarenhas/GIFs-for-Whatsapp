package com.nerdcutlet.gift.activities;

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
import android.view.View;

import com.google.gson.Gson;
import com.nerdcutlet.gift.BuildConfig;
import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.models.giphy.Datum;
import com.nerdcutlet.gift.models.giphy.GIFModelMain;
import com.nerdcutlet.gift.network.GiphyApi;
import com.nerdcutlet.gift.network.GiphyApiInterface;
import com.nerdcutlet.gift.other.adapters.MyRecyclerAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GifDisplayActivity extends AppCompatActivity {

    public static final String LOG_TAG = "GifDisplayActivity";
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
       // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        adapter = new MyRecyclerAdapter(this);

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
        new AsyncHttpTask().execute();
    }

    public class AsyncHttpTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            GiphyApiInterface interf = GiphyApi.createService(GiphyApiInterface.class);
            Call<GIFModelMain> call = interf.searchGifs("cats", "g", 10, BuildConfig.GIPHY_API_TOKEN);


            call.enqueue(new Callback<GIFModelMain>() {
                @Override
                public void onResponse(Call<GIFModelMain> call, Response<GIFModelMain> response) {
                    Log.d(LOG_TAG, "Status Code = " + response.code());

                    if (response.isSuccess()) {
                        // request successful (status code 200, 201)
                        GIFModelMain result = response.body();
                        List<Datum> p = result.getData();

                        /*
                        Log.d(LOG_TAG, "response = " + new Gson().toJson(result));
                        for (int x = 0; x < p.size(); x++) {
                            Log.d(LOG_TAG, "id : " + p.get(x).getId());
                        }
                        */

                        adapter.setmGIFDataList(p);


                    } else {
                        //request not successful (like 400,401,403 etc)
                        //Handle errors
                        String X = response.body().toString();
                        Log.e(LOG_TAG, "fail" + X);

                    }
                }

                @Override
                public void onFailure(Call<GIFModelMain> call, Throwable t) {
                    Log.e(LOG_TAG, "fail");

                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
