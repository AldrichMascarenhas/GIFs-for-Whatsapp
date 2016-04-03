package com.nerdcutlet.gift.models;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by Aldrich on 29-03-2016.
 */
public class FavouriteGif extends SugarRecord {
    String mGifID;

    String mDate;
    /*
    *FORMAT : 'YYYY-mm-dd hh:mm:ss'
    *         24 hour format
    *         '2015-08-11 13:13:00' (time for 1:13pm exactly).
    */

    String mTag;
    /*
    searchData
    trendingGifs
    receivedGifs
    TODO: Add random
     */


    int mTypeOfData;
    /*
    0 - Gifs. [received gifs/stickers]
    2 - Trending Gifs.
    1 - Stickers [in app search ONLY]
     */

    String mCategory;

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public int getmTypeOfData() {
        return mTypeOfData;
    }

    public void setmTypeOfData(int mTypeOfData) {
        this.mTypeOfData = mTypeOfData;
    }

    public FavouriteGif(String mGifID, int mTypeOfData, String mTag, String mDate, String mCategory) {
        this.mGifID = mGifID;
        this.mTypeOfData = mTypeOfData;
        this.mTag = mTag;
        this.mDate = mDate;
        this.mCategory = mCategory;
    }

    public String getmGifID() {
        return mGifID;
    }

    public void setmGifID(String mGifID) {
        this.mGifID = mGifID;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTag() {
        return mTag;
    }

    public void setmTag(String mTag) {
        this.mTag = mTag;
    }


    public FavouriteGif() {
    }

}
