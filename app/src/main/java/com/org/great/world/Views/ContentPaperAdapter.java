package com.org.great.world.Views;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.org.great.world.fragments.ChildBaseFragment;

import java.util.List;

/**
 * Created by dj on 2015/7/18.
 * email:dingjun0225@gmail.com
 */
public class ContentPaperAdapter extends FragmentPagerAdapter {
    private List<ChildBaseFragment> mList;
    public ContentPaperAdapter(FragmentManager fm, List<ChildBaseFragment> list) {
        super(fm);
        mList = list;
    }

    public Fragment getItem(int position) {
        return mList.get(position);
    }

    public int getCount() {
        return mList.size();
    }
}
