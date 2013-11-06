package com.qinnuan.engine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.activity.message.HelloActivity;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.json.ImageMessageJson;
import com.qinnuan.engine.xmpp.json.LocationJson;
import com.qinnuan.engine.xmpp.json.MessageJson;
import com.qinnuan.engine.xmpp.json.TextMessageJson;
import com.qinnuan.engine.xmpp.json.VoiceJson;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.provider.ChatDBManager;
import com.qinnuan.engine.R;

import java.util.List;

/**
 * Created by showu on 13-7-24.
 */
public class HelloMessageAdapter extends BaoguBaseAdapter<MessageListBean> implements View.OnClickListener{
    ChatDBManager chatDBManager ;
    private ImageFetcher imageFetcher ;
    public HelloMessageAdapter(Context context, List<MessageListBean> list,ImageFetcher fetcher) {
        super(context, list);
        chatDBManager=ChatDBManager.getInstance(mContext);
        this.imageFetcher=fetcher;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MessageListBean bean = getItem(i);
        int roundpx = mContext.getResources().getDimensionPixelSize(R.dimen.message_round_agle);
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.message_hello_item,null) ;
        }
        ImageView headView = (ImageView) view.findViewById(R.id.headImage);
        imageFetcher.loadImage(bean.getHeadImage(),headView,roundpx);
        TextView nameText = (TextView) view.findViewById(R.id.username);
        view.findViewById(R.id.throu_check).setTag(bean);
        view.findViewById(R.id.throu_check).setOnClickListener(this);
        nameText.setText(TextUtil.getProcessText(bean.getName(), mContext));
        LinearLayout layou = (LinearLayout) view.findViewById(R.id.message_layout);
        List<BaseMessage> list = chatDBManager.getMessage(bean.getTargetId()) ;
        layou.removeAllViews();
        for(BaseMessage message:list){
            TextView content = (TextView) LayoutInflater.from(mContext).inflate(R.layout.message_text,null);
            MessageJson  json= message.getMessage() ;
            if(json instanceof TextMessageJson){
                TextMessageJson j = (TextMessageJson)json ;
                content.setText(j.getText());
            }else if(json instanceof VoiceJson){
                content.setText("[声音]");
            }else if(json instanceof ImageMessageJson){
                content.setText("[图片]");
            }else if(json instanceof LocationJson){
                content.setText("[地图]");
            }
            layou.addView(content);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        MessageListBean bean = (MessageListBean) view.getTag();
        ((HelloActivity)mContext).verified(bean.getTargetId());
    }
}
