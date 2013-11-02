package com.qinnuan.engine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.bean.film.CityBean;
import com.showu.baogu.R;
import com.qinnuan.engine.bean.film.ProvinceBean;

import java.util.ArrayList;
import java.util.List;

public class ProvinceAdapter extends BaseExpandableListAdapter {
	private List<ProvinceBean> list = new ArrayList<ProvinceBean>();
	private Context mContext;
	private CityBean selectedCity;

	public void setSelectedCity(CityBean selectedCity) {
		this.selectedCity = selectedCity;
	}

	public ProvinceAdapter(Context context) {
		this.mContext = context;
	}

	public List<ProvinceBean> getList() {
		return list;
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return list.get(groupPosition).getCityList().size();
	}

	@Override
	public ProvinceBean getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public CityBean getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).getCityList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ProvinceBean provinceBean = list.get(groupPosition);
		if (groupPosition == 0) {
			convertView = inflate(R.layout.city_item_first, null);
		} else if (groupPosition == getGroupCount() - 1) {
			if (isExpanded) {
				convertView = inflate(R.layout.city_item_midle, null);
			} else {
				convertView = inflate(R.layout.city_item_last, null);
			}
		} else {
			convertView = inflate(R.layout.city_item_midle, null);
		}

		ImageView arrows = (ImageView) convertView.findViewById(R.id.arrows);
		if (isExpanded) {
			arrows.setImageResource(R.drawable.expend_on);
		} else {
			arrows.setImageResource(R.drawable.expend_off);
		}
		TextView name = (TextView) convertView.findViewById(R.id.localName);
		name.setText(provinceBean.getProvincename()); return convertView; }
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		CityBean city = list.get(groupPosition).getCityList()
				.get(childPosition);
		if (groupPosition == list.size() - 1
				&& childPosition == list.get(groupPosition).getCityList().size() - 1) {
			convertView = inflate(R.layout.city_item_last, null);
		} else {
			convertView = inflate(R.layout.city_item_midle, null);
		}
		TextView name = (TextView) convertView.findViewById(R.id.localName);
		name.setText("      " + city.getLocalname());
		ImageView arrows = (ImageView) convertView.findViewById(R.id.arrows);
		if (selectedCity.getLocalid().equals(city.getLocalid())) {
			arrows.setImageResource(R.drawable.gou);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private View inflate(int resource, ViewGroup root) {
		return LayoutInflater.from(mContext).inflate(resource, null);
	}
}
