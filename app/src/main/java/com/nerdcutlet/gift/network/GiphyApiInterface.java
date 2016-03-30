package com.nerdcutlet.gift.network;

import com.nerdcutlet.gift.models.giphy.GIFModelMain;
import com.nerdcutlet.gift.models.giphy.random.RandomGIFModel;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Aldrich on 08-03-2016.
 */
public interface GiphyApiInterface {

    /*
    For URL Resolving Concept see here = : http://inthecheesefactory.com/blog/retrofit-2.0/en

        - Base URL: always ends with /

        - @Url: DO NOT start with /
     */

    @GET("gifs/search")
    Call<GIFModelMain> searchGifs(@Query("q") String searchParameter, @Query("rating") String rating, @Query("limit") int limit, @Query("api_key") String API_KEY);

    @GET("gifs/trending")
    Call<GIFModelMain> getTrendingGifs(@Query("rating") String rating, @Query("limit") int limit, @Query("api_key") String API_KEY);

    @GET("gifs/random")
    Call<RandomGIFModel> getRandomGif(@Query("tag") String tag, @Query("rating") String rating, @Query("api_key") String API_KEY);

    @GET("gifs")
    Call<GIFModelMain> getGifsByID(@Query("ids") String tag, @Query("api_key") String API_KEY);


    //
    @GET("stickers/search")
    Call<GIFModelMain> searchStickers(@Query("q") String searchParameter, @Query("rating") String rating, @Query("limit") int limit, @Query("api_key") String API_KEY);

    @GET("stickers/trending")
    Call<GIFModelMain> getTrendingStickers(@Query("s") String searchParameter, @Query("rating") String rating, @Query("limit") int limit, @Query("api_key") String API_KEY);

    @GET("stickers/random")
    Call<RandomGIFModel> getRandomSticker(@Query("tag") String tag, @Query("rating") String rating, @Query("api_key") String API_KEY);
}
