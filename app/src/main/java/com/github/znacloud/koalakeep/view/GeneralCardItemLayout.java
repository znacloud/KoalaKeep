package com.github.znacloud.koalakeep.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.github.znacloud.koalakeep.R;
import com.github.znacloud.koalakeep.entity.CardItemInfo;
import com.github.znacloud.koalakeep.network.VolleyManager;

/**
 * Created by Zengnianan on 2016/6/15.
 * Copyright (c) 2016 Xunlei. All rights reserved.
 *
 * @author Zengnianan
 * @since 2016/6/15
 */
public class GeneralCardItemLayout extends FrameLayout implements GeneralRecycleView.Openable, View.OnClickListener {
    private static final int DIRECT_RIGHT = 0;
    private static final int DIRECT_LEFT = 1;
    private static final int DIRECT_NULL = -1;
    private static final String TAG = GeneralCardItemLayout.class.getSimpleName();
    private static final long ANIM_DURATION = 250;

    private TextView mTitleTv;
    private ImageView mMenuIv;
    private TextView mExtraTv;
    private TextView mContentTv;
    private CardView mContentLayout;
    private int mTranslateX = 0;
    private float mStartX = 0f;
    private float mStartY = 0f;
    private ViewConfiguration mViewConfig;
    private float mStartTransX = 0;
    private Scroller mScroller;
    private VelocityTracker mVociTracker;
    private int mDirection = DIRECT_NULL;
    private int flag = 0;
    private int mTouchSlop = 0;
    private boolean mIsClick = true;
    private ImageView mPosterIv;
    private ImageLoader mImageLoader;
    private TextView mShareTv;
    private TextView mEditTv;
    private CheckedTextView mPrivateCTv;
    private TextView mDeleteTv;
    private CardItemInfo mItemInfo;

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
        mScroller = new Scroller(context,new DecelerateInterpolator());
        mVociTracker = VelocityTracker.obtain();
        mTouchSlop = mViewConfig.getScaledTouchSlop();

        mImageLoader = VolleyManager.getInstance(context).getImageLoader();

        LayoutInflater.from(context).inflate(R.layout.general_card_item,this,true);
        initViews();

        initEvent();
    }



    private void initViews() {
        mContentLayout = (CardView)findViewById(R.id.cv_content);
        mTitleTv = (TextView)findViewById(R.id.tv_card_title);
        mPosterIv = (ImageView)findViewById(R.id.iv_poster);
        mMenuIv = (ImageView) findViewById(R.id.iv_card_menu);
        mExtraTv = (TextView) findViewById(R.id.tv_extra);
        mContentTv = (TextView) findViewById(R.id.tv_card_content);

        ViewCompat.setBackgroundTintList(mMenuIv,getResources().getColorStateList(R.color.menu_text_color_selector));
        ViewCompat.setBackgroundTintMode(mMenuIv, PorterDuff.Mode.SRC_IN);

        //menu item
        mShareTv = (TextView)findViewById(R.id.tv_menu_share);
        mEditTv = (TextView)findViewById(R.id.tv_menu_edit);
        mPrivateCTv = (CheckedTextView)findViewById(R.id.tv_menu_private);
        mDeleteTv = (TextView)findViewById(R.id.tv_menu_delete);
    }

    private void initEvent() {
        mMenuIv.setOnClickListener(this);
        mShareTv.setOnClickListener(this);
        mPrivateCTv.setOnClickListener(this);
        mDeleteTv.setOnClickListener(this);

        mContentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"got to detail page");
            }
        });

        mContentLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Log.d(TAG,"action=>"+action);
                if(action == MotionEvent.ACTION_DOWN){
                    mStartX = event.getRawX();
                    mStartY = event.getRawY();
                    mStartTransX = mContentLayout.getTranslationX();
                    mVociTracker.clear();
                    mScroller.forceFinished(true);
                    mScroller.abortAnimation();
                    flag = 0;
                    mIsClick = true;
                    return true;
                }else if(action == MotionEvent.ACTION_MOVE){
                    float x = event.getRawX();
                    float y = event.getRawY();

                    if(Math.abs(y-mStartY) < mTouchSlop
                            && Math.abs(x-mStartX) < mTouchSlop){
                        //Do nothing
                        return true;
                    }

                    mIsClick = false;

                    mVociTracker.addMovement(event);
                    //竖向滑动不处理
                    if(flag == 0
                            &&Math.abs(y-mStartY)>mTouchSlop
                            && Math.abs(mStartX-x)<Math.abs(y-mStartY) ){
                        Log.d(TAG,"vertical scroll,return false");
                        requestDisallowInterceptTouchEvent(false);
                        return false;
                    }else if(flag != 1 &&Math.abs(x-mStartX)>mTouchSlop) {
                        requestDisallowInterceptTouchEvent(true);
                        flag = 1;
                    }


                    //滑动
                    float delta = x-mStartX;
                    if(delta > mTouchSlop){
                        mDirection = DIRECT_RIGHT;
                    }else  if(delta < -mTouchSlop){
                        mDirection = DIRECT_LEFT;
                    }else {
                        mDirection = DIRECT_NULL;
                    }

                    if(mDirection != DIRECT_NULL){
                        float transX = mStartTransX+delta;
                        if(transX > 0){
                            transX =0;
                        }else if(transX < -mTranslateX){
                            transX = -mTranslateX;
                        }

                        mContentLayout.setTranslationX(transX);
                    }


                    return true;

                }else if(action==MotionEvent.ACTION_UP){
                    if(mIsClick){
                        mContentLayout.performClick();
                        return false;
                    }

                    mVociTracker.computeCurrentVelocity(1000);
                    float vX = mVociTracker.getXVelocity();
                    float transX = mContentLayout.getTranslationX();
                    int startX=0,deltaX=0;
                    Log.d(TAG,"vx=>"+vX);
                    if(action == MotionEvent.ACTION_CANCEL){
                        startX = (int)transX;
                        deltaX = (int)(-transX);
                    }else if(vX > 300||vX<-300){
                        if(mDirection == DIRECT_LEFT){
                            startX = (int)transX;
                            deltaX = (int)(-mTranslateX-transX);
                        }else if(mDirection == DIRECT_RIGHT){
                            startX = (int)transX;
                            deltaX = (int)(-transX);
                        }
                    }else if(transX < -mTranslateX/2f){
                        startX = (int)transX;
                        deltaX = (int)(-mTranslateX-transX);
                    }else {
                        startX = (int)transX;
                        deltaX = (int)(-transX);
                    }

                    Log.d(TAG,"start scroll=>"+startX+"--"+deltaX);
                    mScroller.startScroll(startX,0,deltaX,0,(int)(Math.abs(deltaX)*1.0f/mTranslateX*ANIM_DURATION));
                    invalidate();

                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.d(TAG,"compute Scroll");
        if(mScroller.computeScrollOffset()){
            Log.d(TAG,"setTransX=>"+mScroller.getCurrX());
            mContentLayout.setTranslationX(mScroller.getCurrX());
            postInvalidate();
        }
    }

    private boolean isMenuShown() {
        if(mContentLayout.getTranslationX() < -mTranslateX+mTouchSlop) return true;
        return false;
    }

    public void bindViewData(CardItemInfo data) {
        mItemInfo = data;
        mTitleTv.setText(mItemInfo.getTitle());
        String content = mItemInfo.getContent();
        if(TextUtils.isEmpty(content)){
            mContentTv.setVisibility(GONE);
        }else {
            mContentTv.setText(content);
            mContentTv.setVisibility(VISIBLE);
        }

        String posterUrl = data.getPoster();
        if(TextUtils.isEmpty(posterUrl)){
            mPosterIv.setVisibility(GONE);
            //无图片时，文字内容显示多一些
            mContentTv.setMaxLines(getResources().getInteger(R.integer.card_max_line));
        }else{
            mImageLoader.get(posterUrl,ImageLoader.getImageListener(mPosterIv,0,0));
            mPosterIv.setVisibility(VISIBLE);
            mContentTv.setMaxLines(getResources().getInteger(R.integer.card_min_line));
        }

        if(mItemInfo.isPrivate()) {
            mExtraTv.setVisibility(VISIBLE);
            mPrivateCTv.setChecked(true);
        }else{
            mExtraTv.setVisibility(INVISIBLE);
            mPrivateCTv.setChecked(false);
        }
    }

    @Override
    public void setOpen(boolean open) {
        if(!open) {
            mContentLayout.animate()
                    .setDuration(ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator())
                    .translationX(0);
        }else{
            mContentLayout.animate()
                    .setDuration(ANIM_DURATION)
                    .setInterpolator(new DecelerateInterpolator())
                    .translationX(-mTranslateX);
        }
    }

    @Override
    public boolean isOpen() {
        return isMenuShown();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_card_menu){
            setOpen(!isMenuShown());
        }else if(v.getId() == R.id.tv_menu_share){
            //TODO:share
        }else if(v.getId() == R.id.tv_menu_edit){
            //TODO:edit
        }else if(v.getId() == R.id.tv_menu_private){
            mPrivateCTv.setChecked(!mItemInfo.isPrivate());
            mItemInfo.setPrivate(!mItemInfo.isPrivate());
            mExtraTv.setVisibility(mItemInfo.isPrivate() ? VISIBLE : INVISIBLE);
            setOpen(false);
        }else if(v.getId() == R.id.tv_menu_delete){
            //TODO:delete
        }
    }
}
