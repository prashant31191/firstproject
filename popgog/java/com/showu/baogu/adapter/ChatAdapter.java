package com.showu.baogu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.filmFan.FanInfoActivity;
import com.showu.baogu.xmpp.json.UserInfo;
import com.showu.baogu.xmpp.message.BaseMessage;
import com.showu.baogu.xmpp.message.MessageType;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.DateUtil;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;

import org.apache.commons.logging.Log;

import java.util.Date;
import java.util.List;

/**
 * Created by showu on 13-7-15.
 */
public class ChatAdapter extends BaseAdapter implements View.OnClickListener {
    private ImageFetcher imageFetcher;
    private Context mContext ;
    private List<BaseMessage> list ;
    public ChatAdapter(Context context, List<BaseMessage> list, ImageFetcher imageFetcher) {
        this.mContext=context ;
        this.imageFetcher = imageFetcher;
        this.list=list ;
    }

    @Override
    public int getCount() {
        LogUtil.d(getClass(),"getCount="+list.size());
        return  list.size() ;
    }

    @Override
    public BaseMessage getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LogUtil.d(getClass(), "getView() type=" + getItem(i).getMessageType());
        return getItem(i).getItemView(mContext, imageFetcher, this);
    }

    // 初始化时间
    private View getTimeChatView(BaseMessage message) {
        View convertView;
        convertView = LayoutInflater.from(mContext).inflate(
                R.layout.message_time_item, null);
        TextView timeView = (TextView) convertView.findViewById(R.id.timeText);
        Date date = DateUtil.getDate("1970-01-01 00:00:00");
        Date d = new Date(date.getTime() + message.getSendTime() * 1000
                + (8 * 3600000));
        UserInfo userInfo = message.getUserinfo();
        if (userInfo != null) {
            timeView.setText(DateUtil.getDateString(d, "yyyy-MM-dd HH:mm"));
        }
        return convertView;
    }


    public void removeMessage(BaseMessage message) {
        list.remove(message);
    }

    @Override
    public void onClick(View view) {
        BaseMessage message = (BaseMessage) view.getTag();
        Intent intent = new Intent(mContext, FanInfoActivity.class);
        if (message.getRoom().equals("0")) {
            intent.putExtra(FanInfoActivity.FAN_ID, message.getTargetId());
        } else {
            UserInfo userInfo = message.getUserinfo();
            intent.putExtra(FanInfoActivity.FAN_ID, userInfo.getUserid());
        }
        mContext.startActivity(intent);
    }
}
