package com.showu.baogu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.film.EtickitActivity;
import com.showu.baogu.bean.film.EcinemaBean;
import com.showu.baogu.bean.film.EtitckBean;
import com.showu.common.util.GUIUtil;

import java.util.List;

/**
 * Created by showu on 13-8-9.
 */
public class EtickitAdapter extends BaoguBaseAdapter<EcinemaBean> implements View.OnClickListener {
    public EtickitAdapter(Context context, List<EcinemaBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        EcinemaBean ecinemaBean = getItem(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.etickt_item, null);
        }
        TextView cinemaNameText = (TextView) view.findViewById(R.id.cinemaName);
        TextView cinemaAddressText= (TextView) view.findViewById(R.id.address);
        TextView brenceText = (TextView) view.findViewById(R.id.brance);
        TextView priceText = (TextView) view.findViewById(R.id.price);
        cinemaNameText.setText(ecinemaBean.getCinemaname());
        brenceText.setText(ecinemaBean.getDiscount());
        cinemaAddressText.setText(ecinemaBean.getAddress());
        priceText.setText("￥"+ecinemaBean.getPrice());
        LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.buyLayout);
        buttonLayout.removeAllViews();
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
        params.setMargins(0,0,50,0);
        for(EtitckBean bean:ecinemaBean.getTicket()){
            bean.setParentCinema(ecinemaBean);
            TextView button=new TextView(mContext) ;
            button.setBackgroundResource(R.drawable.round_yellow_bg);
            button.setOnClickListener(this);
            button.setTextColor(mContext.getResources().getColor(R.color.white));
            String d="2D" ;
            if(bean.getTickettype()==1){
                d="3D";
            }
            button.setText(d + "/￥" + bean.getPrice());
            button.setLayoutParams(params);
            button.setTag(bean);
            button.setOnClickListener(this);
            buttonLayout.addView(button);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
       EtitckBean b = (EtitckBean) view.getTag();
        ((EtickitActivity)mContext).buyTickt(b);
    }
}
