package com.org.great.world.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.HttpUtils;
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.AutoListView;
import com.org.great.world.Views.AutoListView.OnRefreshListener;
import com.org.great.world.Views.Titanic;
import com.org.great.world.Views.TitanicTextView;
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
public abstract class SeeWorldAndJokeChildBaseFragment extends Fragment{
    public ArrayList<CatalogPojo> mCatalogPojo;
    public SeeWorldAdapter mSeeWorldAdapter;
    public Activity mBaseActivity;
    public String mTitle = "";
    public AsyncHttpClient mAsyncHttpClient;
    private Titanic mTitanic;
    public AutoListView mAutoListView;
    private TitanicTextView mTitanicTextView;
    public RelativeLayout mMainLayout;
    protected String mCatalogUrl;
    public Button mReloadBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Debug.d("SeeWorldChildBaseFragment onCreate");
        mBaseActivity = getActivity();
        mAsyncHttpClient = new AsyncHttpClient();
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.reflash_list_layout, null);
        init(view);
        return view;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void showLoading(TitanicTextView view)
    {
        if(mTitanic == null)
        {
            mTitanic = new Titanic();
        }
        mTitanic.start(view);
        view.setVisibility(View.VISIBLE);
    }

    public void hideLoading(TitanicTextView view)
    {
        if(mTitanic == null)
        {
            return;
        }
        mTitanic.cancel();
        view.setVisibility(View.GONE);
    }

    private void init(View layout)
    {
        mTitanicTextView = (TitanicTextView)layout.findViewById(R.id.titanic_text);
        mReloadBtn = (Button)layout.findViewById(R.id.reload);
        mAutoListView = (AutoListView)layout.findViewById(R.id.auto_list);
        mMainLayout = (RelativeLayout)layout.findViewById(R.id.main_layout);
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
                startActivity(intent);
            }
        });
        
        mAutoListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getCatalogListFromServer();
			}
		});
    }

    
    
    public void loading()
    {
        mAutoListView.setVisibility(View.GONE);
        showLoading(mTitanicTextView);
    }

    public void loadingComplete()
    {
    	Debug.d("loadingComplete");
        mAutoListView.setVisibility(View.VISIBLE);
        mAutoListView.onLoadComplete();
        mAutoListView.onRefreshComplete();
        hideLoading(mTitanicTextView);
        if(mReloadBtn.isShown())
        {
            mReloadBtn.setVisibility(View.GONE);
        }
    }

    public void loadingFailed()
    {
        hideLoading(mTitanicTextView);
        mReloadBtn.setVisibility(View.VISIBLE);
    }
    
    public void getCatalogListFromServer()
    {
        if(mCatalogUrl.isEmpty())
        {
            return;
        }
        Debug.d("catalogUrl = " + mCatalogUrl);
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
	                        		Toast.makeText(mBaseActivity, R.string.there_is_no_new_catalog_seeworld, Toast.LENGTH_SHORT).show();
	                        	}
	                        	else
	                        	{
	                        		mCatalogPojo = pojo.getMessage();
	                                mSeeWorldAdapter.setList(mCatalogPojo);
	                                mAutoListView.setAdapter(mSeeWorldAdapter);
	                                mSeeWorldAdapter.notifyDataSetChanged();
	                        	}
	                        	Util.saveFreshTime(mBaseActivity);
	                        	saveJson(arg2);
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
    
    public void notifiCatalogRefresh(String json)
    {
    	Gson gson = new Gson();
        BaseCatalogPojo pojo = gson.fromJson(json, BaseCatalogPojo.class);
        if(pojo.getStatus().equals("success"))
        {
//            Debug.d("json = " + json);
            ArrayList<CatalogPojo> tempList = pojo.getMessage();
            if(tempList != null )
            {
        		mCatalogPojo = pojo.getMessage();
                mSeeWorldAdapter.setList(mCatalogPojo);
                mAutoListView.setAdapter(mSeeWorldAdapter);
                mSeeWorldAdapter.notifyDataSetChanged();
            }
            loadingComplete();
            mAutoListView.noLoadDate();
        }
    }
    
    abstract protected  void getDataFromLocalOrServer();
    abstract public void getCatalogListFromLocal();
    abstract protected void saveJson(String json);
}
