package com.showu.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.film.ShowInfoBean;
import com.showu.common.util.DateUtil;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShowInfoWidget extends LinearLayout {
	private OnShowInfoClickListener listener;
	private List<ShowInfoBean> todayList = new ArrayList<ShowInfoBean>();
	private List<ShowInfoBean> tomorrrowList = new ArrayList<ShowInfoBean>();
	private List<ShowInfoBean> theDayAfter = new ArrayList<ShowInfoBean>();
	private TextView todayText, tomorrowText, afterText;
	private LinearLayout showNumberLayout;
	private int[] yNumber = { R.drawable.y_0, R.drawable.y_1, R.drawable.y_2,
			R.drawable.y_3, R.drawable.y_4, R.drawable.y_5, R.drawable.y_6,
			R.drawable.y_7, R.drawable.y_8, R.drawable.y_9, };
	private int[] gNumber = { R.drawable.g_0, R.drawable.g_1, R.drawable.g_2,
			R.drawable.g_3, R.drawable.g_4, R.drawable.g_5, R.drawable.g_6,
			R.drawable.g_7, R.drawable.g_8, R.drawable.g_9 };
	private int[] bNumber = { R.drawable.b_0, R.drawable.b_1, R.drawable.b_2,
			R.drawable.b_3, R.drawable.b_4, R.drawable.b_5, R.drawable.b_6,
			R.drawable.b_7, R.drawable.b_8, R.drawable.b_9, };
	private int currentDay = 1;

	private Context mContext;

	public void setListener(OnShowInfoClickListener listener) {
		this.listener = listener;
	}

	public interface OnShowInfoClickListener {
		public void onShowinfoClick(ShowInfoBean showInfo);
	}

	public ShowInfoWidget(Context context) {
		super(context);
		mContext = context;
		LogUtil.e(getClass(), "showinfowidget()");
		init(context);

	}

	public ShowInfoWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(context);
		/*
		 * LayoutInflater.from(context).inflate(R.layout.show_info_layout,
		 * this); showNumberLayout = (LinearLayout)
		 * findViewById(R.id.showNumberLayout); todayText = (TextView)
		 * findViewById(R.id.today); tomorrowText = (TextView)
		 * findViewById(R.id.tomorrow); afterText = (TextView)
		 * findViewById(R.id.afterTomorow);
		 * todayText.setOnClickListener(onClickListener);
		 * tomorrowText.setOnClickListener(onClickListener);
		 * afterText.setOnClickListener(onClickListener);
		 */
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.show_info_layout, this);
		showNumberLayout = (LinearLayout) findViewById(R.id.showNumberLayout);
		todayText = (TextView) findViewById(R.id.today);
		tomorrowText = (TextView) findViewById(R.id.tomorrow);
		afterText = (TextView) findViewById(R.id.afterTomorow);
		todayText.setOnClickListener(onClickListener);
		tomorrowText.setOnClickListener(onClickListener);
		afterText.setOnClickListener(onClickListener);
	}

	public void notifyDateSetChanged(List<ShowInfoBean> list) {
		if (list.size() == 0) {
			this.setVisibility(View.GONE);
		} else {
			//this.setVisibility(View.VISIBLE);
			groupShowByDate(list);
            initTime();
		}

	}

	public void showNumberLayout(List<ShowInfoBean> list) {
		LogUtil.e(getClass(), "showNumberLayout list size==>" + list.size());
		showNumberLayout.removeAllViews();
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		LinearLayout itemLayout = null;
		for (int i = 0; i < list.size(); i++) {
			LogUtil.e(getClass(), "showNumberLayout i==>" + i);
			if (i % 3 == 0) {
				itemLayout = (LinearLayout) LayoutInflater.from(
                        showNumberLayout.getContext()).inflate(R.layout.show,
                        null);
				itemLayout.setLayoutParams(layoutParams);
				showNumberLayout.addView(itemLayout);
			}
			RelativeLayout itemView = (RelativeLayout) LayoutInflater.from(
					showNumberLayout.getContext()).inflate(R.layout.show_item,
					null);
			initItem(itemView, list.get(i), i);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT) ;
            params.setMargins(20,20,0,0);
            params.weight=1 ;
            itemView.setLayoutParams(params);
			itemLayout.addView(itemView);
		}
	}

	private void initItem(View view, ShowInfoBean bean, int postion) {
		ImageView hour1 = (ImageView) view.findViewById(R.id.hour_1);
		ImageView hour2 = (ImageView) view.findViewById(R.id.hour_2);
		ImageView point = (ImageView) view.findViewById(R.id.hour_point);
		ImageView min1 = (ImageView) view.findViewById(R.id.min_1);
		ImageView min2 = (ImageView) view.findViewById(R.id.min_2);
		TextView lanText = (TextView) view.findViewById(R.id.language);
		TextView typeText = (TextView) view.findViewById(R.id.dimensional);
		TextView priceText = (TextView) view.findViewById(R.id.lowestprice);
		String str = bean.getShowtime().replace(":", "");
		char[] times = str.toCharArray();
		if (currentDay != 1 || (currentDay == 1 && canBuy(str))) {
			hour1.setImageResource(yNumber[Integer.parseInt(times[0] + "")]);
			hour2.setImageResource(yNumber[Integer.parseInt(times[1] + "")]);
			point.setImageResource(R.drawable.y_point);
			min1.setImageResource(yNumber[Integer.parseInt(times[2] + "")]);
			min2.setImageResource(yNumber[Integer.parseInt(times[3] + "")]);
			view.setOnClickListener(onClickListener);
			view.setId(R.id.showItem);
			view.setTag(bean);
            lanText.setTextColor(getResources().getColor(R.color.day_on));
            typeText.setTextColor(getResources().getColor(R.color.day_on));
		} else {
			hour1.setImageResource(gNumber[Integer.parseInt(times[0] + "")]);
			hour2.setImageResource(gNumber[Integer.parseInt(times[1] + "")]);
			point.setImageResource(R.drawable.g_point);
			min1.setImageResource(gNumber[Integer.parseInt(times[2] + "")]);
			min2.setImageResource(gNumber[Integer.parseInt(times[3] + "")]);
			view.setBackgroundResource(R.drawable.show_lost);
            lanText.setTextColor(getResources().getColor(R.color.day));
            priceText.setTextColor(getResources().getColor(R.color.day));
            typeText.setTextColor(getResources().getColor(R.color.day));
		}
		lanText.setText(bean.getLanguage());
		typeText.setText(bean.getDimenstional());
			String price = bean.getPrice();
			if((price!=null && !price.trim().equalsIgnoreCase(""))) {
				priceText.setText("￥"+price);
			}
	}

	private boolean canBuy(String time) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(c.MINUTE, 15);
		String str = DateUtil.getDateString(c.getTime(), "HH:mm");
		return Integer.parseInt(time) > Integer.parseInt(str.replace(":", ""));
	}

	private void groupShowByDate(List<ShowInfoBean> list) {
		todayList.clear();
		tomorrrowList.clear();
		theDayAfter.clear();

		Date date = new Date();
		String today = DateUtil.getDateString(date, "yyyy-MM-dd");
		for (ShowInfoBean bean : list) {
			LogUtil.e(getClass(),
					"today==>" + today + ", bean fdate==>" + bean.getShowdate());
			if (today.equals(bean.getShowdate())) {
				todayList.add(bean);
			}
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(c.DATE, 1);
		String tomorrow = DateUtil.getDateString(c.getTime(), "yyyy-MM-dd");
		for (ShowInfoBean bean : list) {
			if (tomorrow.equals(bean.getShowdate())) {
				tomorrrowList.add(bean);
			}
		}
		c.setTime(date);
		c.add(c.DATE, 2);
		String after = DateUtil.getDateString(c.getTime(), "yyyy-MM-dd");
		for (ShowInfoBean bean : list) {
			if (after.equals(bean.getShowdate())) {
				theDayAfter.add(bean);
			}
		}
	}

	private void initTime() {
		LogUtil.e(getClass(), "initTime");
		LogUtil.e(getClass(), "todaylist size==>" + todayList.size()
				+ ", tomorrowlist size==>" + tomorrrowList.size());
		if (todayList.size() > 0) {
			todayText.setVisibility(View.VISIBLE);
			String[] strs = todayList.get(0).getShowdate().split("-");
			todayText.setText("今天 " + strs[1] + "-" + strs[2]);
		} else {
			todayText.setVisibility(View.GONE);
		}
		if (tomorrrowList.size() > 0) {
			tomorrowText.setVisibility(View.VISIBLE);
			String[] strs = tomorrrowList.get(0).getShowdate().split("-");
			tomorrowText.setText("明天 " + strs[1] + "-" + strs[2]);
		} else {
			tomorrowText.setVisibility(View.GONE);
		}
		if (theDayAfter.size() > 0) {
			afterText.setVisibility(View.VISIBLE);
			String[] strs = theDayAfter.get(0).getShowdate().split("-");
			afterText.setText("后天 " + strs[1] + "-" + strs[2]);
		} else {
			afterText.setVisibility(View.GONE);
		}
		if (todayList.size() > 0) {
            currentDay=1;
			showNumberLayout(todayList);
			todayText.setTextColor(getResources().getColor(R.color.day_on));
            tomorrowText.setBackgroundResource(R.color.cinema_bg);
            afterText.setBackgroundResource(R.color.cinema_bg);
		} else if (tomorrrowList.size() > 0) {
            currentDay=2;
			showNumberLayout(tomorrrowList);
            tomorrowText.setTextColor(getResources().getColor(R.color.day_on));
            todayText.setBackgroundResource(R.color.cinema_bg);
            afterText.setBackgroundResource(R.color.cinema_bg);
		} else if (theDayAfter.size() > 0) {
            currentDay=3;
            afterText.setTextColor(getResources().getColor(R.color.day_on));
            tomorrowText.setBackgroundResource(R.color.cinema_bg);
            todayText.setBackgroundResource(R.color.cinema_bg);
			showNumberLayout(theDayAfter);
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.today:
				currentDay = 1;
				showNumberLayout(todayList);
                todayText.setTextColor(getResources().getColor(R.color.day_on));
				todayText.setBackgroundResource(R.color.day_color_on);
                tomorrowText.setTextColor(getResources().getColor(R.color.day));
                afterText.setTextColor(getResources().getColor(R.color.day));
				tomorrowText.setBackgroundResource(R.color.cinema_bg);
				afterText.setBackgroundResource(R.color.cinema_bg);
				break;
			case R.id.tomorrow:
				currentDay = 2;
				showNumberLayout(tomorrrowList);
                tomorrowText.setTextColor(getResources().getColor(R.color.day_on));
				tomorrowText.setBackgroundResource(R.color.day_color_on);
                todayText.setTextColor(getResources().getColor(R.color.day));
                afterText.setTextColor(getResources().getColor(R.color.day));
				todayText.setBackgroundResource(R.color.cinema_bg);
				afterText.setBackgroundResource(R.color.cinema_bg);
				break;
			case R.id.afterTomorow:
				currentDay = 3;
				showNumberLayout(theDayAfter);
                todayText.setTextColor(getResources().getColor(R.color.day));
                tomorrowText.setTextColor(getResources().getColor(R.color.day));
                afterText.setTextColor(getResources().getColor(R.color.day_on));
				afterText.setBackgroundResource(R.color.day_color_on);
				tomorrowText.setBackgroundResource(R.color.cinema_bg);
				todayText.setBackgroundResource(R.color.cinema_bg);
				break;
			case R.id.showItem:
				// 判断是否登录，没有登录就提示登录
				//BaseActivity mActivity = (BaseActivity) mContext;
				if (Const.user==null) {
                    GUIUtil.toast(getContext(),"not login");
				} else {
					// do something
					if (listener != null) {
						listener.onShowinfoClick((ShowInfoBean) v.getTag());
					}
				}

				break;
			default:
				break;
			}
		}
	};
}
