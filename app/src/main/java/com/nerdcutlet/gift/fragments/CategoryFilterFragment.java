package com.nerdcutlet.gift.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.models.Categories;
import com.nerdcutlet.gift.models.FavouriteGif;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aldrich on 03-04-2016.
 */
public class CategoryFilterFragment extends DialogFragment {

    public final static String LOG_TAG = "CategoryFilterFragment";

    ArrayAdapter<String> adapter;
    ListView categoryListView;
    AutoCompleteTextView autoCompleteTextView;

    OnFilterSelectedListener mCallback;
    String category;

    boolean isSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category_filter, container, false);
        ButterKnife.bind(this, rootView);

        categoryListView = (ListView) rootView.findViewById(R.id.category_listview);
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.gif_category_Add);

        updateListView();

        Button button_category_Add = (Button) rootView.findViewById(R.id.button_category_Add);

        Button set_filter = (Button) rootView.findViewById(R.id.set_filter);
        set_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isSelected){
                    //Snaxkbar nothing selecteed
                    Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
                    dismiss();

                }
                else {
                    mCallback.onFilterSelected(category);

                    dismiss();
                }


            }
        });


        button_category_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                try{
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (NullPointerException e){
                    //
                }


                category = autoCompleteTextView.getText().toString();
                Log.d(LOG_TAG, "category : " + category);

                Categories categories = new Categories(category);
                categories.save();

                updateListView();
            }
        });

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                isSelected = categoryListView.isItemChecked(position);
                category = categoryListView.getItemAtPosition(position).toString();

            }
        });

        getDialog().setTitle("Hi");
        return rootView;
    }

    void updateListView() {
        List<Categories> categories = Categories.listAll(Categories.class);
        ArrayList<String> categoriesList = new ArrayList<>();

        if (!categories.isEmpty()) {
            for (int i = 0; i < categories.size(); i++) {
                categoriesList.add(categories.get(i).getGifCategory());

                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoriesList);
                categoryListView.setAdapter(adapter);
            }
        } else {
            Toast.makeText(getActivity(), "No saved categories", Toast.LENGTH_LONG).show();
        }
    }

    public interface OnFilterSelectedListener {
        public void onFilterSelected(String category);

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

