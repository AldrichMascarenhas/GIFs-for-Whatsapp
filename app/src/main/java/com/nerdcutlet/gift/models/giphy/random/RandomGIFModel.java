
package com.nerdcutlet.gift.models.giphy.random;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RandomGIFModel {

    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("meta")
    @Expose
    private Meta meta;

    /**
     * 
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     * 
     * @return
     *     The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * 
     * @param meta
     *     The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
