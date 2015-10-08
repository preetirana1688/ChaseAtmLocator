package com.chase.chaseatmlocator.framework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chase.chaseatmlocator.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private List<Location> resultsList = new ArrayList<Location>();
    private Context context;

    public ListAdapter(Context context, List<Location> resultsList) {
        this.context = context;
        this.resultsList = resultsList;
    }

    @Override
    public int getCount() {
        return resultsList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.list_item, parent, false);
        }
        TextView locationType = (TextView) convertView.findViewById(R.id.locationType);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        locationType.setText(resultsList.get(position).getLocType());
        distance.setText(String.valueOf(resultsList.get(position).getDistance()));
        name.setText(resultsList.get(position).getName());
        address.setText(resultsList.get(position).getAddress());
        return convertView;
    }
}
