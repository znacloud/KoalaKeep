package com.github.znacloud.koalakeep.ui;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.znacloud.koalakeep.adapter.home.CardAdapter;
import com.github.znacloud.koalakeep.R;
import com.github.znacloud.koalakeep.adapter.home.MultiCardData;
import com.github.znacloud.koalakeep.entity.CardItemInfo;
import com.github.znacloud.koalakeep.view.GeneralRecycleView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GeneralRecycleView mRecyleView;
    private LinearLayoutManager mLayoutManger;
    private CardAdapter mAdapter;
    private MyHandler mMyhandler;
    private RelativeLayout mBottomBarLyt;
    private ImageView mUserInfoIv;
    private ImageView mAddNewIv;
    private ImageView mMoreInfoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyhandler = new MyHandler();
        initViews();
        initEvents();
    }

    private void initEvents() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh");
                //TODO:add action
                //TEST
                new Thread(new Runnable() {//下拉触发的函数，这里是谁1s然后加入一个数据，然后更新界面
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mMyhandler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });


        mRecyleView.setOnScrollinglistener(new GeneralRecycleView.OnScrollingListener() {


            @Override
            public void onScrolling(float dx,float dy) {

                Log.d(TAG,"dy=>"+dy);
                if(dy < 0 && mBottomBarLyt.getTranslationY() == 0){
                    mBottomBarLyt.clearAnimation();
                    mBottomBarLyt.animate()
                            .translationY(mBottomBarLyt.getHeight());
                }else if(dy > 0 && mBottomBarLyt.getTranslationY() == mBottomBarLyt.getHeight()){
                    mBottomBarLyt.clearAnimation();
                    mBottomBarLyt.animate().translationY(0);
                }
            }
        });
    }

    private void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.fresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorThemeAccent);

        mRecyleView = (GeneralRecycleView)findViewById(R.id.recyle_view);
        mLayoutManger = new LinearLayoutManager(this);
        mRecyleView.setLayoutManager(mLayoutManger);
        mAdapter= new CardAdapter();
        mRecyleView.setAdapter(mAdapter);
        mRecyleView.setSingleOpenEnable(true);

        mBottomBarLyt = (RelativeLayout)findViewById(R.id.ryt_floating_bar);
        ViewCompat.setElevation(mBottomBarLyt,getResources().getDimensionPixelSize(R.dimen.elevation_small));

        mUserInfoIv = (ImageView)findViewById(R.id.iv_user_info);
        ViewCompat.setBackgroundTintList(mUserInfoIv,getResources().getColorStateList(R.color.icon_tint_color_selector));
        mAddNewIv = (ImageView)findViewById(R.id.iv_add_new);
        ViewCompat.setBackgroundTintList(mAddNewIv,getResources().getColorStateList(R.color.icon_tint_color_selector));

        mMoreInfoIv = (ImageView)findViewById(R.id.iv_more);
        ViewCompat.setBackgroundTintList(mMoreInfoIv,getResources().getColorStateList(R.color.icon_tint_color_selector));
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(mAdapter.getItemCount() == 0) {
                        mAdapter.addOneDataFirst(new MultiCardData<String>(MultiCardData.TYPE_HEADER, "header"));
                    }
                    mAdapter.addOneData(1, new MultiCardData(MultiCardData.TYPE_CARD_TEXT,CardItemInfo.createTestItem(MainActivity.this)));
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    }

}
