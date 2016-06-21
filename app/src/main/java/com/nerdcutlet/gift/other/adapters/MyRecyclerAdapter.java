package com.nerdcutlet.gift.other.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.ImageViewBitmapInfo;
import com.koushikdutta.ion.Ion;
import com.nerdcutlet.gift.App;
import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.models.giphy.Datum;
import com.nerdcutlet.gift.other.PaletteTransformation;
import com.nerdcutlet.gift.views.OnItemClickListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Aldrich on 08-03-2016.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private final static String LOG_TAG = "MyRecyclerAdapter";
    private List<Datum> mGIFDataList;
    private LayoutInflater mInflater;
    private Context mContext;

    DisplayMetrics display;
    int width = 0;
    int height = 0;

    OnItemClickListener listener;

    App app;

    public MyRecyclerAdapter(Context context, OnItemClickListener listener) {

        display = context.getResources().getDisplayMetrics();
        width = display.widthPixels;
        height = display.heightPixels;
        width = width / 2;
        height = height / 3;
        Log.e(LOG_TAG, "Width : " + width);
        Log.e(LOG_TAG, "Height : " + height);

        this.listener = listener;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mGIFDataList = new ArrayList<>();
        app = (App) context.getApplicationContext();

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gif, parent, false);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.item_gif_linearlayout);
        ll.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition(), viewHolder.relativeLayout.getBackground());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        final Datum data = mGIFDataList.get(position);

        //Log.d(LOG_TAG, "Connection is : " + app.getmConnectionClass().toString());

        /*
        if(app.getmConnectionClass().toString().equals("POOR") ||app.getmConnectionClass().toString().equals("MODERATE") || app.getmConnectionClass().toString().equals("UNKNOWN")){

            Ion.with(holder.gifImageView)
                    .load();
        }else{
            Ion.with(holder.gifImageView)
                    .load(data.getImages().getFixedHeightSmall().getUrl());
        }*/


        //cancel any loading images on this view
        Picasso.with(mContext).cancelRequest(holder.gifImageView);
        //load the image
        Picasso.with(mContext).load(data.getImages().getFixedHeightSmallStill().getUrl()).transform(PaletteTransformation.instance()).into(holder.gifImageView, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) holder.gifImageView.getDrawable()).getBitmap(); // Ew!

                if (bitmap != null && !bitmap.isRecycled()) {
                    Palette palette = PaletteTransformation.getPalette(bitmap);

                    if (palette != null) {
                        Palette.Swatch s = palette.getVibrantSwatch();
                        if (s == null) {
                            s = palette.getDarkVibrantSwatch();
                        }
                        if (s == null) {
                            s = palette.getLightVibrantSwatch();
                        }
                        if (s == null) {
                            s = palette.getMutedSwatch();
                        }
                        if (s != null) {
                            Log.d(LOG_TAG, "position : " + position +  " Pop : " + s.getPopulation() + " Swatch : " + s.getRgb() + "Title text color : " + s.getTitleTextColor());
                            holder.relativeLayout.setBackgroundColor(s.getRgb());

                            holder.mGifNameTextView.setTextColor(s.getTitleTextColor());
                            holder.mGIFTypeTextView.setTextColor(s.getTitleTextColor());
                        }
                    }
                }

            }
        });

        holder.mGIFTypeTextView.setText(data.getId());
        holder.mGifNameTextView.setText(data.getRating());
    }

    @Override
    public int getItemCount() {
        return (mGIFDataList == null) ? 0 : mGIFDataList.size();

    }

    public void setmGIFDataList(List<Datum> mGIFDataList) {
        this.mGIFDataList.addAll(mGIFDataList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }
}

class CustomViewHolder extends RecyclerView.ViewHolder {
    protected TextView mGifNameTextView;
    protected TextView mGIFTypeTextView;
    protected GifImageView gifImageView;
    protected RelativeLayout relativeLayout;

    public CustomViewHolder(View view) {
        super(view);


        this.gifImageView = (GifImageView) view.findViewById(R.id.gifimageview_recyclerviewitem);
        this.mGifNameTextView = (TextView) view.findViewById(R.id.textview1_recyclerviewitem);
        this.mGIFTypeTextView = (TextView) view.findViewById(R.id.textview2_recyclerviewitem);
        this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_container_recyclerviewitem);


    }

}