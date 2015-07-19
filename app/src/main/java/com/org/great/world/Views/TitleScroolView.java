package com.org.great.world.Views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.org.great.world.Utils.Debug;
import com.org.great.wrold.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class TitleScroolView extends LinearLayout{
    private Context mContext;
    private View mParentView;
    private LinearLayout mContentLayout;
    private List<String> mTitleList;
    private List<TextView> mTitleViewList = new ArrayList<TextView>();
    public TitleScroolView(Context context) {
        super(context);
        mContext = context;
    }

    public TitleScroolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void init(){
        mParentView = View.inflate(mContext, R.layout.title_scroll_layout, null);
        mContentLayout = (LinearLayout)mParentView.findViewById(R.id.content_layout);
        initTitle();
        this.addView(mParentView);
    }

    public void setTitleList(List<String> list)
    {
        mTitleList = list;

    }

    private void initTitle()
    {
        if(mTitleList == null || mTitleList.size() <= 0)
        {
            return;
        }
        mTitleViewList.clear();
        int len = mTitleList.size();
        Debug.d("mTitleList.size() = " + mTitleList.size());
        for(int i = 0; i < len;i++)
        {
            Debug.d("title = " + mTitleList.get(i));
            TextView textView = new TextView(mContext);
            textView.setText(mTitleList.get(i));
            textView.setBackgroundColor(Color.RED);
            textView.setMinWidth(200);
            textView.setGravity(Gravity.CENTER);
            textView.setId(i);
            mContentLayout.addView(textView);
            mTitleViewList.add(textView);
        }
    }


}
