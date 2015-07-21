package com.org.great.world.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.org.great.world.Views.ContentPaperAdapter;
import com.org.great.world.Views.FragmentViewPaper;
import com.org.great.world.Views.TitleScroolView;
import com.org.great.wrold.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 2015/7/17.
 * email:dingjun0225@gmail.com
 */
public class BaseFragment extends Fragment {
    private View mParentView;
    private Activity mBaseActivity;
    public FragmentViewPaper mFragmentViewPaper;
    public List<ChildBaseFragment> mFragmentList = new ArrayList<ChildBaseFragment>();
    public List<String> mTitleListStr = new ArrayList<String>();
    private TitleScroolView mTitleScroolView;
    public int mCurrentIndex = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseActivity = getActivity();
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.parent_fragment_content, null);
        mParentView = view;
        return mParentView;
    }

    public void updateFragmentList()
    {
        init();
    }

    private void init()
    {
        mTitleScroolView = (TitleScroolView)mParentView.findViewById(R.id.titlescrool);
        mTitleScroolView.setTitleList(mTitleListStr);
        mTitleScroolView.init();
        mTitleScroolView.setOnTitleClickListener(new TitleScroolView.OnTitleClickListener() {
            @Override
            public void onClick(int id) {
                mFragmentViewPaper.setCurrentItem(id);
            }
        });
        mFragmentViewPaper = (FragmentViewPaper)mParentView.findViewById(R.id.viewpager);
        mFragmentViewPaper.setAdapter(new ContentPaperAdapter(getChildFragmentManager(), mFragmentList));
        mFragmentViewPaper.setCurrentItem(0);
        mFragmentViewPaper.setOnPageChangeListener(new BaseOnPageChangeListener());
    }

    public class BaseOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
//            Animation animation = new TranslateAnimation(currIndex*one,arg0*one,0,0);//平移动画
//            currIndex = arg0;
//            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
//            animation.setDuration(200);//动画持续时间0.2秒
//            image.startAnimation(animation);//是用ImageView来显示动画的
//            int i = currIndex + 1;
            mCurrentIndex = arg0;
//            Toast.makeText(mBaseActivity, "您选择了第"+mCurrentIndex+"个页卡", Toast.LENGTH_SHORT).show();
        }
    }
}
