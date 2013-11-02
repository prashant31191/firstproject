package com.qinnuan.common.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.showu.baogu.R;

public class BottomPopWindow extends LinearLayout {
	public interface OnMenuSelect {
		public void onItemMenuSelect(int position);

		public void onCancelSelect();
	}

	private OnMenuSelect menuSelectListener;
	List<String> nameList = new ArrayList<String>();
	private Context mContext;
	private LinearLayout contentView;
	private String title ;
	public void setMenuSelectListener(OnMenuSelect menuSelectListener) {
		this.menuSelectListener = menuSelectListener;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getNameList() {
		return nameList;
	}

	public void setMenu(String[] menuName) {
		nameList.clear() ;
		for (String name : menuName) {
			nameList.add(name);
		}
		notifyDateSetChanged() ;
	}

	public BottomPopWindow(Context context, OnMenuSelect menuSelect) {
		super(context);
		this.mContext = context;
		this.menuSelectListener = menuSelect;
		LayoutInflater.from(context).inflate(R.layout.bottom_pop_layout, this);
		contentView = (LinearLayout) findViewById(R.id.contentView);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) ;
		setLayoutParams(p) ;
	}

	public void notifyDateSetChanged() {
		initButton();
	}

	private void initButton() {
		contentView.removeAllViews() ;
		if(title!=null&&title.trim().length()>0){
			
		}
		int textSize = getContext().getResources().getDimensionPixelSize(R.dimen.buttom_pop_text);
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
		params.setMargins(32, 20, 32, 0) ;
		int i = 0;
		for (String name : nameList) {
			Button button = new Button(mContext);
			button.setText(name);
			button.setTextSize(textSize) ;
            button.setTextColor(getResources().getColor(R.color.title_bar_bg));
			button.setLayoutParams(params);
			button.setBackgroundResource(R.drawable.btn_pop_item_bg);
			button.setId(i);
			i++;
			button.setOnClickListener(onClickListener);
			contentView.addView(button);
		}
		Button button = new Button(mContext);
		button.setText("取消");
		button.setTextSize(textSize);
        button.setTextColor(getResources().getColor(R.color.white));
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.btn_pop_cancel_bg);
		button.setGravity(Gravity.CENTER);
		LayoutParams bottomParams= new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		bottomParams.setMargins(32, 20, 32, 20);
		button.setId(i);
		button.setLayoutParams(bottomParams);
		button.setOnClickListener(onClickListener);
		contentView.addView(button);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == nameList.size()) {
				if (menuSelectListener != null) {
					menuSelectListener.onCancelSelect();
				}
			} else {
				if (menuSelectListener != null) {
					menuSelectListener.onItemMenuSelect(v.getId());
				}
			}
		}
	};
}
