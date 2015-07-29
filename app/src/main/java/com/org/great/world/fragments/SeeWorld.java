package com.org.great.world.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.org.great.world.Utils.Debug;
import com.org.great.world.Views.AutoListView;
import com.org.great.wrold.R;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class SeeWorld extends ChildBaseFragment{
    private AutoListView mAutoListView;
    private TestAdapter mTestAdapter;
    public SeeWorld() {
        mTitle = "SeeWorld";
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.reflash_list_layout, null);
        init(view);
        return view;
    }

    private void init(View layout)
    {
        mAutoListView = (AutoListView)layout.findViewById(R.id.auto_list);
        mAutoListView.setOnRefreshListener(new AutoListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "下拉刷新", Toast.LENGTH_SHORT).show();
                mAutoListView.onRefreshComplete();
            }
        });

        mAutoListView.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {
                Toast.makeText(getActivity(), "加载更多", Toast.LENGTH_SHORT).show();
                mAutoListView.onLoadComplete();
                mAutoListView.noLoadDate();
            }
        });

        mTestAdapter = new TestAdapter();
        mAutoListView.setAdapter(mTestAdapter);
        mTestAdapter.notifyDataSetChanged();
    }

    public class TestAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 50;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Debug.d("position = " + position);
            if(convertView == null)
            {
                TextView view = new TextView(getActivity());
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
}
