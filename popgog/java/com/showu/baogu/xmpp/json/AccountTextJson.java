package com.showu.baogu.xmpp.json;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.xmpp.message.BaseMessage;
import com.showu.baogu.xmpp.message.MessageSRC;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.DateUtil;
import com.showu.common.util.TextUtil;

import java.util.Date;

/**
 * Created by showu on 13-8-7.
 */
public class AccountTextJson extends MessageJson {
    private String text;
    private long baogu_send_time;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getBaogu_send_time() {
        return baogu_send_time;
    }

    public void setBaogu_send_time(long baogu_send_time) {
        this.baogu_send_time = baogu_send_time;
    }

    @Override
    public void action(Context context, BaseMessage message) {

    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        int roundpx = context.getResources().getDimensionPixelSize(R.dimen.message_round_agle);
        View convertView;
        UserInfo userInfo = message.getUserinfo();
        convertView = LayoutInflater.from(context).inflate(
                R.layout.message_from_text_item, null);
        TextView content = (TextView) convertView.findViewById(R.id.text);
        ImageView headImage = (ImageView) convertView.findViewById(R.id.headImage);
        if (userInfo != null) {
            fetcher.setLoadingImage(R.drawable.defaul_head);
            fetcher.loadImage(userInfo.getHeadImage(), headImage, roundpx);
        }
            content.setText(TextUtil.getProcessText(text, context));
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
        sessionBean.setContent(text);
        return sessionBean;
    }
}
