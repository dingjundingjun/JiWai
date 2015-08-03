package com.org.great.world.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.HttpUtils;
import com.org.great.world.Utils.Util;
import com.org.great.world.Views.Titanic;
import com.org.great.world.Views.TitanicTextView;
import com.org.great.world.data.BaseCatalogPojo;

import org.apache.http.Header;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class ChildBaseFragment extends Fragment {
    public Activity mBaseActivity;
    public String mTitle = "";
    public AsyncHttpClient mAsyncHttpClient;
    private Titanic mTitanic;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mBaseActivity = getActivity();
        super.onCreate(savedInstanceState);
        mAsyncHttpClient = new AsyncHttpClient();
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



}
