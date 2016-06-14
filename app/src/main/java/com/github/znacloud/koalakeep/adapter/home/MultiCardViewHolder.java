package com.github.znacloud.koalakeep.adapter.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Zengnianan on 2016/6/14.
 * Copyright (c) 2016 Xunlei. All rights reserved.
 *
 * @author Zengnianan
 * @since 2016/6/14
 */
public abstract class MultiCardViewHolder extends RecyclerView.ViewHolder{
    public MultiCardViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindItemData(MultiCardData data);
}
