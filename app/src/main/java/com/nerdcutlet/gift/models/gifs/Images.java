
package com.nerdcutlet.gift.models.gifs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {


    @SerializedName("fixed_height")
    @Expose
    private FixedHeight fixedHeight;


    @SerializedName("fixed_height_still")
    @Expose
    private FixedHeightStill fixedHeightStill;


    @SerializedName("fixed_height_downsampled")
    @Expose
    private FixedHeightDownsampled fixedHeightDownsampled;




    @SerializedName("fixed_height_small")
    @Expose
    private FixedHeightSmall fixedHeightSmall;
    @SerializedName("fixed_height_small_still")
    @Expose
    private FixedHeightSmallStill fixedHeightSmallStill;



    

    /**
     * 
     * @return
     *     The fixedHeight
     */
    public FixedHeight getFixedHeight() {
        return fixedHeight;
    }

    /**
     * 
     * @param fixedHeight
     *     The fixed_height
     */
    public void setFixedHeight(FixedHeight fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    /**
     * 
     * @return
     *     The fixedHeightStill
     */
    public FixedHeightStill getFixedHeightStill() {
        return fixedHeightStill;
    }

    /**
     * 
     * @param fixedHeightStill
     *     The fixed_height_still
     */
    public void setFixedHeightStill(FixedHeightStill fixedHeightStill) {
        this.fixedHeightStill = fixedHeightStill;
    }

    /**
     * 
     * @return
     *     The fixedHeightDownsampled
     */
    public FixedHeightDownsampled getFixedHeightDownsampled() {
        return fixedHeightDownsampled;
    }

    /**
     * 
     * @param fixedHeightDownsampled
     *     The fixed_height_downsampled
     */
    public void setFixedHeightDownsampled(FixedHeightDownsampled fixedHeightDownsampled) {
        this.fixedHeightDownsampled = fixedHeightDownsampled;
    }




  
    /**
     * 
     * @return
     *     The fixedHeightSmall
     */
    public FixedHeightSmall getFixedHeightSmall() {
        return fixedHeightSmall;
    }

    /**
     * 
     * @param fixedHeightSmall
     *     The fixed_height_small
     */
    public void setFixedHeightSmall(FixedHeightSmall fixedHeightSmall) {
        this.fixedHeightSmall = fixedHeightSmall;
    }

    /**
     * 
     * @return
     *     The fixedHeightSmallStill
     */
    public FixedHeightSmallStill getFixedHeightSmallStill() {
        return fixedHeightSmallStill;
    }

    /**
     * 
     * @param fixedHeightSmallStill
     *     The fixed_height_small_still
     */
    public void setFixedHeightSmallStill(FixedHeightSmallStill fixedHeightSmallStill) {
        this.fixedHeightSmallStill = fixedHeightSmallStill;
    }

    
}
