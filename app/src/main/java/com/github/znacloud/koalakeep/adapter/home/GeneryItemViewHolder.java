package com.github.znacloud.koalakeep.adapter.home;

import android.view.View;
import com.github.znacloud.koalakeep.entity.CardItemInfo;
import com.github.znacloud.koalakeep.view.GeneralCardItemLayout;

/**
 * Created by Zengnianan on 2016/6/15.
 * Copyright (c) 2016 Xunlei. All rights reserved.
 *
 * @author Zengnianan
 * @since 2016/6/15
 */
public class GeneryItemViewHolder extends MultiCardViewHolder {
    private final GeneralCardItemLayout mRootView;

    public GeneryItemViewHolder(View itemView) {
        super(itemView);
        mRootView = (GeneralCardItemLayout)itemView;
    }

    @Override
    public void bindItemData(MultiCardData data) {
        CardItemInfo info = (CardItemInfo)data.getOrginData();
        mRootView.bindViewData(info);
    }
}
