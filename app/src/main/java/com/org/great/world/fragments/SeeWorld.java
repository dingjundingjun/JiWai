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

import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.HttpUtils;
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.AutoListView;
import com.org.great.world.Views.Titanic;
import com.org.great.world.Views.TitanicTextView;
import com.org.great.world.adapters.SeeWorldAdapter;
import com.org.great.world.data.BaseCatalogPojo;
import com.org.great.wrold.R;

import org.apache.http.Header;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class SeeWorld extends ChildBaseFragment{
    private AutoListView mAutoListView;
    private SeeWorldAdapter mSeeWorldAdapter;
    private TitanicTextView mTitanicTextView;
    public SeeWorld() {
        mTitle = "看世界";
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.reflash_list_layout, null);
        init(view);
        return view;
    }

    private void init(View layout)
    {
        mTitanicTextView = (TitanicTextView)layout.findViewById(R.id.titanic_text);
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
//        mAutoListView.setAdapter(mSeeWorldAdapter);
//        mSeeWorldAdapter.notifyDataSetChanged();
        mAutoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getCatalogList();
        loading();
    }

    private void loading()
    {
        mAutoListView.setVisibility(View.GONE);
        showLoading(mTitanicTextView);
    }

    private void loadingComplete()
    {
        mAutoListView.setVisibility(View.VISIBLE);
        hideLoading(mTitanicTextView);
    }

    public void getCatalogList()
    {
        RequestHandle handle = mAsyncHttpClient.get(HttpUtils.RequestUrl.URL_WORLD_CATALOG, new BaseJsonHttpResponseHandler()
        {
            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, Object arg4)
            {
                loadingComplete();
                Toast.makeText(mBaseActivity,"获取数据列表失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2, Object arg3)
            {
                if(!Util.isEmpty(arg2))
                {
                    Gson gson = new Gson();
                    BaseCatalogPojo pojo = gson.fromJson(arg2, BaseCatalogPojo.class);
                    if(pojo.getStatus().equals("success"))
                    {
                        Debug.d("json = " + arg2);
                        mSeeWorldAdapter.setList(pojo.getMessage());
                        mAutoListView.setAdapter(mSeeWorldAdapter);
                        mSeeWorldAdapter.notifyDataSetChanged();
                        loadingComplete();
                    }
                }
            }

            @Override
            protected Object parseResponse(String arg0, boolean arg1) throws Throwable
            {
                return arg0;
            }
        });
    }
}
