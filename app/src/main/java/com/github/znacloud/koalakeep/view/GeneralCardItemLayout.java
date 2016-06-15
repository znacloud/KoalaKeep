package com.github.znacloud.koalakeep.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.znacloud.koalakeep.R;
import com.github.znacloud.koalakeep.entity.CardItemInfo;

/**
 * Created by Zengnianan on 2016/6/15.
 * Copyright (c) 2016 Xunlei. All rights reserved.
 *
 * @author Zengnianan
 * @since 2016/6/15
 */
public class GeneralCardItemLayout extends FrameLayout{
    private TextView mTitleTv;
    private ImageView mMenuIv;
    private TextView mExtraTv;
    private TextView mContentTv;
    private CardView mContentLayout;
    private int mTranslateX = 0;
    private float mStartX = 0f;
    private float mStartY = 0f;
    private ViewConfiguration mViewConfig;

    public GeneralCardItemLayout(Context context) {
        super(context);
        init(context);
    }

    public GeneralCardItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GeneralCardItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mTranslateX = context.getResources().getDimensionPixelSize(R.dimen.card_menu_width);
        mViewConfig = ViewConfiguration.get(context);
        LayoutInflater.from(context).inflate(R.layout.text_card_item,this,true);
        initViews();

        initEvent();
    }



    private void initViews() {
        mContentLayout = (CardView)findViewById(R.id.cv_content);
        mTitleTv = (TextView)findViewById(R.id.tv_card_title);
        mMenuIv = (ImageView) findViewById(R.id.iv_card_menu);
        mExtraTv = (TextView) findViewById(R.id.tv_extra);
        mContentTv = (TextView) findViewById(R.id.tv_card_content);
    }

    private void initEvent() {
        mMenuIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMenuShown()) {
                    mContentLayout.animate().translationX(0);
                }else{
                    mContentLayout.animate().translationX(-mTranslateX);
                }
            }
        });

        mContentLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    mStartX = event.getRawX();
                    mStartY = event.getRawY();
                    return true;
                }else if(action == MotionEvent.ACTION_MOVE){
                    float x = event.getRawX();
                    float y = event.getRawY();


                    if(!isMenuShown()&&mStartX < x  //菜单隐藏时，向右滑动不处理
                            || isMenuShown()&&mStartX > x  //菜单打开时，向左滑动不处理
                            || (mStartX-x)<Math.abs(y-mStartY //竖向滑动不处理
                    )){
                        return false;
                    }

                    //滑动
                    float delta = x-mStartX;
                    if(Math.abs(delta) > mViewConfig.getScaledTouchSlop()
                            && Math.abs(delta) < mTranslateX) {
                        mContentLayout.setTranslationX(x - mStartX);
                    }
                    return true;

                }else if(action == MotionEvent.ACTION_CANCEL||action==MotionEvent.ACTION_UP){
                    if(isMenuShown()){
                        mContentLayout.animate().translationX(0);
                    }else{
                        mContentLayout.animate().translationX(-mTranslateX);
                    }
                    return true;
                    //TODO:记录速度，惯性滑动
                }
                return false;
            }
        });
    }



    private boolean isMenuShown() {
        if(mContentLayout.getTranslationX() == -mTranslateX) return true;
        return false;
    }

    public void bindViewData(CardItemInfo data) {
        CardItemInfo info = data;
        mTitleTv.setText(info.getTitle());
        mContentTv.setText(info.getContent());
        mExtraTv.setVisibility(info.isPrivate() ? View.VISIBLE:View.INVISIBLE);
    }
}
