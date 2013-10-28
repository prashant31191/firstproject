package com.showu.baogu.xmpp.json;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.filmFan.PublicInfoActivity;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.xmpp.message.BaseMessage;
import com.showu.common.http.image.util.ImageFetcher;

import java.util.List;

/**
 * Created by showu on 13-8-7.
 */
public class AccountJson extends MessageJson  implements View.OnClickListener{
    private List<AccountItemBean> accounts ;
    private String text ;
    private Context mContext ;
    @Override
    public void action(Context context, BaseMessage message) {

    }

    @Override
    public View getItem(Context context, BaseMessage message, ImageFetcher fetcher) {
        this.mContext=context ;
        LayoutInflater inflater = LayoutInflater.from(context) ;
        View contentView = inflater.inflate(R.layout.message_account,null);
        LinearLayout layout = (LinearLayout) contentView.findViewById(R.id.item_layout);
        for(AccountItemBean bean : accounts){
            View itemView = inflater.inflate(R.layout.message_account_item,null) ;
            ImageView iconImage = (ImageView) itemView.findViewById(R.id.account_image);
            fetcher.loadImage(bean.getProfileimg(),iconImage);
            TextView nameText = (TextView) itemView.findViewById(R.id.name);
            TextView sigletrueText = (TextView) itemView.findViewById(R.id.sigletrue);
            nameText.setText(bean.getNickname());
            sigletrueText.setText(bean.getSignature());
            itemView.setOnClickListener(this);
            itemView.setTag(bean);
            layout.addView(itemView);
        }
        return contentView;
    }

    @Override
    public MessageListBean getSessionBean() {
        MessageListBean sessionBean =new MessageListBean();
        sessionBean.setContent(text);
        return sessionBean;
    }

    @Override
    public void onClick(View view) {
       AccountItemBean bean = (AccountItemBean) view.getTag();
        Intent intent=new Intent(mContext, PublicInfoActivity.class) ;
        intent.putExtra(PublicInfoActivity.PUBLIC_FAN_ID,bean.getUserid()) ;
        mContext.startActivity(intent);
    }
}
