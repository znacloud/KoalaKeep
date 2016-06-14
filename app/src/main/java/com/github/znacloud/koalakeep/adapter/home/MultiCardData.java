package com.github.znacloud.koalakeep.adapter.home;

/**
 * Created by Zengnianan on 2016/6/14.
 * Copyright (c) 2016 Xunlei. All rights reserved.
 *
 * @author Zengnianan
 * @since 2016/6/14
 */
public class MultiCardData<T> {
        public static final int TYPE_UNKNOWN = -1;
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_CARD_TEXT = 1;
        public static final int TYPE_CARD_IAMGE = 2;
        public static final int TYPE_CARD_VIDEO = 3;

        //... may another type need

        public static final int TYPE_FOOTER = 10;


    private int type = TYPE_UNKNOWN;

    private  T orginData;

    public MultiCardData(int type,T orginData) {
        this.type = type;
        this.orginData = orginData;
    }

    public static int getTypeUnknown() {
        return TYPE_UNKNOWN;
    }

    public int getType() {
        return type;
    }

    public T getOrginData() {
        return orginData;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOrginData(T orginData) {
        this.orginData = orginData;
    }
}
