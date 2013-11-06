package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qinnuan.engine.activity.WapActivity;
import com.qinnuan.engine.R;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.http.image.util.ImageFetcher;

/**
 * Created by showu on 13-8-7.
 */
public class AccountSigleImageTextJson extends MessageJson {
    private String title;
    private String imgurl;
    private String linkurl;
    private String summary;
    private String createdate;
    private long baogu_send_time;
    private Context mContext;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public long getBaogu_send_time() {
        return baogu_send_time;
    }

    public void setBaogu_send_time(long baogu_send_time) {
        this.baogu_send_time = baogu_send_time;
    }

    @Override
    public void action(Context context, BaseMessage message) {

        Intent intent = new Intent(mContext, WapActivity.class);
        intent.putExtra(WapActivity.EXT_TITLE, title);
        intent.putExtra(WapActivity.EXT_URL, linkurl);
        mContext.startActivity(intent);
    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        this.mContext = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.message_sigle_item, null);
        TextView titleText, timeText, summaryText;
        timeText = (TextView) view.findViewById(R.id.time);
        titleText = (TextView) view.findViewById(R.id.title);
        summaryText = (TextView) view.findViewById(R.id.summary);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        fetcher.loadImage(imgurl,imageView);
        params.width = 720;
        params.height = 400;
        timeText.setText(createdate);
        titleText.setText(title);
        summaryText.setText(summary);
        return view;
    }

    @Override
    public MessageListBean getSessionBean() {
        MessageListBean sessionBean = new MessageListBean();
        sessionBean.setContent(title);
        return sessionBean;
    }


}
