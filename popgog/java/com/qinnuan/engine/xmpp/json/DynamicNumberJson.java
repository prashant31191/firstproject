package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.view.View;

import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.SessionType;
import com.qinnuan.common.http.image.util.ImageFetcher;

/**
 * Created by showu on 13-8-14.
 */
public class DynamicNumberJson extends MessageJson {
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
        return null;
    }

    @Override
    public MessageListBean getSessionBean() {
        MessageListBean sessionBean = new MessageListBean();
        sessionBean.setType(SessionType.DYNAMICNUMBER);
        return sessionBean;
    }
}
