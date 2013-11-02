package com.qinnuan.engine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qinnuan.common.util.TextUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.bean.CinemaBean;

import java.util.List;

/**
 * Created by showu on 13-7-25.
 */
public class CinemaAdapter extends BaoguBaseAdapter<CinemaBean> {
    public CinemaAdapter(Context context, List<CinemaBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       CinemaBean bean = getItem(i) ;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.cinema_item,null) ;
        }
        TextView cinameName = (TextView) view.findViewById(R.id.cinemaName);
        TextView distance= (TextView) view.findViewById(R.id.distance);
        TextView address = (TextView) view.findViewById(R.id.address);
        cinameName.setText(bean.getCinemaname());
        distance.setText(TextUtil.getDistance(bean.getDistance() + "")+"km");
        address.setText(bean.getAddress());
        if(bean.getIsoften()==1){
            view.findViewById(R.id.love).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.love).setVisibility(View.GONE);
        }
        return view;
    }
}
