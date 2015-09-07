package com.org.great.world.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.great.world.Utils.Debug;
import com.org.great.wrold.R;
import com.umeng.socialize.bean.UMComment;

import java.util.List;

/**
 * Created by dj on 2015/8/12.
 * email:dingjun0225@gmail.com
 */
public class CommentAdapter extends BAdapter
{
    private List<UMComment> mUMCommentList;
    public CommentAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public int getCount() {
        if(mUMCommentList != null)
        {
            Debug.d("mUMCommentList.size() = " + mUMCommentList.size());
            return mUMCommentList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        Debug.d("mUMCommentList.position = " + position);
        return mUMCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Debug.d("getView position = " + position + " size = " + mUMCommentList.size());
        if(convertView != null) {

            viewHolder = (ViewHolder) convertView.getTag();

        } else {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.comment_layout_item, null);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        }

        UMComment umData = (UMComment) getItem(position);
        viewHolder.content.setText(umData.mText);
//        viewHolder.date.setText(DateUtils.formatDateTime(mContext, umData.mDt, DateUtils.FORMAT_24HOUR));
        viewHolder.date.setText(DateFormat.format("yyyy-MM-dd kk:mm", umData.mDt).toString());
        viewHolder.name.setText(umData.mUname);
        return convertView;
    }

    static class ViewHolder
    {
        TextView content;
        TextView date;
        TextView name;
    }

    public void setList(List<UMComment> list)
    {
        mUMCommentList = list;
    }
}
