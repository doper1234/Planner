package com.example.chris.planner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chris on 22/04/2016.
 */
public class DeleteFragment  extends Fragment {

    ViewGroup rootView;
    boolean time, bomberman;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.delete_fragment_layout, container, false);

        rootView.findViewById(R.id.bomberManImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseOperations dbo = new DataBaseOperations(getContext());
                dbo.deleteDatabase();
            }
        });
        return rootView;
    }
}
