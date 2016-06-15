package com.github.znacloud.koalakeep.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.znacloud.koalakeep.adapter.home.CardAdapter;
import com.github.znacloud.koalakeep.R;
import com.github.znacloud.koalakeep.adapter.home.MultiCardData;
import com.github.znacloud.koalakeep.entity.CardItemInfo;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyleView;
    private LinearLayoutManager mLayoutManger;
    private CardAdapter mAdapter;
    private MyHandler mMyhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyhandler = new MyHandler();

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.fresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

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
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mMyhandler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });

        mRecyleView = (RecyclerView)findViewById(R.id.recyle_view);
        mLayoutManger = new LinearLayoutManager(this);
        mRecyleView.setLayoutManager(mLayoutManger);
        mAdapter= new CardAdapter();
        mRecyleView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO:add action
            }
        });
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
