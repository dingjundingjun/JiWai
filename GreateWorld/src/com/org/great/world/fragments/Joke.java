package com.org.great.world.fragments;

import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.HttpUtils;
import com.org.great.world.Utils.Util;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class Joke extends SeeWorldAndJokeChildBaseFragment{
    public Joke() {
        mTitle = "笑一校";
        mCatalogUrl = HttpUtils.RequestUrl.URL_JOKE_CATALOG;
    }

	@Override
	protected void getDataFromLocalOrServer() {
		if(Util.getJokeJson(mBaseActivity) == null)
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

	@Override
	protected void saveJson(String json) {
		Util.saveJokeJson(mBaseActivity, json);
	}

	@Override
	public void getCatalogListFromLocal() {
		String json = Util.getJokeJson(mBaseActivity);
    	notifiCatalogRefresh(json);
	}
}
