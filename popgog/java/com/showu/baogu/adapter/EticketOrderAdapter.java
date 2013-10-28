package com.showu.baogu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.bean.user.EticketOrder;
import com.showu.baogu.bean.user.OnlineOrder;
import com.showu.common.util.LogUtil;

import java.util.List;

/**
 * Created by showu on 13-7-25.
 */
public class EticketOrderAdapter extends BaoguBaseAdapter<EticketOrder> {

    public EticketOrderAdapter(Context context, List<EticketOrder> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        EticketOrder bean = getItem(i) ;
        LogUtil.e(getClass(), "bean==>"+bean);
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_eticket_order, null) ;
        }
        TextView date = (TextView) view.findViewById(R.id.item_eticket_order_date);
        TextView orderno= (TextView) view.findViewById(R.id.item_eticket_order_no);
        TextView cinema = (TextView) view.findViewById(R.id.item_eticket_order_cinema);
        TextView type = (TextView) view.findViewById(R.id.item_eticket_order_type);
        TextView status = (TextView) view.findViewById(R.id.item_eticket_order_status);

        date.setText(bean.getCreatedate());
        orderno.setText(bean.getUorderid());
        cinema.setText(bean.getCinemaname());
        //类型 0=2D 1=3D
        int ticket_type = bean.getTickettype();
        if(ticket_type ==0) {
            type.setText("2D 电子券"+" ("+ bean.getTicketcount() +")");
        } else if(ticket_type == 1) {
            type.setText("3D 电子券"+" ("+ bean.getTicketcount() +")");
        }
        status.setText(bean.getStatusremark());

        return view;
    }

}
