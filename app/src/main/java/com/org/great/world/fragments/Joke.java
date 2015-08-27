package com.org.great.world.fragments;

import com.org.great.world.Utils.HttpUtils;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class Joke extends SeeWorld{
    public Joke() {
        mTitle = "笑一校";
        mCatalogUrl = HttpUtils.RequestUrl.URL_JOKE_CATALOG;
    }
}
