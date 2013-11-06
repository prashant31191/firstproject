package com.qinnuan.engine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qinnuan.engine.bean.Cash;
import com.qinnuan.engine.R;

import java.util.List;

public class UserCashAdapter extends BaoguBaseAdapter<Cash> {

	private ViewHolder holder;

	public UserCashAdapter(Context context, List<Cash> cashs) {
        super(context, cashs);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_cash, null);
			convertView.setTag(holder);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_user_cash_time);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.item_user_cash_money);
			holder.tvRecord = (TextView) convertView.findViewById(R.id.item_user_cash_record);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Cash cash = getItem(position);
		holder.tvTime.setText(cash.getCreatedate());
		holder.tvMoney.setText("￥" + cash.getCashamount());
		holder.tvRecord.setText(cash.getDescription());
		return convertView;
	}
	
	private class ViewHolder {
		private TextView tvTime, tvMoney, tvRecord;
	}

}
