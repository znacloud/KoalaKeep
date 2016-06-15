package com.github.znacloud.koalakeep.adapter.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.znacloud.koalakeep.R;
import com.github.znacloud.koalakeep.view.GeneralCardItemLayout;

import java.util.ArrayList;

/**
 * Created by Zengnianan on 2016/6/13.
 * Copyright (c) 2016 Xunlei. All rights reserved.
 *
 * @author Zengnianan
 * @since 2016/6/13
 */
public class CardAdapter extends RecyclerView.Adapter<MultiCardViewHolder>{

    private ArrayList<MultiCardData> mDatas;

    @Override
    public MultiCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        MultiCardViewHolder viewHolder = null;
        if(viewType == MultiCardData.TYPE_HEADER){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,null);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder = new HeaderItemViewHolder(itemView);
        }else if(viewType == MultiCardData.TYPE_CARD_TEXT){
            itemView = new GeneralCardItemLayout(parent.getContext());
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder = new GeneryItemViewHolder(itemView);
        }

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(MultiCardViewHolder holder, int position) {
        holder.bindItemData(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        if(mDatas == null) return 0;
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (mDatas== null || mDatas.isEmpty()) ? MultiCardData.TYPE_UNKNOWN :
                mDatas.get(position).getType();
    }

    public ArrayList<MultiCardData> getDatas() {
        return mDatas;
    }

    /**
     * 设置新的数据集
     * @param datas 数据集
     */
    public void setDatas(ArrayList<MultiCardData> datas) {
        if(mDatas == null){
            mDatas = new ArrayList<>(10);
        }else{
            mDatas.clear();
        }

        mDatas.addAll(datas);

        notifyDataSetChanged();
    }

    /**
     * 添加一项数据到末尾
     * @param data 要添加的数据
     */
    public void addOneData(MultiCardData data){
        if(mDatas == null){
            mDatas = new ArrayList<>(10);
        }

        mDatas.add(data);
        notifyItemInserted(mDatas.size()-1);
    }

    public void addOneDataFirst(MultiCardData data){
        addOneData(0,data);
    }

    public void addOneData(int pos,MultiCardData data){
        if(mDatas == null){
            mDatas = new ArrayList<>(10);
        }

        mDatas.add(pos,data);
        notifyItemInserted(pos);
    }


    /**
     * 添加一组数据到尾部
     * @param datas
     */
    public void addDatas(ArrayList<MultiCardData> datas){
        if(mDatas == null){
            mDatas = new ArrayList<>(10);
        }

        int oldSize = datas.size();
        datas.addAll(datas);

        notifyItemRangeInserted(oldSize-1,datas.size());
    }
}
