package com.org.great.world.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.HttpUtils;
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.AutoListView.OnRefreshListener;
import com.org.great.world.activities.GameActivity;
import com.org.great.world.activities.SeeWorldActivity;
import com.org.great.world.adapters.GameAdapter;
import com.org.great.world.adapters.SeeWorldAdapter;
import com.org.great.world.data.AllAD;
import com.org.great.world.data.BaseCatalogPojo;
import com.org.great.world.data.BaseGamePojo;
import com.org.great.world.data.BasePojo;
import com.org.great.world.data.CardBaseProjo;
import com.org.great.world.data.CardType;
import com.org.great.world.data.CatalogPojo;
import com.org.great.world.data.GamePojo;
import com.org.great.world.data.VideoPojo;
import com.org.great.wrold.R;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class Game extends SeeWorldAndJokeChildBaseFragment
{
	private TextView mTimeLastView;
    private GameAdapter mGameAdapter;
    private ArrayList<GamePojo> mGamePojos;
    private UpdateHandle mUpdateHandle = new UpdateHandle();
    public Game() {
        mTitle = "益智游戏";
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
    	View view = super.onCreateView(inflater, container, savedInstanceState);
        mAutoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	if(Util.isCanPlaygame(mBaseActivity))
            	{
	                position--;
	                Bundle bundle = new Bundle();
	                bundle.putSerializable("list", mGamePojos);
	                bundle.putInt("index_id",position);
	                Intent intent = new Intent(mBaseActivity, GameActivity.class);
	                intent.putExtra("bundle",bundle);
	                startActivity(intent);
            	}
            	else
            	{
            		Toast.makeText(mBaseActivity, "如果点击广告，可以获得30分钟的游戏时间哦", Toast.LENGTH_SHORT).show();
            	}
            }
        });
        
        mAutoListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getGameUrl();
			}
		});
        
        
        return view;
    }
    
    
    @Override
	public void onResume() {
		super.onResume();
		if(mUpdateHandle != null)
		{
			mUpdateHandle.sendEmptyMessageDelayed(1, 1000);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBaseActivity = getActivity();
		mGameAdapter = new GameAdapter(mBaseActivity);
	}

    public void getDataFromLocalOrServer()
    {
    	if(Util.getGameJson(mBaseActivity) == null)
    	{
    		Debug.d("getDataFromServer because the local json is null");
//    		getGameCatalogListFromServer();
    		getGameUrl();
    	}
    	else
    	{
    		if(Util.isCanFresh(mBaseActivity))
    		{
    			Debug.d("getDataFromServer because the time is over 6 hour");
    			getGameUrl();
//    			getGameCatalogListFromServer();
    		}
    		else
    		{
    			Debug.d("getCatalogListFromLocal");
    			getCatalogListFromLocal();
    		}
    	}
    }
    
    public void getGameCatalogListFromServer()
    {
    	Debug.d("mCatalogUrl = " + mCatalogUrl);
        if(mCatalogUrl == null || mCatalogUrl.isEmpty())
        {
            return;
        }
	        RequestHandle handle = mAsyncHttpClient.get(mCatalogUrl, new BaseJsonHttpResponseHandler()
	        {
	            @Override
	            public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, Object arg4)
	            {
	                loadingFailed();
	                mAutoListView.setVisibility(View.GONE);
	                Toast.makeText(mBaseActivity, mBaseActivity.getResources().getString(R.string.get_list_failed), Toast.LENGTH_SHORT).show();
	            }
	
	            @Override
	            public void onSuccess(int arg0, Header[] arg1, String arg2, Object arg3)
	            {
	                if(!Util.isEmpty(arg2))
	                {
	                    Gson gson = new Gson();
	                    BaseGamePojo pojo = gson.fromJson(arg2, BaseGamePojo.class);
	                    if(pojo.getStatus().equals("success"))
	                    {
	                        Debug.d("json = " + arg2);
	                        ArrayList<GamePojo> tempList = pojo.getMessage();
	                        if(tempList != null )
	                        {
	                        	if(mGamePojos != null && tempList.size() == mGamePojos.size())
	                        	{
	                        		Debug.d("there is no new news ");
	                        		Toast.makeText(mBaseActivity, R.string.there_is_no_new_catalog_seeworld, Toast.LENGTH_SHORT).show();
	                        	}
	                        	else
	                        	{
	                        		mGamePojos = pojo.getMessage();
	                        		mGameAdapter.setList(mGamePojos);
	                                mAutoListView.setAdapter(mGameAdapter);
	                                mGameAdapter.notifyDataSetChanged();
	                        	}
	                        	Util.saveFreshTime(mBaseActivity);
	                        	saveJson(arg2);
	                        }
	                        loadingComplete();
	                        mAutoListView.noLoadDate();
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
    
    public void getCatalogListFromLocal()
    {
    	String json = Util.getGameJson(mBaseActivity);
    	notifiGameCatalogRefresh(json);
    }
    
    private void notifiGameCatalogRefresh(String json)
    {
    	Gson gson = new Gson();
        BaseGamePojo pojo = gson.fromJson(json, BaseGamePojo.class);
        if(pojo.getStatus().equals("success"))
        {
            Debug.d("json = " + json);
            ArrayList<GamePojo> tempList = pojo.getMessage();
            if(tempList != null )
            {
        		mGamePojos = pojo.getMessage();
                mGameAdapter.setList(mGamePojos);
                mAutoListView.setAdapter(mGameAdapter);
                mGameAdapter.notifyDataSetChanged();
            }
            loadingComplete();
            mAutoListView.noLoadDate();
        }
    }

	@Override
	protected void saveJson(String json) {
		Util.saveGameJson(mBaseActivity, json);
	}
	
    private void getGameUrl()
    {
    	RequestHandle handle = mAsyncHttpClient.get(HttpUtils.RequestUrl.URL_TYPE_LIST, new BaseJsonHttpResponseHandler()
        {
            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, Object arg4)
            {
                Toast.makeText(mBaseActivity, mBaseActivity.getResources().getString(R.string.get_list_failed), Toast.LENGTH_SHORT).show();
                mAutoListView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2, Object arg3)
            {
                if(!Util.isEmpty(arg2))
                {
                    Gson gson = new Gson();
                    CardBaseProjo pojo = gson.fromJson(arg2, CardBaseProjo.class);
                    if(pojo.getStatus().equals("success"))
                    {
                        Debug.d("json = " + arg2);
                        ArrayList<CardType> tempList = pojo.getMessage();
                        if(tempList != null )
                        {
                        	for(int i = 0; i < tempList.size();i++)
                        	{
                        		if(tempList.get(i).getTypeName().equals("游戏"))
                        		{
                        			mCatalogUrl = "http://121.40.93.89:13090/card/getCardListByType?typeId=" + tempList.get(i).getCardTypeId();
                        			Debug.d("game catalog url = " + mCatalogUrl);
                        			getGameCatalogListFromServer();
                        			break;
                        		}
                        	}
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
    
    private void updateTimeLast()
    {
    	if(mTimeLastView == null)
		{
			mTimeLastView = new TextView(mBaseActivity);
			mTimeLastView.setTextColor(Color.RED);
			android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			mMainLayout.addView(mTimeLastView, params);
		}
    	if(AllAD.bShowAD == false)
    	{
    		mTimeLastView.setVisibility(View.GONE);
    	}
    	else
    	{
    		mTimeLastView.setVisibility(View.VISIBLE);
    	}
    	if(Util.isCanPlaygame(mBaseActivity))
    	{
    		String lastTimeStr = (String)Util.getPlayGameTime(mBaseActivity);
    		long lastTime = Long.decode(lastTimeStr);
    		long last = 30*60 - (System.currentTimeMillis()/1000 - lastTime);
    		int minute = ((int)last/60);
    		int second = (int)last%60;
    		Debug.d("minute = " + minute + " second = " + second);
    		if(last <= 0)
    		{
    			mTimeLastView.setText("点击广告将获得游戏时间！");
    			return;
    		}
    		Debug.d("minute = " + minute + " second = " + second);
    		if(minute > 0)
    		{
    			mTimeLastView.setText(minute + "分"+second + "秒");
    		}
    		else
    		{
    			mTimeLastView.setText(second + "秒");
    		}
    	}
    	else
    	{
    		if(AllAD.bShowAD == true)
    		{
    			mTimeLastView.setText("点击广告将获得游戏时间！");
    		}
    	}
    }
    
    public class UpdateHandle extends Handler
    {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
			{
			case 1:
			{
				updateTimeLast();
				sendEmptyMessageDelayed(1, 1000);
				break;
			}
			}
		}
    	
    }
}
