package com.showu.baogu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.bean.CinemaBean;
import com.showu.common.util.TextUtil;

public class CinemasTogetherAdapter extends BaoguBaseAdapter<CinemaBean> {

	public CinemasTogetherAdapter(Context context, List<CinemaBean> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        CinemaBean bean = getItem(position);
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cinemas_tegether, null);
        TextView cinemaName = (TextView) getViewById(convertView, R.id.item_cinemas_together_cinemaName);
        TextView distance = (TextView) getViewById(convertView, R.id.item_cinemas_together_distance);
        TextView location = (TextView) getViewById(convertView, R.id.item_cinemas_together_location);
        cinemaName.setText(bean.getCinemaname());
        distance.setText(TextUtil.getDistance(bean.getDistance() + "") + "km");
        location.setText(bean.getAddress());
        return convertView;
	}

}
