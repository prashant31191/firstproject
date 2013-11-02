package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.common.util.DateUtil;
import com.qinnuan.common.util.TextUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageSRC;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.josn.EAJson;

import java.util.Date;

/**
 * Created by showu on 13-7-15.
 */
public class TextMessageJson extends MessageJson {
    @EAJson
    private String text ;
    @EAJson
    private  long baogu_send_time ;

    public TextMessageJson() {
    }

    public TextMessageJson(String text) {
        this.text = text;
    }

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
    public void action(Context context,BaseMessage message) {

    }

    @Override
    public View getItem(Context context,BaseMessage message,ImageFetcher fetcher) {
        int roundpx = context.getResources().getDimensionPixelSize(R.dimen.message_round_agle) ;
        View convertView;
        UserInfo userInfo = message.getUserinfo() ;
        if (message.getMessageSRC() == MessageSRC.TO|| Const.user.getUserid().equals(userInfo.getUserid())) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.message_mine_text_item, null);
        } else {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.message_from_text_item, null);

        }
        TextView content = (TextView) convertView.findViewById(R.id.text);
        ImageView headImage = (ImageView) convertView.findViewById(R.id.headImage);
        TextMessageJson textJson = (TextMessageJson) message.getMessage();
        if(userInfo!=null){
                fetcher.setLoadingImage(R.drawable.defaul_head);
                  if(userInfo.getUserid().equals(Const.user.getUserid())){
                    fetcher.loadImage(Const.user.getProfileimg(),headImage,roundpx);
                  }else{
                    fetcher.loadImage(userInfo.getHeadImage(),headImage,roundpx);
                  }
        }
        if(textJson!=null){
        content.setText(TextUtil.getProcessText(textJson.getText(), context));
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
        sessionBean.setContent(text);
        return sessionBean;
    }
}
