package com.org.great.world.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.org.great.world.activities.SeeWorldActivity;
import com.org.great.world.adapters.SeeWorldAdapter;
import com.org.great.world.data.BaseCatalogPojo;
import com.org.great.world.data.CatalogPojo;
import com.org.great.wrold.R;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class SeeWorld extends ChildBaseFragment implements View.OnClickListener{
    private AutoListView mAutoListView;
    private SeeWorldAdapter mSeeWorldAdapter;
    private TitanicTextView mTitanicTextView;
    private ArrayList<CatalogPojo> mCatalogPojo;
    private final int START_SEEWORLD_ACTIVITY_REQUESTCODE = 1;
    private Button mReloadBtn;
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
        mReloadBtn = (Button)layout.findViewById(R.id.reload);
        mReloadBtn.setOnClickListener(this);
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", mCatalogPojo);
                bundle.putInt("index_id",position);
                Intent intent = new Intent(mBaseActivity, SeeWorldActivity.class);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,START_SEEWORLD_ACTIVITY_REQUESTCODE);
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
        if(mReloadBtn.isShown())
        {
            mReloadBtn.setVisibility(View.GONE);
        }
    }

    private void loadingFailed()
    {
        hideLoading(mTitanicTextView);
        mReloadBtn.setVisibility(View.VISIBLE);
    }

    public void getCatalogList()
    {
        RequestHandle handle = mAsyncHttpClient.get(HttpUtils.RequestUrl.URL_WORLD_CATALOG, new BaseJsonHttpResponseHandler()
        {
            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, Object arg4)
            {
                loadingFailed();
                Toast.makeText(mBaseActivity,mBaseActivity.getResources().getString(R.string.get_list_failed),Toast.LENGTH_SHORT).show();
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
                        mCatalogPojo = pojo.getMessage();
                        mSeeWorldAdapter.setList(mCatalogPojo);
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

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.reload:
            {
                mReloadBtn.setVisibility(View.GONE);
                getCatalogList();
                loading();
                break;
            }
        }
    }
}
