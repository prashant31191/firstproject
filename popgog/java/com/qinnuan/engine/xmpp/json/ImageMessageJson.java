package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.josn.EAJson;
import com.qinnuan.common.util.DateUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.activity.ImageDetailActivity;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageSRC;
import com.qinnuan.common.http.image.util.LoadLocalImage;
import com.qinnuan.common.util.ImageUtil;

import java.util.Date;

/**
 * Created by showu on 13-7-18.
 */
public class ImageMessageJson extends MessageJson {
    @EAJson
    private String baogu_imageurl;
    @EAJson
    private long baogu_send_time;

    public ImageMessageJson() {
    }


    public String getBaogu_imageurl() {
        return baogu_imageurl;
    }

    public void setBaogu_imageurl(String baogu_imageurl) {
        this.baogu_imageurl = baogu_imageurl;
    }

    public long getBaogu_send_time() {
        return baogu_send_time;
    }

    public void setBaogu_send_time(long baogu_send_time) {
        this.baogu_send_time = baogu_send_time;
    }


    @Override
    public void action(Context context, BaseMessage message) {
        if (!TextUtil.isEmpty(baogu_imageurl)) {
            Intent intent = new Intent(context, ImageDetailActivity.class);
            intent.putExtra(ImageDetailActivity.EXTRA_RUL, baogu_imageurl);
            intent.putExtra(ImageDetailActivity.EXTRA_RULS, baogu_imageurl);
            context.startActivity(intent);
        }
    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        LogUtil.d(getClass(), "status=" + message.getStatus());
        int roundpx = context.getResources().getDimensionPixelSize(R.dimen.message_round_agle);
        View view = null;
        if (message.getMessageSRC() == MessageSRC.TO) {
            view = LayoutInflater.from(context).inflate(R.layout.message_mine_image_item, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.message_from_image_item, null);
        }

        if(message.getStatus()==1){
            TextView markText = (TextView) view.findViewById(R.id.mark);
            markText.setVisibility(View.VISIBLE);
        }

        ImageView image = (ImageView) view.findViewById(R.id.image);
        ImageView headImage = (ImageView) view.findViewById(R.id.headImage);
        UserInfo userInfo = message.getUserinfo();
        ImageMessageJson imageJson = (ImageMessageJson) message.getMessage();
        if (userInfo != null) {
            if(userInfo.getUserid().equals(Const.user.getUserid())){
                fetcher.loadImage(Const.user.getProfileimg(),headImage,roundpx);
            }else{
                fetcher.loadImage(userInfo.getHeadImage(),headImage,roundpx);
            }
        }
        if (TextUtil.isEmpty(message.getLocalPath())) {
            if (imageJson != null) {
                if(!TextUtil.isEmpty(imageJson.baogu_imageurl)){
                    fetcher.loadImage(ImageUtil.get2xUrl(imageJson.baogu_imageurl), image);
                }
            }
        } else {
            int size = context.getResources().getDimensionPixelSize(R.dimen.image_upload_size);
            new LoadLocalImage(size,image,message.getLocalPath()).start() ;
        }
        TextView timeText = (TextView) view.findViewById(R.id.timeText);
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
        return view;
    }

    @Override
    public MessageListBean getSessionBean() {
        MessageListBean sessionBean = new MessageListBean();
        sessionBean.setContent("[图片]");
        return sessionBean;
    }
}
