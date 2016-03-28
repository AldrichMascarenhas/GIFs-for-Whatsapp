package com.nerdcutlet.gift.models;

import com.orm.SugarRecord;

/**
 * Created by Aldrich on 29-03-2016.
 */
public class FavouriteGif extends SugarRecord {
     String mid;

    public FavouriteGif() {
    }

    public FavouriteGif(String id) {
        this.mid = id;
    }

    public String getGifId(){
        return mid;
    }
}
