package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.common.util.DateUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.message.ShowMapActivity;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageSRC;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.josn.EAJson;

import java.util.Date;

/**
 * Created by showu on 13-7-18.
 */
public class LocationJson extends MessageJson {
    @EAJson
    private String  baogu_latitude;
    @EAJson
    private String baogu_longitude ;

    public String getBaogu_latitude() {
        return baogu_latitude;
    }

    public void setBaogu_latitude(String baogu_latitude) {
        this.baogu_latitude = baogu_latitude;
    }

    public String getBaogu_longitude() {
        return baogu_longitude;
    }

    public void setBaogu_longitude(String baogu_longitude) {
        this.baogu_longitude = baogu_longitude;
    }

    public long getBaogu_send_time() {
        return baogu_send_time;
    }

    public void setBaogu_send_time(long baogu_send_time) {
        this.baogu_send_time = baogu_send_time;
    }

    private long baogu_send_time ;
    @Override
    public void action(Context context,BaseMessage message) {
        Intent intent =new Intent(context, ShowMapActivity.class) ;
        intent.putExtra("activity_type","activity_type") ;
        intent.putExtra("longitude",baogu_longitude) ;
        intent.putExtra("latitude",baogu_latitude) ;
        context.startActivity(intent);
    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        int roundpx = context.getResources().getDimensionPixelSize(R.dimen.message_round_agle) ;
        View convertView;
        if (message.getMessageSRC() == MessageSRC.TO) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.message_mine_location_item, null);
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.image);
            fetcher.loadImage(getMapUrl(),imageView);
        } else {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.message_from_location_item, null);
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.image);
            fetcher.loadImage(getMapUrl(), imageView);
        }
        ImageView headImage = (ImageView) convertView.findViewById(R.id.headImage);
        UserInfo userInfo = message.getUserinfo() ;
        if(userInfo!=null){
            if(userInfo.getUserid().equals(Const.user.getUserid())){
                fetcher.loadImage(Const.user.getProfileimg(),headImage,roundpx);
            }else{
                fetcher.loadImage(userInfo.getHeadImage(),headImage,roundpx);
            }
        }

        TextView timeText = (TextView) convertView.findViewById(R.id.timeText);
        if (message.isNeedShowTime()) {
            Date date = DateUtil.getDate("1970-01-01 00:00:00");
            Date d = new Date(date.getTime() + message.getSendTime() * 1000
                    + (8 * 3600000));
            if (userInfo != null) {
                timeText.setText(DateUtil.getDateString(d, "yyyy-MM-dd HH:mm"));
            }
        } else {
            timeText.setVisibility(View.GONE);
        }

        return convertView;



    }

    @Override
    public MessageListBean getSessionBean() {
        MessageListBean sessionBean = new MessageListBean();
        sessionBean.setContent("[地图]");
        return sessionBean;
    }


    private String getMapUrl() {
        StringBuffer sb = new StringBuffer(
                "http://maps.google.com/maps/api/staticmap?sensor=true");
        sb.append("&center=").append(baogu_longitude).append(",")
                .append(baogu_latitude);
        sb.append("&markers=color:blue%7Clabel:S%7C")
                .append(baogu_latitude).append(",")
                .append(baogu_longitude);
        sb.append("&size=400x200");
        sb.append("&zoom=15");
        return sb.toString();
    }
}
