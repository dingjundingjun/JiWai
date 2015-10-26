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
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.AutoListView;
import com.org.great.world.activities.PlayerActivity;
import com.org.great.world.adapters.VideoAdapter;
import com.org.great.world.data.VideoBasePojo;
import com.org.great.world.data.VideoPojo;
import com.org.great.wrold.R;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by dj on 2015/9/7.
 * email:dingjun0225@gmail.com
 */
public class Video extends SeeWorldAndJokeChildBaseFragment{
    private final int START_GAME_ACTIVITY_REQUESTCODE = 1;
    private VideoAdapter mVideoAdapter;
    private ArrayList<VideoPojo> mVideoPojoList;
    private int mPageIndex = 0;
    private int mTotalCount = 0;
    private int mOnePageCount = 0;
    public Video() {
        mTitle = "搞笑视频";
        mPageIndex = 1;
        mOnePageCount = 10;
		mCatalogUrl = "https://api.youku.com/quality/video/by/category.json?client_id=7960cf1cd1ea53a4&cate=2&"
				+ "page=" + mPageIndex + "&count=" + mOnePageCount;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Debug.d("Game onCreateView");
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReloadBtn.setVisibility(View.GONE);
                getCatalogList();
                loading();
            }
        });
        mVideoAdapter = new VideoAdapter(getActivity());
        
        getCatalogList();
        loading();
        mAutoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                Intent intent = new Intent(mBaseActivity,PlayerActivity.class);
                intent.putExtra("vid", mVideoPojoList.get(position).getId());
                startActivityForResult(intent, START_GAME_ACTIVITY_REQUESTCODE);
            }
        });
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
                loadMore();
                
            }
        });
        return view;
    }

    public void loadMore()
    {
    	if(mPageIndex > 10)
    	{
    		mAutoListView.noLoadDate();
    		return;
    	}
    	mPageIndex++;
		mCatalogUrl = "https://api.youku.com/quality/video/by/category.json?client_id=7960cf1cd1ea53a4&cate=2&"
				+ "page=" + mPageIndex + "&count=" + mOnePageCount;
		getCatalogList();
    }
    
    public void getCatalogList()
    {
        if(mCatalogUrl.isEmpty())
        {
            return;
        }
        Debug.d("AsyncHttpClient = " + mAsyncHttpClient);
        RequestHandle handle = mAsyncHttpClient.get(mCatalogUrl, new BaseJsonHttpResponseHandler()
        {
            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, Object arg4)
            {
                loadingFailed();
//                Toast.makeText(mBaseActivity, mBaseActivity.getResources().getString(R.string.get_list_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2, Object arg3)
            {
                if(!Util.isEmpty(arg2))
                {
                    Debug.d("arg2 = " + arg2);
                    Gson gson = new Gson();
                    VideoBasePojo pojo = gson.fromJson(arg2, VideoBasePojo.class);
                    {
                        Debug.d("json = " + arg2);
                        ArrayList<VideoPojo> videoList = pojo.getVideos();
                        if(videoList != null && !videoList.isEmpty())
                        {
                        	if(mVideoPojoList == null)
                        	{
                        		mVideoPojoList = videoList;
                        	}
                        	else
                        	{
                        		mVideoPojoList.addAll(videoList);
                        	}
	                        mVideoAdapter.setList(mVideoPojoList);
	                        mAutoListView.setAdapter(mVideoAdapter);
	                        mVideoAdapter.notifyDataSetChanged();
	                        if(mPageIndex > 1)
	                        {
	                        	mAutoListView.setSelection(mVideoPojoList.size()-1);
	                        }
	                        loadingComplete();
                        }
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
	protected void getDataFromLocalOrServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCatalogListFromLocal() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveJson(String json) {
		// TODO Auto-generated method stub
		
	}
}
