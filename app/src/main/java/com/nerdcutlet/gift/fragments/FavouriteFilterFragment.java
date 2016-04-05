package com.nerdcutlet.gift.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.models.Categories;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aldrich on 01-04-2016.
 */
public class FavouriteFilterFragment extends DialogFragment {

    boolean isListItemSelected = false;
    int filterType;

    OnFilterSelectedListener mCallback;
    String category;
    /*
        Random - Type 0;
        Trending - Type 1;
        Received - Type 2;
        Category - Type 3;
     */

    @Bind(R.id.imagebutton_fragment_favourite_random_gifs)
    ImageButton imageButtonRandomGifs;

    @Bind(R.id.imagebutton_fragment_favourite_trending_gifs)
    ImageButton imageButtonTrendingGifs;

    @Bind(R.id.button_fragment_favourite_received_gifs)
    Button buttonReceivedGifs;

    @Bind(R.id.button_fragment_favourite_setfilter)
    Button buttonSetFilter;


    @OnClick(R.id.imagebutton_fragment_favourite_random_gifs)
    void getRandomGifs() {
        filterType = 0;
        isListItemSelected = false;

        listViewGifCategories.clearChoices();
        listViewGifCategories.requestLayout();

        imageButtonRandomGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected));
        imageButtonTrendingGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.notselected));
        buttonReceivedGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.notselected));

    }

    @OnClick(R.id.imagebutton_fragment_favourite_trending_gifs)
    void getTrendingGifs() {
        filterType = 1;
        isListItemSelected = false;

        listViewGifCategories.clearChoices();
        listViewGifCategories.requestLayout();

        imageButtonRandomGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.notselected));
        imageButtonTrendingGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected));
        buttonReceivedGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.notselected));


    }

    @OnClick(R.id.button_fragment_favourite_received_gifs)
    void getReceivedGifs() {
        filterType = 2;
        isListItemSelected = false;

        listViewGifCategories.clearChoices();
        listViewGifCategories.requestLayout();

        imageButtonRandomGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.notselected));
        imageButtonTrendingGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.notselected));
        buttonReceivedGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected));


    }

    @OnClick(R.id.button_fragment_favourite_setfilter)
    void dismissDialog() {
        /*todo: logic to select and verify data. Allow for only one to be selected. Chck if listview selected else
        if listview then its own callback, else normal.
         */

        if (isListItemSelected) {
            mCallback.onFilterSelected(3, category);
            dismiss();

        } else if (filterType == 0) {
            mCallback.onFilterSelected(0, "random");
            dismiss();


        } else if (filterType == 1) {
            mCallback.onFilterSelected(1, "trending");
            dismiss();


        } else if (filterType == 2) {
            mCallback.onFilterSelected(2, "received");
            dismiss();

        }

    }

    @Bind(R.id.listview_fragment_favourite_gif_categories)
    ListView listViewGifCategories;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite_filter, container, false);

        ButterKnife.bind(this, rootView);

        List<Categories> categories = Categories.listAll(Categories.class);
        ArrayList<String> categoriesList = new ArrayList<>();

        if (!categories.isEmpty()) {
            for (int i = 0; i < categories.size(); i++) {
                categoriesList.add(categories.get(i).getGifCategory());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoriesList);
                listViewGifCategories.setAdapter(adapter);
            }
        } else {
            Toast.makeText(getActivity(), "No saved categories", Toast.LENGTH_LONG).show();
        }

        listViewGifCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                imageButtonRandomGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.notselected));
                imageButtonTrendingGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.notselected));
                buttonReceivedGifs.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.notselected));

                isListItemSelected = listViewGifCategories.isItemChecked(position);
                category = listViewGifCategories.getItemAtPosition(position).toString();

            }
        });

        getDialog().setTitle("Hi");
        return rootView;
    }


    public interface OnFilterSelectedListener {
        public void onFilterSelected(int favType, String selectedData);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFilterSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnFilterSelectedListener");
        }

    }

}
