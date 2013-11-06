package com.qinnuan.engine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qinnuan.engine.bean.user.OnlineOrder;
import com.qinnuan.engine.R;

import java.util.List;

/**
 * Created by showu on 13-7-25.
 */
public class OnlineOrderAdapter extends BaoguBaseAdapter<OnlineOrder> {

    public OnlineOrderAdapter(Context context, List<OnlineOrder> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        OnlineOrder bean = getItem(i) ;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_online_order, null) ;
        }
        TextView date = (TextView) view.findViewById(R.id.item_onlineticket_order_date);
        TextView orderno= (TextView) view.findViewById(R.id.item_onlineticket_order_no);
        TextView cinema = (TextView) view.findViewById(R.id.item_onlineticket_order_cinema);
        TextView film = (TextView) view.findViewById(R.id.item_onlineticket_order_film);
        TextView status = (TextView) view.findViewById(R.id.item_onlineticket_order_status);

        date.setText(bean.getCreatedate());
        orderno.setText(bean.getUorderid());
        cinema.setText(bean.getCinemaname());
        film.setText(bean.getFilmname());
        status.setText(bean.getStatusremark());

        return view;
    }

}
