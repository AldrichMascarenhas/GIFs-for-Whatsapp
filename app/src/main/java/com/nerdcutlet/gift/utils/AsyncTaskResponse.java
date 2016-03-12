package com.nerdcutlet.gift.utils;

import com.nerdcutlet.gift.models.giphy.Datum;

import java.util.List;

/**
 * Created by Aldrich on 13-03-2016.
 */
public interface AsyncTaskResponse {
    void processFinish(List<Datum> p);

}
