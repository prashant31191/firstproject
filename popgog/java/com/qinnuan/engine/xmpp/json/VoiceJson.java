package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.josn.EAJson;
import com.qinnuan.common.util.DateUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.xmpp.message.MessageSRC;

import java.util.Date;

/**
 * Created by showu on 13-7-18.
 */
public class VoiceJson extends MessageJson{
    @EAJson
    private String baogu_voiceurl ;
    @EAJson
    private long baogu_send_time ;
    @EAJson
    private  float baogu_audio_length;




    public String getBaogu_voiceurl() {
        return baogu_voiceurl;
    }

    public void setBaogu_voiceurl(String baogu_voiceurl) {
        this.baogu_voiceurl = baogu_voiceurl;
    }

    public long getBaogu_send_time() {
        return baogu_send_time;
    }

    public void setBaogu_send_time(long baogu_send_time) {
        this.baogu_send_time = baogu_send_time;
    }

    public double getBaogu_audio_length() {
        return baogu_audio_length;
    }

    public void setBaogu_audio_length(int baogu_audio_length) {
        this.baogu_audio_length = baogu_audio_length;
    }

    @Override
    public void action(Context context,BaseMessage message) {

    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        int roundpx = context.getResources().getDimensionPixelSize(R.dimen.message_round_agle) ;
        View convertView;
        UserInfo userInfo= message.getUserinfo();
        if (message.getMessageSRC() == MessageSRC.TO) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.message_mine_voice_item, null);
            TextView lenthText = (TextView) convertView
                    .findViewById(R.id.length);
            TextView lengthText = (TextView) convertView
                    .findViewById(R.id.length_text);
            lenthText.setText(baogu_audio_length+ "s");
            // LogUtil.e(getClass(), message.baogu_audio_length+"||"+lent) ;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < baogu_audio_length; i++) {
                sb.append(" ");
            }
            lengthText.setText(sb.toString());
            ImageView headImage = (ImageView) convertView.findViewById(R.id.headImage);
            fetcher.loadImage(Const.user.getProfileimg(),headImage,roundpx);
        } else {// 收到声音消息
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.message_from_voice_item, null);
            TextView lenthText = (TextView) convertView
                    .findViewById(R.id.length);
            TextView spaceText = (TextView) convertView
                    .findViewById(R.id.length_text);
            lenthText.setText(baogu_audio_length + "s");
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < baogu_audio_length; i++) {
                sb.append(" ");
            }
            spaceText.setText(sb.toString());

            if(userInfo!=null){
                ImageView headImage = (ImageView) convertView.findViewById(R.id.headImage);
                fetcher.setLoadingImage(R.drawable.defaul_head);
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
        MessageListBean sessionBean = new MessageListBean() ;
        sessionBean.setContent("[声音]");
        return sessionBean;
    }
}
