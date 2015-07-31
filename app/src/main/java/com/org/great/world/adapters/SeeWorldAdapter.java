package com.org.great.world.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by dj on 2015/7/31.
 * email:dingjun0225@gmail.com
 */
public class SeeWorldAdapter extends BAdapter
{
    public SeeWorldAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            TextView view = new TextView(mContext);
            view.setWidth(500);
            view.setHeight(100);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(50);
            view.setText("" + position);
            view.setBackgroundColor(Color.GREEN);
            return view;
        }
        else
        {
            TextView v = (TextView)convertView;
            v.setText(""+position);
            v.setBackgroundColor(Color.GREEN);
            v.setWidth(500);
            v.setHeight(100);
            v.setGravity(Gravity.CENTER);
            v.setTextSize(50);
            return v;
        }
    }


}
