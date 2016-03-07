package com.nerdcutlet.gift.network;

import com.nerdcutlet.gift.models.gifs.GIFModelMain;
import com.nerdcutlet.gift.models.gifs.random.RandomGIFModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

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


}