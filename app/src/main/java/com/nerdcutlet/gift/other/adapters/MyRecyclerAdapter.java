package com.nerdcutlet.gift.other.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.models.giphy.Datum;
import com.nerdcutlet.gift.views.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Datum data = mGIFDataList.get(position);

        Picasso.with(mContext)
                .load(data.getImages().getFixedHeightSmallStill().getUrl())
                .placeholder(R.color.colorAccent)
                .into(holder.mGifImageView);

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

class CustomViewHolder extends RecyclerView.ViewHolder{
    protected ImageView mGifImageView;
    protected TextView mGifNameTextView;
    protected TextView mGIFTypeTextView;

    public CustomViewHolder(View view) {
        super(view);


        this.mGifImageView = (ImageView) view.findViewById(R.id.gif_image_view);
        this.mGifNameTextView = (TextView) view.findViewById(R.id.gif_name);
        this.mGIFTypeTextView = (TextView) view.findViewById(R.id.gif_type);


    }

}