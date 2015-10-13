package com.org.great.world.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.org.great.world.data.CatalogPojo;
import com.org.great.world.data.GamePojo;

import java.util.List;

/**
 * Created by Administrator on 2015/8/27.
 */
public class GameAdapter extends BAdapter
{
    private List<GamePojo> mGameList;

    public void setList(List<GamePojo> list)
    {
        mGameList = list;
    }
    public GameAdapter(Context mContext) {
        super(mContext);
    }


    @Override
    public int getCount() {
        return mGameList.size();
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
        view.setText(mGameList.get(position).getTitle());
//        view.setBackgroundColor(Color.GREEN);
        view.setWidth(500);
        view.setHeight(100);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(30);
        return view;
    }
}
