package com.qinnuan.engine.listener;

import android.widget.ImageView;

/**
 * Created by Administrator on 13-7-17.
 */
public interface IUserHomeListener {

    public void gotoUserInfoDetail();

    public void gotoUserFilmTicket();

    public void gotoCountCash();

    public void gotoChit();

    public void setHead(String imgUrl, ImageView head);

}
