package com.org.great.world.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.HttpUtils;
import com.org.great.world.Utils.Util;
import com.org.great.world.activities.SeeWorldActivity;
import com.org.great.world.adapters.SeeWorldAdapter;
import com.org.great.world.data.BaseCatalogPojo;
import com.org.great.world.data.CatalogPojo;
import com.org.great.wrold.R;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class SeeWorld extends SeeWorldChildBaseFragment{
    private final int START_SEEWORLD_ACTIVITY_REQUESTCODE = 1;
    private ArrayList<CatalogPojo> mCatalogPojo;
    private SeeWorldAdapter mSeeWorldAdapter;
    public SeeWorld() {
        mTitle = "看世界";
        mCatalogUrl = HttpUtils.RequestUrl.URL_WORLD_CATALOG;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Debug.d("SeeWorld onCreateView");
        View view = super.onCreateView(inflater,container,savedInstanceState);
        mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReloadBtn.setVisibility(View.GONE);
                getCatalogList();
                loading();
            }
        });
        mSeeWorldAdapter = new SeeWorldAdapter(getActivity());
        getCatalogList();
        loading();
        mAutoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", mCatalogPojo);
                bundle.putInt("index_id",position);
                Intent intent = new Intent(mBaseActivity, SeeWorldActivity.class);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,START_SEEWORLD_ACTIVITY_REQUESTCODE);
            }
        });
        return view;
    }

    public void getCatalogList()
    {
        if(mCatalogUrl.isEmpty())
        {
            return;
        }
        RequestHandle handle = mAsyncHttpClient.get(mCatalogUrl, new BaseJsonHttpResponseHandler()
        {
            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, Object arg4)
            {
                loadingFailed();
                Toast.makeText(mBaseActivity, mBaseActivity.getResources().getString(R.string.get_list_failed), Toast.LENGTH_SHORT).show();
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
}
