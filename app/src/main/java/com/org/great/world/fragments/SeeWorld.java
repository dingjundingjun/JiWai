package com.org.great.world.fragments;

import com.org.great.world.Utils.HttpUtils;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class SeeWorld extends SeeWorldChildBaseFragment{
    public SeeWorld() {
        mTitle = "看世界";
        mCatalogUrl = HttpUtils.RequestUrl.URL_WORLD_CATALOG;
    }
}
