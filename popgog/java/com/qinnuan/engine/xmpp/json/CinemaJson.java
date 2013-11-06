package com.qinnuan.engine.xmpp.json;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qinnuan.engine.activity.WapActivity;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;

import java.util.List;

/**
 * Created by showu on 13-8-7.
 */
public class CinemaJson extends MessageJson implements View.OnClickListener{
    private List<CinemaItemBean> cinemas;
    private String text;
    private Context mContext ;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<CinemaItemBean> getCinemas() {
        return cinemas;
    }

    public void setCinemas(List<CinemaItemBean> cinemas) {
        this.cinemas = cinemas;
    }

    @Override
    public void action(Context context, BaseMessage message) {

    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        this.mContext=context ;
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.message_cinema, null);
        ImageView headImage = (ImageView) contentView.findViewById(R.id.headImage);
        UserInfo userInfo = message.getUserinfo();
        if (userInfo != null) {
            fetcher.loadImage(userInfo.getHeadImage(), headImage);
        }
        LinearLayout layout = (LinearLayout) contentView.findViewById(R.id.mesageLayout);
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 0);
        for (CinemaItemBean it : cinemas) {
            LinearLayout itemView = (LinearLayout) inflater.inflate(
                    R.layout.message_film_item, null);
            itemView.setLayoutParams(params);
            TextView cinemaName = (TextView) itemView.findViewById(R.id.cinemaname);
            TextView cinameAddress = (TextView) itemView.findViewById(R.id.address);
            ;
            TextView descText = (TextView) itemView.findViewById(R.id.desc);
            ;
            cinemaName.setText(it.getCinemaname());
            cinameAddress.setText(it.getAddress());
            String colorText = "<font color='#2345ef'>" +it.getLinktext()
                    + "</font>";
            String htmlString = TextUtil
                    .getProcessText(it.getFulltext(), context).toString()
                    .replaceAll(it.getLinktext(), colorText);
            descText.setText(Html.fromHtml(htmlString));
            itemView.setTag(it);
            itemView.setOnClickListener(this);
            layout.addView(itemView);
        }
        return contentView;
    }


    @Override
    public MessageListBean getSessionBean() {
        MessageListBean sessionBean = new MessageListBean();
        sessionBean.setContent(text);
        return sessionBean;
    }

    @Override
    public void onClick(View view) {
        CinemaItemBean itemBean = (CinemaItemBean) view.getTag();
        Intent intent = new Intent(mContext, WapActivity.class) ;
        intent.putExtra(WapActivity.EXT_TITLE,itemBean.getCinemaname()) ;
        intent.putExtra(WapActivity.EXT_URL,itemBean.getLinkurl()) ;
        mContext.startActivity(intent);
    }
}
