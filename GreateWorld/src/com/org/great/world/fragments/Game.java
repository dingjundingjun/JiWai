package com.org.great.world.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.HttpUtils;
import com.org.great.world.Utils.Util;
import com.org.great.world.activities.GameActivity;
import com.org.great.world.activities.SeeWorldActivity;
import com.org.great.world.adapters.GameAdapter;
import com.org.great.world.adapters.SeeWorldAdapter;
import com.org.great.world.data.BaseCatalogPojo;
import com.org.great.world.data.BaseGamePojo;
import com.org.great.world.data.CatalogPojo;
import com.org.great.world.data.GamePojo;
import com.org.great.wrold.R;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class Game extends SeeWorldChildBaseFragment
{
    private final int START_GAME_ACTIVITY_REQUESTCODE = 1;
    private GameAdapter mGameAdapter;
    private ArrayList<GamePojo> mGamePojoList;
    public Game() {
        mTitle = "小游戏";
        mCatalogUrl = HttpUtils.RequestUrl.URL_GAME_CATALOG;
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
        mGameAdapter = new GameAdapter(getActivity());
        getCatalogList();
        loading();
        mAutoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", mGamePojoList);
                bundle.putInt("index_id",position);
                Intent intent = new Intent(mBaseActivity, GameActivity.class);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,START_GAME_ACTIVITY_REQUESTCODE);
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
                    BaseGamePojo pojo = gson.fromJson(arg2, BaseGamePojo.class);
                    if(pojo.getStatus().equals("success"))
                    {
                        Debug.d("json = " + arg2);
                        mGamePojoList = pojo.getMessage();
                        mGameAdapter.setList(mGamePojoList);
                        mAutoListView.setAdapter(mGameAdapter);
                        mGameAdapter.notifyDataSetChanged();
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
