package com.qinnuan.engine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.showu.baogu.R;
import com.qinnuan.engine.bean.Coupon;

import java.util.List;

/**
 * Created by showu on 13-7-25.
 */
public class CouponAdapter extends BaoguBaseAdapter<Coupon> {

    public CouponAdapter(Context context, List<Coupon> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Coupon bean = getItem(i) ;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_coupon,null) ;
        }
        TextView money = (TextView) view.findViewById(R.id.item_coupon_money);
        TextView date = (TextView) view.findViewById(R.id.item_coupon_date);
        TextView markedText = (TextView)view.findViewById(R.id.marked) ;
        money.setText(bean.getCashamount()+"元兑换券");
        date.setText(bean.getExpireddate());
        //状态0=未使用1=使用中2=已使用3已过期
        String ma="未使用" ;
        if(bean.getStatus()==1){
           ma="使用中"  ;
        }else if(bean.getStatus()==2){
            ma="已使用"  ;
        }else if(bean.getStatus()==3){
            ma="已过期"  ;
        }
        markedText.setText(ma);
        return view;
    }

}
