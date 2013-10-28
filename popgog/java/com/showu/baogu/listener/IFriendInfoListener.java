package com.showu.baogu.listener;

import android.view.View;
import android.widget.ImageView;

import com.showu.baogu.bean.Fan;
import com.showu.baogu.bean.User;

/**
 * Created by Administrator on 13-7-17.
 */
public interface IFriendInfoListener {

    public void gotoWeibo(Fan o, View b);

    public void gotoDynamic(Fan o, View b);

    public void verified(Fan o, View b);
    public void forbid(Fan o, View b);

    public void sayhi();
    public void report(Fan o, View b);

    public void sendMsg(Fan o, View b);

    public void fillData(User user);

    public void setHeadImg(String url, ImageView img);

    public void setDynamicImg(String[] url, ImageView[] img);

    public void showPop(View view);

    public void setDynamicImg(String url, ImageView imgv);

}
