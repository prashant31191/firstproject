package com.qinnuan.engine.listener;


import android.widget.ImageView;

import com.qinnuan.engine.activity.user.UserInfoActivity;

/**
 * Created by Administrator on 13-7-17.
 */
public interface IUserInfoListener {

    public void update(String update, UserInfoActivity.UPDATETYPE type);

    public void setHeadImg(String imgUrl, ImageView head);

    public void updateHead(UserInfoActivity.UPDATETYPE typ,int postion);

    public void bindWeibo();

    public void bindPhone();
}
