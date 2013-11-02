package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.qinnuan.engine.activity.ImageDetailActivity;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.util.DateUtil;
import com.qinnuan.common.util.ImageUtil;
import com.qinnuan.common.util.TextUtil;

import java.util.Date;

/**
 * Created by showu on 13-8-7.
 */
public class AccountImageJson extends MessageJson {
    private String baogu_imageurl;
    private long baogu_send_time;

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
        int roundpx = context.getResources().getDimensionPixelSize(R.dimen.message_round_agle);
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.message_from_image_item, null);
        if (message.getStatus() == 1) {
            TextView markText = (TextView) view.findViewById(R.id.mark);
            markText.setVisibility(View.VISIBLE);
        }

        ImageView image = (ImageView) view.findViewById(R.id.image);
        ImageView headImage = (ImageView) view.findViewById(R.id.headImage);
        UserInfo userInfo = message.getUserinfo();
        if (userInfo != null) {
            fetcher.loadImage(userInfo.getHeadImage(), headImage, roundpx);
        }
        if (!TextUtil.isEmpty(baogu_imageurl))
            fetcher.loadImage(ImageUtil.get2xUrl(baogu_imageurl), image);
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
