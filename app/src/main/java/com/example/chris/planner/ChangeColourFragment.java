package com.example.chris.planner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chris on 22/04/2016.
 */
public class ChangeColourFragment  extends Fragment {

    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_change_colour_static, container, false);
        return rootView;
    }
}
