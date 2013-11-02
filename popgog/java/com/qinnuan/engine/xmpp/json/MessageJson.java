package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.view.View;

import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.http.image.util.ImageFetcher;

/**
 * Created by showu on 13-7-16.
 */
public abstract class MessageJson {
    public abstract void action(Context context,BaseMessage message);
    public abstract View getItem(Context context,BaseMessage message,ImageFetcher fetcher);
    public abstract MessageListBean getSessionBean() ;
}
