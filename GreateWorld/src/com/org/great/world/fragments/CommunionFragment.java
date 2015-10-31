package com.org.great.world.fragments;

import com.org.great.wrold.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class CommunionFragment extends Fragment{
	private Activity mBaseActivity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBaseActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = RelativeLayout.inflate(mBaseActivity, R.layout.communion_layout, null);
		return view;
	}
	
	private void init(View view)
	{
		
	}
}
