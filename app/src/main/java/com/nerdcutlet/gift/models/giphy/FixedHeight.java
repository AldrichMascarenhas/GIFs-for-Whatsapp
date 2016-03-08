
package com.nerdcutlet.gift.models.giphy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FixedHeight {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("mp4")
    @Expose
    private String mp4;
    @SerializedName("mp4_size")
    @Expose
    private String mp4Size;
    @SerializedName("webp")
    @Expose
    private String webp;
    @SerializedName("webp_size")
    @Expose
    private String webpSize;

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The width
     */
    public String getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * 
     * @return
     *     The height
     */
    public String getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * 
     * @return
     *     The size
     */
    public String getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * 
     * @return
     *     The mp4
     */
    public String getMp4() {
        return mp4;
    }

    /**
     * 
     * @param mp4
     *     The mp4
     */
    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    /**
     * 
     * @return
     *     The mp4Size
     */
    public String getMp4Size() {
        return mp4Size;
    }

    /**
     * 
     * @param mp4Size
     *     The mp4_size
     */
    public void setMp4Size(String mp4Size) {
        this.mp4Size = mp4Size;
    }

    /**
     * 
     * @return
     *     The webp
     */
    public String getWebp() {
        return webp;
    }

    /**
     * 
     * @param webp
     *     The webp
     */
    public void setWebp(String webp) {
        this.webp = webp;
    }

    /**
     * 
     * @return
     *     The webpSize
     */
    public String getWebpSize() {
        return webpSize;
    }

    /**
     * 
     * @param webpSize
     *     The webp_size
     */
    public void setWebpSize(String webpSize) {
        this.webpSize = webpSize;
    }

}
