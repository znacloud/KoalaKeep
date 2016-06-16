package com.github.znacloud.koalakeep.entity;

import android.content.Context;

import com.github.znacloud.koalakeep.R;

/**
 * Created by Zengnianan on 2016/6/15.
 * Copyright (c) 2016 Xunlei. All rights reserved.
 *
 * @author Zengnianan
 * @since 2016/6/15
 */
public class CardItemInfo {
    private String title;
    private String content;
    private String poster;//图片
    private boolean isPrivate;
    private String from;//来源
    private String creator;//卡片创造着

    public static CardItemInfo createTestItem(Context context){
        CardItemInfo info = new CardItemInfo();
        info.setTitle(context.getResources().getString(R.string.title_sample));
        String content = context.getResources().getString(R.string.content_sample);
        info.setContent(content.substring(0,10+(int)(Math.random()*(content.length()-10))));
        info.setPoster(Math.random()*100 > 70 ?null :
                Math.random()*100 > 30 ? "http://a.hiphotos.baidu.com/image/pic/item/1c950a7b02087bf442c2b1c9fad3572c11dfcfe0.jpg" :
                        "http://g.hiphotos.baidu.com/image/pic/item/0d338744ebf81a4cdfd76fe9df2a6059252da62d.jpg");
        info.setPrivate(Math.random()*100 > 50 ? true:false);
        info.setFrom(context.getResources().getString(R.string.from_sample));
        info.setCreator(context.getResources().getString(R.string.creator_sample));

        return info;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
