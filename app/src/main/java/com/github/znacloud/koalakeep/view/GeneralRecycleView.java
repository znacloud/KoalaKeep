package com.github.znacloud.koalakeep.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by Zengnianan on 2016/6/16.
 * Copyright (c) 2016 Xunlei. All rights reserved.
 *
 * @author Zengnianan
 * @since 2016/6/16
 */
public class GeneralRecycleView extends RecyclerView {
    private static final String TAG = GeneralRecycleView.class.getSimpleName();
    private boolean mSingleOpenEnable = false;
    private Rect rect;
    private float mLastRawY = 0;
    private float mLastRawX = 0;
    private OnScrollingListener mScrollingListener;
    private int mTouchSlop;

    public GeneralRecycleView(Context context) {
        super(context);
        init(context);
    }

    public GeneralRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GeneralRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        rect = new Rect();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        addOnItemTouchListener(new SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if(!mSingleOpenEnable) return false;

                if(e.getAction() == MotionEvent.ACTION_DOWN){
                    long start = System.currentTimeMillis();
                    int itemCount = getLayoutManager().getChildCount();

                    for(int i=0;i<itemCount;i++){

                        View view = getLayoutManager().getChildAt(i);
                        if(view instanceof Openable){

                            Openable openable = (Openable)view;
                            if(openable.isOpen()){

                                view.getGlobalVisibleRect(rect);
                                if(!rect.contains((int)e.getRawX(),(int)e.getRawY())) {

                                    openable.setOpen(false);
                                    break;
                                }
                            }

                        }
                    }
                    long end = System.currentTimeMillis();
                    Log.d(TAG,"cost=>"+(end-start));
                }
                return false;
            }

        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(mScrollingListener != null) {
            Log.d(TAG,"recycle action =>"+e.getAction());
            if(e.getAction() == MotionEvent.ACTION_DOWN){
                mLastRawX = e.getRawX();
                mLastRawY = e.getRawY();
            }else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                float rawY = e.getRawY();
                float rawX = e.getRawX();
                int dy = (int)(rawY - mLastRawY);
                int dx = (int)(rawX - mLastRawX);
                if(!(canScrollVertically(-1)&&dy <= 0
                        || canScrollVertically(1)&& dy >= 0)){
                    dy = 0;
                }

                if(!(canScrollHorizontally(-1)&&dx < -0
                        || canScrollHorizontally(1)&& dx >= 0)){
                    dx = 0;
                }

                if(dx !=0 || dy !=0) {
                    mScrollingListener.onScrolling(dx, dy);
                }
                mLastRawX =rawX;
                mLastRawY = rawY;
            }
        }
        return super.onTouchEvent(e);
    }

    public void setSingleOpenEnable(boolean enable){
        mSingleOpenEnable =enable;

    }

    public void setOnScrollinglistener(OnScrollingListener listener){
        mScrollingListener = listener;
    }


    public interface OnScrollingListener{
        public void onScrolling(float dx,float dy);
    }

    public interface Openable{
        void setOpen(boolean open);
        boolean isOpen();
    }
}
