package com.nerdcutlet.gift.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Aldrich on 03-04-2016.
 */
public class Categories extends SugarRecord {

    String gifCategory;

    public Categories(String gifCategory) {
        this.gifCategory = gifCategory;

    }

    public String getGifCategory(){
        return gifCategory;
    }
    public Categories() {

    }

}
