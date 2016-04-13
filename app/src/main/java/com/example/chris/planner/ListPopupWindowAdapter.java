package com.example.chris.planner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Chris on 09/04/2016.
 */
public class ListPopupWindowAdapter extends ArrayAdapter<String> {

    int[] imageIDs;
    Activity ctx;
    String[] itemNames;
    Activity a;

    public ListPopupWindowAdapter(Activity context, int resource, int textViewResource, String[] options, int[] imageResources) {
        super(context, resource, textViewResource, options);
        ctx = context;
        itemNames = options;
        imageIDs = imageResources;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater= ctx.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.more_options_layout, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.popTextView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listPopupWindowImageView);

        txtTitle.setText(itemNames[position]);
        imageView.setImageResource(imageIDs[position]);
        return rowView;

    };

//    R.layout.more_options_layout, R.id.popTextView
//    if(convertView == null){
//        LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView = layoutInflater.inflate(R.layout.event_parent_layout, null);
//    }
//    Log.d("parentview", title);
//    TextView textView = (TextView) convertView.findViewById(R.id.eventTextView);
//    textView.setTypeface(font, Typeface.BOLD);
//    textView.setText(title);
//    TextView timeTextView = (TextView) convertView.findViewById(R.id.timeLeftTextView);
//    timeTextView.setTypeface(font, Typeface.BOLD);
//    ImageView editImage = (ImageView) convertView.findViewById(R.id.editEventImageView);
//    setListPopup(editImage);
}
