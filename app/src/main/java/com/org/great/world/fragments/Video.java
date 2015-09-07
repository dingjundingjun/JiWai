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
import com.org.great.world.activities.GameActivity;
import com.org.great.world.adapters.GameAdapter;
import com.org.great.world.adapters.VideoAdapter;
import com.org.great.world.data.BaseGamePojo;
import com.org.great.world.data.GamePojo;
import com.org.great.world.data.VideoBasePojo;
import com.org.great.world.data.VideoPojo;
import com.org.great.wrold.R;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by dj on 2015/9/7.
 * email:dingjun0225@gmail.com
 */
public class Video extends SeeWorldChildBaseFragment{
    private final int START_GAME_ACTIVITY_REQUESTCODE = 1;
    private VideoAdapter mVideoAdapter;
    private ArrayList<VideoPojo> mVideoPojoList;
    public Video() {
        mTitle = "搞笑视频";
        mCatalogUrl = "https://api.youku.com/quality/video/by/category.json?client_id=7960cf1cd1ea53a4&cate=2&page=1&count=20";
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
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("list", mGamePojoList);
//                bundle.putInt("index_id",position);
//                Intent intent = new Intent(mBaseActivity, GameActivity.class);
//                intent.putExtra("bundle",bundle);
//                startActivityForResult(intent,START_GAME_ACTIVITY_REQUESTCODE);
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
        Debug.d("AsyncHttpClient = " + mAsyncHttpClient);
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
                    Debug.d("arg2 = " + arg2);
                    Gson gson = new Gson();
                    VideoBasePojo pojo = gson.fromJson(arg2, VideoBasePojo.class);
                    {
                        Debug.d("json = " + arg2);
                        mVideoPojoList = pojo.getVideos();
                        mVideoAdapter.setList(mVideoPojoList);
                        mAutoListView.setAdapter(mVideoAdapter);
                        mVideoAdapter.notifyDataSetChanged();
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
