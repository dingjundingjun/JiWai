package com.org.great.world.fragments;

import com.org.great.world.Utils.Debug;
import com.org.great.world.Utils.HttpUtils;
import com.org.great.world.Utils.Util;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class SeeWorld extends SeeWorldAndJokeChildBaseFragment
{
    public SeeWorld() {
        mTitle = "看世界";
        mCatalogUrl = HttpUtils.RequestUrl.URL_WORLD_CATALOG;
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
    	notifiCatalogRefresh(json);
    }

	@Override
	protected void saveJson(String json) {
		Util.saveSeeWorldJson(mBaseActivity, json);
	}
    
    
}
