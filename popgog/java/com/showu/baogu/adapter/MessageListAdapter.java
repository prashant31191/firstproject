package com.showu.baogu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.GroupActivity;
import com.showu.baogu.activity.filmFan.FanInfoActivity;
import com.showu.baogu.activity.filmFan.MyFriendActivity;
import com.showu.baogu.activity.filmFan.PublicInfoActivity;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.xmpp.message.ComparatorMessage;
import com.showu.baogu.xmpp.message.SessionType;
import com.showu.baogu.xmpp.provider.SessionManager;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.DateUtil;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;
import com.showu.common.widget.PressImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageListAdapter extends BaoguBaseAdapter<MessageListBean> {
    private List<MessageListBean> list = new ArrayList<MessageListBean>();
    private List<MessageListBean> showList = new ArrayList<MessageListBean>();
    private Map<String, View> mapView = new HashMap<String, View>();
    private Map<String, MessageListBean> beanMap = new HashMap<String, MessageListBean>();
    private SessionManager sessionContentPro;
    private ImageFetcher mImageFatcher;

    public MessageListAdapter(Context context, List<MessageListBean> list, ImageFetcher fetcher) {
        super(context, list);
        this.mImageFatcher = fetcher;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageListBean bean = getItem(position);
        if (bean.getType() == SessionType.MESSAGE
                || bean.getType() == SessionType.HELLO || bean.getType() == SessionType.HELP || bean.getType() == SessionType.ACCOUNT) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.message_item, null);
            initConvertView(convertView, bean);
        } else {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.message_list_time_item, null);
            TextView timeText = (TextView) convertView
                    .findViewById(R.id.messageTime);
            Date date = DateUtil.getDate("1970-01-01 00:00:00");
            Date d = new Date(date.getTime() + bean.getTime() * 1000
                    + (8 * 3600000));
            timeText.setText(DateUtil.getDayName(d, "yyyy-MM-dd"));
        }
        return convertView;
    }

    private View getFilmTrendView(MessageListBean bean) {
        View convertView = LayoutInflater.from(mContext).inflate(
                R.layout.message_item_trend, null);
        // convertView.findViewById(R.id.treandLayout).setOnClickListener(
        // onClickListener);
        convertView.findViewById(R.id.treandLayout).setTag(bean);
        TextView numberView = (TextView) convertView
                .findViewById(R.id.messageNumber);
        if (bean.getCount() > 0) {
            numberView.setVisibility(View.VISIBLE);
//            numberView.setText(bean.getCount() + "");
        } else {
            numberView.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private void initConvertView(View convertView, MessageListBean bean) {
        TextView timeText = (TextView) convertView
                .findViewById(R.id.messageTime);
        Date date = DateUtil.getDate("1970-01-01 00:00:00");
        Date d = new Date(date.getTime() + bean.getTime() * 1000
                + (8 * 3600000));
        timeText.setText(DateUtil.getDayName(d, "yyyy-MM-dd"));
        if (!bean.isNeedTime()) {
            convertView.findViewById(R.id.timeLayout).setVisibility(View.GONE);
        }
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView content = (TextView) convertView
                .findViewById(R.id.message_content);
        TextView name = (TextView) convertView.findViewById(R.id.username);
        TextView numberView = (TextView) convertView
                .findViewById(R.id.messageNumber);
        if (bean.getType() == SessionType.MESSAGE || bean.getType() == SessionType.ACCOUNT) {
            if (bean.getMembercount() > 0) {
                name.setText(TextUtil.getProcessText("(" + bean.getMembercount() + "人)" + bean.getName(), mContext));
            } else {
                name.setText(TextUtil.getProcessText(bean.getName(), mContext));
            }
        } else if (bean.getType() == SessionType.HELLO || bean.getType() == SessionType.HELP) {
            name.setText(TextUtil.getProcessText(bean.getName(), mContext));
        }

        if (bean.getCount() > 0) {
            numberView.setVisibility(View.VISIBLE);
            numberView.setText(bean.getCount() + "");
        } else {
            numberView.setVisibility(View.INVISIBLE);
        }
        DateFormat format = DateFormat.getDateInstance();
        try {
            Date da = format.parse("1970-01-01 00:00:00");
            Date db = new Date(da.getTime() + bean.getTime() * 1000
                    + (8 * 3600000));
            time.setText(DateUtil.getDateString(db, "HH:mm"));
        } catch (java.text.ParseException e2) {
            e2.printStackTrace();
        }
        content.setText(TextUtil.getProcessText(bean.getContent(), mContext));
        if (TextUtil.isEmpty(Const.user.getUserid())) {
            PressImageView headView = (PressImageView) convertView
                    .findViewById(R.id.head_fram);
            TextView nameText = (TextView) convertView
                    .findViewById(R.id.username);
            nameText.setText(bean.getName());
            headView.getImageView().setImageResource(R.drawable.logo);
        }
        PressImageView headView = (PressImageView) convertView
                .findViewById(R.id.head_fram);
        headView.setTag(bean);
        if (bean.getType() == SessionType.HELLO) {
            headView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.hello_head));
        } else {
            int roundpx = mContext.getResources().getDimensionPixelSize(R.dimen.message_round_agle);
            mImageFatcher.loadImage(bean.getHeadImage(), headView.getImageView(), roundpx);
        }
        TextView distanceTextView = (TextView) convertView
                .findViewById(R.id.distance);
//        distanceTextView.setText("[" + TextUtil.getDistance(bean.getDistanc())
//                + "km]");
        if (bean.getType() == SessionType.MESSAGE||bean.getType()==SessionType.ACCOUNT) {
            FrameLayout layout = (FrameLayout) convertView.findViewById(R.id.head_fram);
            layout.setTag(bean);
            layout.setOnClickListener(onClickListener);
        }
    }


    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head_fram:
                    onHeadViewClick((MessageListBean) v.getTag());
                    break;
            }
        }
    };

    private void onHeadViewClick(MessageListBean bean) {
        if (bean.getRoom().equals("0")) {
            if (bean.getType() != SessionType.HELLO) {
                if(bean.getType()==SessionType.ACCOUNT){
                    Intent intent = new Intent(mContext, PublicInfoActivity.class);
                    intent.putExtra(PublicInfoActivity.PUBLIC_FAN_ID, bean.getTargetId());
                    mContext.startActivity(intent);
                }else{
                Intent intent = new Intent(mContext, MyFriendActivity.class);
                intent.putExtra(MyFriendActivity.FAN_ID, bean.getTargetId());
                mContext.startActivity(intent);
                }
            }
        } else {
            Intent intent = new Intent(mContext, GroupActivity.class);
            intent.putExtra(GroupActivity.EXT_GROUP_ID, bean.getRoom());
            mContext.startActivity(intent);
        }
    }


}
