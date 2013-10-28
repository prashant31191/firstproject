package com.showu.baogu.xmpp.json;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.xmpp.message.BaseMessage;
import com.showu.common.http.image.util.ImageFetcher;

/**
 * Created by showu on 13-7-16.
 */
public abstract class MessageJson {
    public abstract void action(Context context,BaseMessage message);
    public abstract View getItem(Context context,BaseMessage message,ImageFetcher fetcher);
    public abstract MessageListBean getSessionBean() ;
}
