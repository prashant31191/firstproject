package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinnuan.engine.activity.WapActivity;
import com.qinnuan.engine.R;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.util.ImageUtil;

import java.util.List;

/**
 * Created by showu on 13-8-7.
 */
public class AccountMultImageTextJson extends MessageJson implements View.OnClickListener {
    private SubjectBean subject;
    private Context mContext;
    private List<RefsubjectBean> refsubjects;
    private long baogu_send_time ;

    public long getBaogu_send_time() {
        return baogu_send_time;
    }

    public void setBaogu_send_time(long baogu_send_time) {
        this.baogu_send_time = baogu_send_time;
    }

    public SubjectBean getSubject() {
        return subject;
    }

    public void setSubject(SubjectBean subject) {
        this.subject = subject;
    }

    public List<RefsubjectBean> getRefsubjects() {
        return refsubjects;
    }

    public void setRefsubjects(List<RefsubjectBean> refsubjects) {
        this.refsubjects = refsubjects;
    }

    @Override
    public void action(Context context, BaseMessage message) {

    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        this.mContext = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.message_multi_item, null);
        TextView titleText = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        int weidth = imageView.getWidth();
        params.height = 400;
        params.width = 720;
        view.setOnClickListener(this);
        view.setTag(subject);
        imageView.setLayoutParams(params);
        LinearLayout itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
        titleText.setText(subject.getTitle());
        fetcher.loadImage(subject.getImgurl(), imageView);
        initItem(itemLayout, mInflater, fetcher);
        return view;
    }

    @Override
    public MessageListBean getSessionBean() {
        MessageListBean messageListBean = new MessageListBean();
        messageListBean.setContent(subject.getTitle());
        return messageListBean;
    }

    private void initItem(LinearLayout itemLayout, LayoutInflater mInflater, ImageFetcher fetcher) {
        itemLayout.removeAllViews();
        for (RefsubjectBean bean : refsubjects) {
            ImageView image = new ImageView(mContext);
//			image.setImageResource(R.drawable.message_link_line);
            image.setImageDrawable(new ColorDrawable(Color.GRAY));
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            itemLayout.addView(image);
            FrameLayout layout = (FrameLayout) mInflater.inflate(
                    R.layout.message_multi_item_item, null);
            layout.setTag(bean);
            layout.setTag(bean);
            layout.setOnClickListener(this);
            TextView nameText = (TextView) layout.findViewById(R.id.time);
            ImageView itemImageView = (ImageView) layout
                    .findViewById(R.id.image);
            fetcher.loadImage(ImageUtil.get2xUrl(bean.getImgurl()), itemImageView);
            nameText.setText(bean.getTitle());
            itemLayout.addView(layout);
        }
    }

    @Override
    public void onClick(View view) {
        Object o = view.getTag();
        if (o instanceof RefsubjectBean) {
            RefsubjectBean bean = (RefsubjectBean) o;
            Intent intent = new Intent(mContext, WapActivity.class);
            intent.putExtra(WapActivity.EXT_URL, bean.getLinkurl());
            intent.putExtra(WapActivity.EXT_TITLE, bean.getTitle());
            mContext.startActivity(intent);
        } else if (o instanceof SubjectBean) {
            SubjectBean bean = (SubjectBean) o;
            Intent intent = new Intent(mContext, WapActivity.class);
            intent.putExtra(WapActivity.EXT_URL, bean.getLinkurl());
            intent.putExtra(WapActivity.EXT_TITLE, bean.getTitle());
            mContext.startActivity(intent);
        }

    }
}
