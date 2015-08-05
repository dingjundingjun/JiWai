package com.org.great.world.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.great.world.Views.ContentPaperAdapter;
import com.org.great.world.Views.FragmentViewPaper;
import com.org.great.world.Views.TitleScroolView;
import com.org.great.wrold.R;

/**
 * Created by dj on 2015/8/4.
 * email:dingjun0225@gmail.com
 */
public class BaseContentFragment extends Fragment {
    private Activity mBaseActivity;
    private View mParentView;
    private String mTitle;
    private String mURL;
    private TextView mTitleTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseActivity = getActivity();
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.seeworld_layout, null);
        mParentView = view;
        init();
        return mParentView;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public void setURL(String url)
    {
        mURL = url;
    }

    private void init()
    {
        mTitleTextView = (TextView)mParentView.findViewById(R.id.title);
    }
}
