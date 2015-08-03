package com.org.great.world.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.great.wrold.R;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class Joke extends ChildBaseFragment {
    public Joke() {
        mTitle = "笑一校";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.see_world_layout, null);
        TextView textView = (TextView)view.findViewById(R.id.text);
        textView.setText("Joke");

        return view;
    }
}
