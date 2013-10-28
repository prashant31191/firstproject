package com.showu.baogu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaoguBaseAdapter<T> extends BaseAdapter {
	protected List<T> modelList;
	protected Context mContext;

	public BaoguBaseAdapter(Context context, List<T> list) {
		this.mContext = context;
		this.modelList = list;
	}

    public BaoguBaseAdapter(Context context) {
        this.mContext = context;
    }

	@Override
	public int getCount() {
		return modelList.size();
	}

	public List<T> getModelList() {
		return modelList;
	}

	@Override
	public T getItem(int position) {
		return modelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

    public View getViewById(View view, int id) {
        return view.findViewById(id);
    }
}
