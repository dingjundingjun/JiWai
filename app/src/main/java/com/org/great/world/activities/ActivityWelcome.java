package com.org.great.world.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.org.great.world.Utils.DensityUtil;
import com.org.great.world.Utils.PersonalUtil;
import com.org.great.world.pojo.PersonalInfoPojo;
import com.org.great.wrold.R;
import com.ryanharter.viewpager.PagerAdapter;
import com.ryanharter.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell390 on 2015/8/17.
 */
public class ActivityWelcome extends FragmentActivity implements ViewPager.OnPageChangeListener {

    public final static String WELCOME_CFG = "welcome_cfg";
    public final static String FIRST_IN = "first_in";


    private int mType = 0; /** 0 显示引导界面  1 显示欢迎的图片*/
    private ImageView mWelcomeImg;
    private ViewPager mPager;
    private VerticalFragementPagerAdapter mAdapter;

    private List<View> pagers = new ArrayList<View>();

    private ImageView t1_icon1,t1_icon2, t1_fixed, t1_next;
    private ImageView t2_icon1, t2_fixed, t2_next;
    private ImageView t3_fixed, t3_next, t3_icon2, t3_icon3, t3_icon4, t3_icon5, t3_icon6;

    private RelativeLayout centerLayout;
    private int fx1, fy1, tx1, ty1;
    private int fx2, fy2, tx2, ty2;
    private int fx3, fy3, tx3, ty3;
    private int fx4, fy4, tx4, ty4;

    private ImageView t4_icon1, t4_fixed;
    private Button t4_start;

    private int preIndex = 0;
    private AnimationDrawable t1_icon1_animationDrawable;
    private AnimationDrawable t3_icon6_animationDrawable;

    private Animation animationTop;
    private Animation animationBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mWelcomeImg = (ImageView)findViewById(R.id.welcomebg);
        mPager = (ViewPager) findViewById(R.id.pager);

        initActivity( );
    }



    private void initActivity() {

        if( isFirstInJy()){
            //初始化 viewpager
            saveFistInFlag();
            initViewPager();
        }
        else{
            //显示欢迎界面 image
            initImageView();
        }
    }



    @SuppressWarnings("deprecation")
    private void initImageView() {

        mWelcomeImg.setVisibility(View.VISIBLE);
        mPager.setVisibility(View.INVISIBLE);
        mWelcomeImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcome_bg));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                intoJy();
            }
        }, 1000);
    }



    private void initViewPager() {

        mWelcomeImg.setVisibility(View.INVISIBLE);
        mPager.setVisibility(View.VISIBLE);

        View view1 = LayoutInflater.from(this).inflate(R.layout.layout_tutorial_1, null);
        t1_icon1= (ImageView) view1.findViewById(R.id.t1_icon1);//电子表
        t1_icon2 = (ImageView) view1.findViewById(R.id.t1_icon2);//旋转电池
        t1_fixed = (ImageView) view1.findViewById(R.id.t1_fixed);//极低耗电文字
        t1_next = (ImageView) view1.findViewById(R.id.t1_next);//向下箭头
        pagers.add(view1);


        View view2 = LayoutInflater.from(this).inflate(R.layout.layout_tutorial_2, null);
        t2_icon1 = (ImageView) view2.findViewById(R.id.t2_icon1);//心型图案
        t2_fixed = (ImageView) view2.findViewById(R.id.t2_fixed);//极小安装文字
        t2_next = (ImageView) view2.findViewById(R.id.t2_next);//向下箭头
        pagers.add(view2);

        View view3 = LayoutInflater.from(this).inflate(R.layout.layout_tutorial_3, null);
        t3_icon2 = (ImageView) view3.findViewById(R.id.t3_icon2);//云左1
        t3_icon3 = (ImageView) view3.findViewById(R.id.t3_icon3);//云左2
        t3_icon4 = (ImageView) view3.findViewById(R.id.t3_icon4);//云右1
        t3_icon5 = (ImageView) view3.findViewById(R.id.t3_icon5);//云右1
        t3_icon6 = (ImageView) view3.findViewById(R.id.t3_icon6);//火箭预留
        t3_fixed = (ImageView) view3.findViewById(R.id.t3_fixed);//极速流畅
        t3_next = (ImageView) view3.findViewById(R.id.t3_next);//向下箭头
        centerLayout = (RelativeLayout) view3.findViewById(R.id.center_layout_3);//云群和中间位置
        pagers.add(view3);
        view3.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub  计算云的起始结束位置
                int h1 = centerLayout.getTop();
                int h2 = centerLayout.getBottom();
                DensityUtil densityUtil = new DensityUtil( ActivityWelcome.this);
                int w = densityUtil.getScreenWidth();

                fx1 = t3_icon2.getTop() + t3_icon2.getHeight();
                fy1 = -t3_icon2.getTop() - t3_icon2.getHeight();
                tx1 = -t3_icon2.getWidth() - t3_icon2.getLeft();
                ty1 = t3_icon2.getTop() + t3_icon2.getLeft() + t3_icon2.getWidth();

                fx2 = t3_icon3.getTop() + t3_icon3.getHeight();
                fy2 = -t3_icon3.getTop() - t3_icon3.getHeight();
                tx2 = -t3_icon3.getWidth() - t3_icon3.getLeft();
                ty2 = t3_icon3.getTop() + t3_icon3.getLeft()+ t3_icon3.getWidth();

                fx3 = w - t3_icon4.getLeft();
                fy3 = -(w - t3_icon4.getLeft());
                tx3 = -(h2 - h1 - t3_icon4.getTop());
                ty3 = h2 - h1 - t3_icon4.getTop();

                fx4 = w - t3_icon5.getLeft();
                fy4 = -(w - t3_icon5.getLeft());
                tx4 = -(h2 - h1 - t3_icon5.getTop());
                ty4 = h2 - h1 - t3_icon5.getTop();
            }
        });

        View view4 = LayoutInflater.from(this).inflate(R.layout.layout_tutorial_4, null);
        t4_icon1 = (ImageView) view4.findViewById(R.id.t4_icon1);//挂板 墨迹天气
        t4_fixed = (ImageView) view4.findViewById(R.id.t4_fixed);//文字图片
        t4_start = (Button)view4.findViewById(R.id.t4_start);

        t4_start.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intoJy();
            }
        });
        pagers.add(view4);

        mAdapter = new VerticalFragementPagerAdapter();
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(this);

        animal(0);
    }


    private void intoJy() {

        PersonalInfoPojo personInfo = PersonalUtil.getPersonInfo(this);

        if( null == personInfo ){

            goIntoLoginActivity();
        }
        else{
            goIntoMainActivity();
        }
    }

    private void goIntoMainActivity() {
        // TODO Auto-generated method stub
        //Intent intent = new Intent( this, ActivityMain.class);
        Intent intent = new Intent( this, MainActivity.class);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);
    }


    private void goIntoLoginActivity() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, ActivityRegister.class);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);
    }


    private void saveFistInFlag() {

        SharedPreferences preferences;
        SharedPreferences.Editor prefsEditor;

        preferences = getSharedPreferences(WELCOME_CFG, Context.MODE_PRIVATE);
        prefsEditor = preferences.edit();
        prefsEditor.putBoolean(FIRST_IN, false);
        prefsEditor.commit();
    }



    private boolean isFirstInJy() {

        boolean isFirstIn = false;

        SharedPreferences preferences;
        preferences = getSharedPreferences(WELCOME_CFG, Context.MODE_PRIVATE);
        isFirstIn = preferences.getBoolean(FIRST_IN, true);

        return isFirstIn;
    }



    private class VerticalFragementPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(pagers.get(position));
            return pagers.get(position);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        animal(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }




    private void animal(int position) {
        switch (position) {
            case 0:
                if (preIndex > position) {
                    t2_icon1.setVisibility(View.INVISIBLE);
                }

                //电子表闪烁
                t1_icon1.setImageResource(R.drawable.t1_frame_animation);//电子表设置背景
                t1_icon1_animationDrawable = (AnimationDrawable) t1_icon1.getDrawable();
                t1_icon1_animationDrawable.start();

                //电池旋转
                Animation animation1 = AnimationUtils.loadAnimation(ActivityWelcome.this, R.anim.tutorail_rotate);
                LinearInterpolator lin = new LinearInterpolator();
                animation1.setInterpolator(lin);

                t1_icon2.setVisibility(View.VISIBLE);
                t1_icon2.startAnimation(animation1);

                //文字放大
                animationTop = AnimationUtils.loadAnimation(ActivityWelcome.this,R.anim.tutorail_scalate_top);
                t1_fixed.startAnimation(animationTop);

                //向下箭头
                animationBottom = AnimationUtils.loadAnimation(ActivityWelcome.this,R.anim.tutorail_bottom);
                t1_next.startAnimation(animationBottom);

                break;
            case 1:
                if (preIndex > position) {
                    t3_icon2.getAnimation().cancel();
                    t3_icon3.getAnimation().cancel();
                    t3_icon4.getAnimation().cancel();
                    t3_icon5.getAnimation().cancel();
                    t3_icon2.setVisibility(View.INVISIBLE);
                    t3_icon3.setVisibility(View.INVISIBLE);
                    t3_icon4.setVisibility(View.INVISIBLE);
                    t3_icon5.setVisibility(View.INVISIBLE);
                    t3_icon6_animationDrawable.stop();
                } else {
                    t1_icon1_animationDrawable.stop();
                    t1_icon2.getAnimation().cancel();
                    t1_icon2.setVisibility(View.INVISIBLE);
                }

                //心型图案由小变大
                Animation animation2 = AnimationUtils.loadAnimation(ActivityWelcome.this, R.anim.tutorail_scalate);
                t2_icon1.setVisibility(View.VISIBLE);
                t2_icon1.startAnimation(animation2);

                //文字变大
                t2_fixed.startAnimation(animationTop);

                //向下箭头
                t2_next.startAnimation(animationBottom);

                break;
            case 2:

                //火箭的图片设置
                t3_icon6.setImageResource(R.drawable.t3_frame_animation);
                t3_icon6_animationDrawable = (AnimationDrawable) t3_icon6.getDrawable();


                final TranslateAnimation transAnimation2 = new TranslateAnimation(fx1, tx1, fy1, ty1);
                transAnimation2.setDuration(800);
                transAnimation2.setRepeatCount(Animation.INFINITE);
                transAnimation2.setRepeatMode(Animation.RESTART);
                lin = new LinearInterpolator();
                transAnimation2.setInterpolator(lin);

                final TranslateAnimation transAnimation3 = new TranslateAnimation(fx2, tx2, fy2, ty2);
                transAnimation3.setDuration(1200);
                transAnimation3.setRepeatCount(Animation.INFINITE);
                transAnimation3.setRepeatMode(Animation.RESTART);
                transAnimation3.setInterpolator(lin);

                final TranslateAnimation transAnimation4 = new TranslateAnimation(fx3, tx3, fy3, ty3);
                transAnimation4.setDuration(1200);
                transAnimation4.setRepeatCount(Animation.INFINITE);
                transAnimation4.setRepeatMode(Animation.RESTART);
                transAnimation4.setInterpolator(lin);

                final TranslateAnimation transAnimation5 = new TranslateAnimation(fx4, tx4, fy4, ty4);
                transAnimation5.setDuration(800);
                transAnimation5.setRepeatCount(Animation.INFINITE);
                transAnimation5.setRepeatMode(Animation.RESTART);
                transAnimation5.setInterpolator(lin);


                new Handler() {
                    public void handleMessage(android.os.Message msg) {
                        if (msg.what == 1) {

                            t3_icon2.setVisibility(View.VISIBLE);
                            t3_icon3.setVisibility(View.VISIBLE);
                            t3_icon4.setVisibility(View.VISIBLE);
                            t3_icon5.setVisibility(View.VISIBLE);

                            t3_icon2.startAnimation(transAnimation2);
                            t3_icon3.startAnimation(transAnimation3);
                            t3_icon4.startAnimation(transAnimation4);
                            t3_icon5.startAnimation(transAnimation5);
                            t3_icon6_animationDrawable.start();

                        }
                    };
                }.sendEmptyMessageDelayed(1, 1000);// 0.5 延迟1s开始效果


                //文字效果
                t3_fixed.startAnimation(animationTop);
                //向下箭头
                t3_next.startAnimation(animationBottom);
                break;

            case 3:
//			t3_icon2.getAnimation().cancel();
//			t3_icon3.getAnimation().cancel();
//			t3_icon4.getAnimation().cancel();
//			t3_icon5.getAnimation().cancel();
                t3_icon2.setVisibility(View.INVISIBLE);
                t3_icon3.setVisibility(View.INVISIBLE);
                t3_icon4.setVisibility(View.INVISIBLE);
                t3_icon5.setVisibility(View.INVISIBLE);
                t3_icon6_animationDrawable.stop();

                int pivot = Animation.RELATIVE_TO_SELF;
                CycleInterpolator interpolator = new CycleInterpolator(3.0f);
                RotateAnimation animation = new RotateAnimation(0, 10, pivot, 0.47f, pivot, 0.05f);
                animation.setStartOffset(500);
                animation.setDuration(3000);
                animation.setRepeatCount(1);// Animation.INFINITE
                animation.setInterpolator(interpolator);
                t4_icon1.startAnimation(animation);

                //文字效果
                t4_fixed.startAnimation(animationTop);
                break;

            default:
                break;
        }

        preIndex = position;
    }
}

