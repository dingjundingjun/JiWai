package com.org.great.world.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.org.great.world.Utils.Debug;
import com.org.great.world.Views.AutoListView;
import com.org.great.world.adapters.SeeWorldAdapter;
import com.org.great.wrold.R;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class SeeWorld extends ChildBaseFragment{
    private AutoListView mAutoListView;
    private SeeWorldAdapter mSeeWorldAdapter;
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

        mSeeWorldAdapter = new SeeWorldAdapter(getActivity());
        mAutoListView.setAdapter(mSeeWorldAdapter);
        mSeeWorldAdapter.notifyDataSetChanged();
        mAutoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
