package com.nerdcutlet.gift.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdcutlet.gift.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aldrich on 10-03-2016.
 */
public class FilterFragment extends DialogFragment {

    public RadioButton ratingRadioButton;
    public RadioButton limitRadioButton;

    OnFilterSelectedListener mCallback;

    @Bind(R.id.rating_radio_group)
    RadioGroup ratingRadioGroup;
    @Bind(R.id.limit_radio_group)
    RadioGroup limitRadioGroup;
    @Bind(R.id.filter_set_button)
    Button filterSetButton;

    @OnClick(R.id.limit_radio_group)
    public void onLimitSet(){

    }
    @OnClick(R.id.rating_radio_group)
    public void onRatingSet(){

    }
    @OnClick(R.id.filter_set_button)
    public void setFilters(){
        limitRadioButton = (RadioButton) getView().findViewById(limitRadioGroup.getCheckedRadioButtonId());
        ratingRadioButton = (RadioButton) getView().findViewById(ratingRadioGroup.getCheckedRadioButtonId());


        mCallback.onFilterSelected(limitRadioButton, ratingRadioButton);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this,rootView);

        getDialog().setTitle("Hi");
        return rootView;
    }

    public interface OnFilterSelectedListener{
        public void onFilterSelected(RadioButton limit, RadioButton filter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (OnFilterSelectedListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement OnFilterSelectedListener");
        }

    }
}
