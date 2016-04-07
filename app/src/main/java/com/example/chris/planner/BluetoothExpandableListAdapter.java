package com.example.chris.planner;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Chris on 03/04/2016.
 */
public class BluetoothExpandableListAdapter extends BaseExpandableListAdapter {
    private List<String> headerTitles;
    private HashMap<String, List<String>> childTitles;
    private Context ctx;

    BluetoothExpandableListAdapter(Context ctx, List<String> headerTitles,  HashMap<String, List<String>> childTitles){

        this.ctx = ctx;
        this.childTitles = childTitles;
        this.headerTitles = headerTitles;
    }

    @Override
    public int getGroupCount() {
        return headerTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return childTitles.get(headerTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return headerTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childTitles.get(headerTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String)this.getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bluetooth_parent_layout, null);
        }
        Log.d("parentview", title);
        TextView textView = (TextView) convertView.findViewById(R.id.parentItem);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title = (String)this.getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bluetooth_child_layout, null);
        }
        Log.d("childview", title);
        TextView textView = (TextView) convertView.findViewById(R.id.childItem);
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setText(title);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        if(getChildrenCount(groupPosition) > 6){
            return childPosition != 1 || childPosition != 0;
        }else if(getChildrenCount(groupPosition) == 6){
            return childPosition != 0;
        }else{
            return true;
        }
    }
}
