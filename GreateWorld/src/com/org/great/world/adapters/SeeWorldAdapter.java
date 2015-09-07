package com.org.great.world.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.org.great.world.data.CatalogPojo;

import java.util.List;

/**
 * Created by dj on 2015/7/31.
 * email:dingjun0225@gmail.com
 */
public class SeeWorldAdapter extends BAdapter
{
    private List<CatalogPojo> mCatalogList;

    public void setList(List<CatalogPojo> list)
    {
        mCatalogList = list;
    }

    public SeeWorldAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public int getCount() {
        return mCatalogList.size();
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
        TextView view = null;
        if (convertView == null) {
            view = new TextView(mContext);
        } else {
            view = (TextView) convertView;
        }
        view.setText(mCatalogList.get(position).getTitle());
        view.setBackgroundColor(Color.GREEN);
        view.setWidth(500);
        view.setHeight(100);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(30);
        return view;
    }

}
