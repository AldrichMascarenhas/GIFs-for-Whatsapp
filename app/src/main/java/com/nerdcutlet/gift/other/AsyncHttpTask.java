package com.nerdcutlet.gift.other;

import android.os.AsyncTask;
import android.util.Log;

import com.nerdcutlet.gift.BuildConfig;
import com.nerdcutlet.gift.models.giphy.Datum;
import com.nerdcutlet.gift.models.giphy.GIFModelMain;
import com.nerdcutlet.gift.network.GiphyApi;
import com.nerdcutlet.gift.network.GiphyApiInterface;
import com.nerdcutlet.gift.utils.AsyncTaskResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Aldrich on 13-03-2016.
 */
public class AsyncHttpTask extends AsyncTask<Void, Void, Void> {

    public AsyncTaskResponse asyncTaskResponse = null;

    private final static String LOG_TAG = "AsyncHttpTask";
    String searchParameter;
    boolean isGif;
    boolean trending;
    String rating;
    int limit;


    public void setData(String search, boolean isGif, boolean trending, String rating, int limit) {
        try {
            this.searchParameter = URLEncoder.encode(search, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, e.toString());

        }
        this.isGif = isGif;
        this.trending = trending;
        this.rating = rating;
        this.limit = limit;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        GiphyApiInterface interf = GiphyApi.createService(GiphyApiInterface.class);
        Call<GIFModelMain> gifCall;
        Call<GIFModelMain> stickerCall;
        Call<GIFModelMain> trendingCall;

        if (isGif) {
            //Is a Gif
            gifCall = interf.searchGifs(searchParameter, rating, limit, BuildConfig.GIPHY_API_TOKEN);
            FetchData(gifCall);

        } else if(!isGif && !trending){
            //Is a Sticker as !Gif & !trending
            stickerCall = interf.searchStickers(searchParameter, rating, limit, BuildConfig.GIPHY_API_TOKEN);
            FetchData(stickerCall);

        }else if(!isGif && trending) {
            //Trending
            trendingCall = interf.getTrendingGifs(rating, limit, BuildConfig.GIPHY_API_TOKEN);
            FetchData(trendingCall);

        }

        return null;


    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

    public void FetchData(Call<GIFModelMain> call) {

        call.enqueue(new Callback<GIFModelMain>() {
            @Override
            public void onResponse(Call<GIFModelMain> call, Response<GIFModelMain> response) {
                Log.d(LOG_TAG, "Status Code = " + response.code());

                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    GIFModelMain result
                            = response.body();

                    asyncTaskResponse.processFinish(result.getData());


                    //adapter.setmGIFDataList(p);
                    /*
                    Log.d(LOG_TAG, "response = " + new Gson().toJson(result));
                    for (int x = 0; x < p.size(); x++) {
                        Log.d(LOG_TAG, "id : " + p.get(x).getId());
                    }
                    */
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
    }

}


