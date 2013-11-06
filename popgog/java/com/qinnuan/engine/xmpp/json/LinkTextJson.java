package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.activity.WapActivity;
import com.qinnuan.common.util.DateUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageSRC;
import com.qinnuan.common.http.image.util.ImageFetcher;

import java.util.Date;

/**
 * Created by showu on 13-8-8.
 */
public class LinkTextJson extends MessageJson {
    private String text;
    private String linktext;
    private String linkurl;
    private String baogu_send_time;

    @Override
    public void action(Context context, BaseMessage message) {
        Intent intent = new Intent(context, WapActivity.class) ;
        intent.putExtra(WapActivity.EXT_URL,linkurl);
        intent.putExtra(WapActivity.EXT_TITLE,text) ;
        context.startActivity(intent);
    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        int roundpx = context.getResources().getDimensionPixelSize(R.dimen.message_round_agle);
        View convertView;
        UserInfo userInfo = message.getUserinfo();
        if (message.getMessageSRC() == MessageSRC.TO || Const.user.getUserid().equals(userInfo.getUserid())) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.message_mine_text_item, null);
        } else {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.message_from_text_item, null);

        }
        TextView content = (TextView) convertView.findViewById(R.id.text);
        ImageView headImage = (ImageView) convertView.findViewById(R.id.headImage);
        if (userInfo != null) {
            fetcher.setLoadingImage(R.drawable.defaul_head);
            fetcher.loadImage(userInfo.getHeadImage(), headImage, roundpx);
        }

        String colorText = "<font color='#2345ef'>" +linktext
                + "</font>";
        String htmlString = TextUtil
                .getProcessText(text, context).toString()
                .replaceAll(linktext, colorText);
        content.setText(Html.fromHtml(htmlString));
//        content.setText(TextUtil.getProcessText(text, context));
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
        MessageListBean listBean = new MessageListBean();
        listBean.setContent(text);
        return listBean;
    }
}
