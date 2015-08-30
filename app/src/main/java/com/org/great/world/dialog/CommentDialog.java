package com.org.great.world.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.org.great.world.Utils.PersonalUtil;
import com.org.great.world.data.PersonalInfoPojo;
import com.org.great.wrold.R;
import com.umeng.socialize.bean.Gender;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;

/**
 * Created by Administrator on 2015/8/30.
 */
public class CommentDialog extends Dialog implements View.OnClickListener
{
    private EditText mCommentEdit;
    private Button mCommentBtn;
    private CommentListener mCommentlistener;
    private Context mContext;
    private UMSocialService mSocialService;
    private String mCommentStr;
    public CommentDialog(Context context, int theme) {
        super(context, theme);
    }

    public CommentDialog(Context context) {
        super(context);
        mContext = context;
    }

    public void setSocialService(UMSocialService ums)
    {
        mSocialService = ums;
    }

    public void setCommentListener(CommentListener cl)
    {
        mCommentlistener = cl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.commnet_dialog_layout);
        init();
    }

    private void init()
    {
        mCommentEdit = (EditText)this.findViewById(R.id.comment_edit);
        mCommentBtn = (Button)this.findViewById(R.id.comment_btn);
        mCommentBtn.setOnClickListener(this);
    }

    public void clear()
    {
        mCommentEdit.setText("");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.comment_btn:
            {
                if(mCommentEdit.getText().toString() != null && !mCommentEdit.getText().toString().equals(""))
                {
                    mCommentStr = mCommentEdit.getText().toString();
                    getUserInfo();
                }
                else
                {
                    Toast.makeText(mContext,R.string.comment_can_not_null,Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public interface CommentListener
    {
        public void OnCommentComplete();
    }

    private void getUserInfo() {

        PersonalInfoPojo perInfoPojo = PersonalUtil.getPersonInfo(mContext);
        SnsAccount snsAccount = new SnsAccount("哈哈", Gender.MALE, perInfoPojo.getPhotoPath(),perInfoPojo.getAccountId().toString());

        mSocialService.login(mContext, snsAccount, new SocializeListeners.SocializeClientListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onComplete(int arg0, SocializeEntity arg1) {
                postComment();
            }
        });
    }

    private void postComment() {

        PersonalInfoPojo perInfoPojo = PersonalUtil.getPersonInfo(mContext);
        final UMComment socializeComment = new UMComment();

        socializeComment.mUid = perInfoPojo.getLoginName();
        socializeComment.mUname = perInfoPojo.getNickName();
        socializeComment.mUserIcon = perInfoPojo.getPhotoPath();
        socializeComment.mText = mCommentStr;

        if (TextUtils.isEmpty(socializeComment.mText)) {

            Toast.makeText(mContext, "童鞋，你想说点啥？", Toast.LENGTH_SHORT).show();
            return;
        }

        mSocialService.postComment(mContext, socializeComment,
                new SocializeListeners.MulStatusListener() {
                    @Override
                    public void onStart() {
//                        mTextView.setText("发送中...");
                    }

                    @Override
                    public void onComplete(MultiStatus multiStatus, int status,
                                           SocializeEntity entity) {
                        if (status == 200) {
                            Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                            mCommentlistener.OnCommentComplete();
                            CommentDialog.this.dismiss();
                        } else {
                            Toast.makeText(mContext, "评论失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE);

    }
}
