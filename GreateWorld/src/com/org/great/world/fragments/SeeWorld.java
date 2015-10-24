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
import com.org.great.world.Views.AutoListView.OnRefreshListener;
import com.org.great.world.activities.SeeWorldActivity;
import com.org.great.world.adapters.SeeWorldAdapter;
import com.org.great.world.data.BaseCatalogPojo;
import com.org.great.world.data.CatalogPojo;
import com.org.great.wrold.R;
import com.youku.uplayer.OnRealVideoStartListener;

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
                getCatalogListFromServer();
                loading();
            }
        });
        mSeeWorldAdapter = new SeeWorldAdapter(getActivity());
        loading();
        getDataFromLocalOrServer();
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
        
        mAutoListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getCatalogListFromServer();
			}
		});
        return view;
    }

    public void getDataFromLocalOrServer()
    {
    	if(Util.getSeeWorldJson(mBaseActivity) == null)
    	{
    		Debug.d("getDataFromServer because the local json is null");
    		getCatalogListFromServer();
    	}
    	else
    	{
    		if(Util.isCanFresh(mBaseActivity))
    		{
    			Debug.d("getDataFromServer because the time is over 6 hour");
    			getCatalogListFromServer();
    		}
    		else
    		{
    			Debug.d("getCatalogListFromLocal");
    			getCatalogListFromLocal();
    		}
    	}
    }
    
    public void getCatalogListFromLocal()
    {
    	String json = Util.getSeeWorldJson(mBaseActivity);
    	Gson gson = new Gson();
        BaseCatalogPojo pojo = gson.fromJson(json, BaseCatalogPojo.class);
        if(pojo.getStatus().equals("success"))
        {
            Debug.d("json = " + json);
            ArrayList<CatalogPojo> tempList = pojo.getMessage();
            if(tempList != null )
            {
        		mCatalogPojo = pojo.getMessage();
                mSeeWorldAdapter.setList(mCatalogPojo);
                mAutoListView.setAdapter(mSeeWorldAdapter);
                mSeeWorldAdapter.notifyDataSetChanged();
            }
            loadingComplete();
        }
    }
    
    public void getCatalogListFromServer()
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
	                        ArrayList<CatalogPojo> tempList = pojo.getMessage();
	                        if(tempList != null )
	                        {
	                        	if(mCatalogPojo != null && tempList.size() == mCatalogPojo.size())
	                        	{
	                        		Debug.d("there is no new news ");
	                        		mAutoListView.onRefreshComplete();
	                        		Toast.makeText(mBaseActivity, R.string.there_is_no_new_catalog_seeworld, Toast.LENGTH_SHORT).show();
//	                        		String Str = mBaseActivity.getResources().getString(R.string.there_has_some_seeworld, 2);
//	                        		Toast.makeText(mBaseActivity, Str, Toast.LENGTH_SHORT).show();
	                        	}
	                        	else
	                        	{
	                        		mCatalogPojo = pojo.getMessage();
	                                mSeeWorldAdapter.setList(mCatalogPojo);
	                                mAutoListView.setAdapter(mSeeWorldAdapter);
	                                mSeeWorldAdapter.notifyDataSetChanged();
	                        	}
	                        	Util.saveFreshTime(mBaseActivity);
	                        	Util.saveSeeWorldJson(mBaseActivity, arg2);
	                        }
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
