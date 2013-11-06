package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.film.ActivityActivity;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.http.image.util.ImageFetcher;

/**
 * Created by showu on 13-8-7.
 */
public class AccountActivityJson extends MessageJson {
    private String title;
    private String imgurl;
    private String iconimg;
    private String address;
    private String time;
    private String actid;
    private String actlinkurl;
    private String baogu_send_time;

    @Override
    public void action(Context context, BaseMessage message) {
        Intent intent=new Intent(context, ActivityActivity.class) ;
        intent.putExtra(ActivityActivity.EXT_ID,actid) ;
        intent.putExtra(ActivityActivity.EXT_TITLE,title) ;
        intent.putExtra(ActivityActivity.EXT_URL,actlinkurl);
        context.startActivity(intent);
    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_activity_item, null);
        TextView titleText = (TextView) view.findViewById(R.id.title);
        TextView addressText = (TextView) view.findViewById(R.id.activity_address);
        TextView activityTimeText = (TextView) view.findViewById(R.id.activity_time);
        titleText.setText(title);
        addressText.setText(address);
        activityTimeText.setText(time);
        ImageView iconImage = (ImageView) view.findViewById(R.id.activity_icon);
        ImageView activityImage = (ImageView) view.findViewById(R.id.image);
        fetcher.loadImage(iconimg, iconImage);
        fetcher.loadImage(imgurl, activityImage);
        return view;
    }

    @Override
    public MessageListBean getSessionBean() {
        MessageListBean listBean = new MessageListBean();
        listBean.setContent(title);
        return listBean;
    }
}
