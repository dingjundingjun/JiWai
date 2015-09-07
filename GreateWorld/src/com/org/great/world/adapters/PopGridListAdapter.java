package com.org.great.world.adapters;



import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.org.great.wrold.R;


public class PopGridListAdapter extends BaseAdapter {

    private Context mContext;
    private String mCurGradeString;
    /** 点中状态的图片 */
    private int itemClickDrawable = R.drawable.item_click;
    /** 点中状态下的文本颜色 */
    private int itemClickFontColor = Color.GRAY;
    /** 当前选中项 */
    private int currentSelected = 0;

    /** 字体大小 */
    final int nFontSize = 22;
    /** 每一个View的高和宽 */
    private LayoutParams itemParams = new LayoutParams(LayoutParams.MATCH_PARENT, 50);

    private final static String [] GRADE_STRINGS = {"幼儿园呢", "一年级", "二年级", "三年级", "四年级", "五年级", "六年级", "七年级", "八年级", "九年级", "高一", "高二", "高三","大学生了"};

    public PopGridListAdapter( Context context, String gradeStr ) {

        mContext = context;
        
        mCurGradeString = gradeStr;
    }

    /**
     * 设置点中状态下的图片
     * @param
     */
    public void setItemClickDrawable(int itemClickDrawable) {
        this.itemClickDrawable = itemClickDrawable;
    }


    /**
     * 设置点中状态下的文本颜色
     * @param itemClickFontColor the itemClickFontColor to set
     */
    public void setItemClickFontColor(int itemClickFontColor) {
        this.itemClickFontColor = itemClickFontColor;
    }

    public void setItemLayoutParam(LayoutParams itemParams) {
        this.itemParams  = itemParams;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return GRADE_STRINGS.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return GRADE_STRINGS[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     * 设置当前的年级
     * @param
     */
    public void setCurrentGrade(int position) {
        if (position < 0 || position >= GRADE_STRINGS.length) {
            return;
        }

        this.mCurGradeString = GRADE_STRINGS[position];
        this.notifyDataSetChanged();
    }

    /**
     * @return the currentSelected
     */
    public int getCurrentSelectedPosition() {
        return currentSelected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = new TextView( mContext );
        ((TextView)convertView).setLayoutParams(itemParams);
        ((TextView)convertView).setTextSize(nFontSize);
        ((TextView)convertView).setTextColor(Color.WHITE);
        ((TextView)convertView).setGravity(Gravity.CENTER);
        ((TextView)convertView).setText(GRADE_STRINGS[position]);

        if( mCurGradeString.equals(GRADE_STRINGS[position])) {
            currentSelected = position;
            ((TextView)convertView).setBackgroundResource(itemClickDrawable);
            ((TextView)convertView).setTextColor(itemClickFontColor);
        } else {
            ((TextView)convertView).setBackgroundResource(android.R.color.transparent);
            ((TextView)convertView).setTextColor(Color.GRAY);
        }
        return convertView;
    }
}




