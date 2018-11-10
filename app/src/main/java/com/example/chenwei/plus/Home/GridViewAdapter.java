package com.example.chenwei.plus.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenwei.plus.R;

public class GridViewAdapter extends BaseAdapter {

    private int[] resIds;
    private String[] titles;
    private Context context;
    private LayoutInflater inflater;
    public GridViewAdapter(int[] resIds, String[] titles, Context context) {
        super();
        this.resIds = resIds;
        this.titles = titles;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.gridview_item, null);
        ImageView img=    (ImageView) convertView.findViewById(R.id.img_home);
        TextView tv=  (TextView) convertView.findViewById(R.id.text_home);
        img.setImageResource(resIds[position]);
        tv.setText(titles[position]);
        return convertView;

    }
}
