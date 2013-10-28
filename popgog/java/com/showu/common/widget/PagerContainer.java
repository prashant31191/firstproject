/* 
 * Copyright (C) 2012 National Key Laboratory, Hisense Co. , Ltd.
 * All Rights Reserved.
 * 
 * @Title: PagerContainer.java
 * @Package: com.hisense.viewpage
 * @author:kenneth
 * @date: 2012-11-30 上午10:00:15
 * @version V1.0  
 */

package com.showu.common.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.showu.baogu.R;
import com.showu.baogu.adapter.PageViewAdapter;
import com.showu.baogu.bean.film.FilmBean;
import com.showu.common.http.image.util.ImageFetcher;
import com.showu.common.util.LogUtil;

import java.util.List;

/**
 * @ClassName:PagerContainer
 * @author:kenneth
 * @date 2012-11-30 上午10:00:15
 * 
 */
public class PagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
	
	private Context mContext;

	public interface OnItemSelectChangeListener {
		public void onItemSelectChangeListener(ViewPager viewPage, int position);
	}

	private ViewPager mPager;
	boolean mNeedsRedraw = false;
	private OnItemSelectChangeListener itemSelectListener;

	public OnItemSelectChangeListener getItemSelectListener() {
		return itemSelectListener;
	}

	public void setItemSelectListener(
			OnItemSelectChangeListener itemSelectListener) {
		this.itemSelectListener = itemSelectListener;
	}

	public PagerContainer(Context context) {
		super(context);
		mContext = context;
		if (Build.VERSION.SDK_INT < 11) {
			initUnder11();
		} else {
			init();
		}
	}

	public PagerContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		if (Build.VERSION.SDK_INT < 11) {
			initUnder11();
		} else {
			init();
		}
	}

	public PagerContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		if (Build.VERSION.SDK_INT < 11) {
			initUnder11();
		} else {
			init();
		}
	}

	public View getCurrentView() {
		return mPager.getFocusedChild();
	}
	
	@TargetApi(11)
	private void init() {
		// Disable clipping of children so non-selected pages are visible
		setClipChildren(false);

		// Child clipping doesn't work with hardware acceleration in Android
		// 3.x/4.x
		// You need to set this value here if using hardware acceleration in an
		// application targeted at these releases.

		setLayerType(1, null);
	}

	private void initUnder11() {
		setClipChildren(false);
	}

	@Override
	protected void onFinishInflate() {
		try {
			mPager = (ViewPager) getChildAt(0);
			mPager.setOnPageChangeListener(this);
		} catch (Exception e) {
			throw new IllegalStateException(
					"The root child of PagerContainer must be a ViewPager");
		}
	}

	public ViewPager getViewPager() {
		return mPager;
	}

	private Point mCenter = new Point();
	private Point mInitialTouch = new Point();

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCenter.x = w / 2;
		mCenter.y = h / 2;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// We capture any touches not already handled by the ViewPager
		// to implement scrolling from a touch outside the pager bounds.
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				mInitialTouch.x = (int) ev.getX();
				mInitialTouch.y = (int) ev.getY();
				break;
			}
				
			default: {
				ev.offsetLocation(mCenter.x - mInitialTouch.x, mCenter.y - mInitialTouch.y);
				break;
			}
		}

		return mPager.dispatchTouchEvent(ev);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// Force the container to redraw on scrolling.
		// Without this the outer pages render initially and then stay static
		if (mNeedsRedraw)
			invalidate();
	}

	@Override
	public void onPageSelected(int position) {
        adapter.setCurrentItemIndex(position);
		playSoundEffect(SoundEffectConstants.CLICK);
		nextLoad(position);
		if (itemSelectListener != null) {
			itemSelectListener.onItemSelectChangeListener(mPager, position);
		}
//        if(position>0){
//            resetView(position-1);
//        }
//        if(position<adapter.getCount()-1){
//            resetView(position+1);
//        }
	}

    public  void resetView(int position){
        View view = adapter.getCurrentView(position) ;
        LogUtil.d(getClass(),"resetView......"+position+"-->"+view.findViewById(R.id.film_detail).getVisibility());
        view.findViewById(R.id.film_front_fullItem_x).setVisibility(VISIBLE);
        view.findViewById(R.id.film_front_fullItem_x).bringToFront();
        view.findViewById(R.id.film_detail).setVisibility(GONE);
    }
//	private View oldView;
//	private void showCurrentView(int position) {
//		if(oldView != null) {
//			sendBottomLayoutMsg(oldView, invisible);
//		}
//		
//		View currentView = adapter.getCurrentView(position);
//		View bottomLayout = currentView.findViewById(R.id.film_gallery_item_bottomlayout);
//		if(position == 0) {
//			FilmBean filmBean = (FilmBean)currentView.getTag();
//			if(filmBean.isFilm()) {
//				sendBottomLayoutMsg(bottomLayout, visible);
//				oldView = bottomLayout;
//			}
//		} else {
//			sendBottomLayoutMsg(bottomLayout, visible);
//			oldView = bottomLayout;
//		}
//		
//	}
	
//	private int visible = 1000;
//	private int invisible = 1001;
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if(msg.what == visible) {
//				((View)msg.obj).setVisibility(View.VISIBLE);
//			} else if(msg.what == invisible) {
//				((View)msg.obj).setVisibility(View.INVISIBLE);
//			}
//		}
//	};
//	
//	private void sendBottomLayoutMsg(Object viewObj, int visibility) {
//		Message msg = new Message();
//		msg.obj = viewObj;
//		msg.what = visibility;
//		handler.sendMessageDelayed(msg, 250);
//	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
		mNeedsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);
	}

	private void nextLoad(int postion) {
		int count = mPager.getAdapter().getCount();
		if (count > postion + 1) {
			loadByPostion(postion + 1);
		}
	}

	private void loadByPostion(int postion) {
		if (mPager.getAdapter().getCount() > postion) {
			View view = mPager.getChildAt(postion);
//			ImageView fullFront = (ImageView)view.findViewById(R.id.film_front_fullItem_x);
//			FilmBean filmBean = (FilmBean)view.getTag();
//			new LoadLocalImage(getContext()).loadImag(filmBean.getFrontcover(), fullFront);
		}
	}
	
	private PageViewAdapter adapter;
	public void initViewPager(List<FilmBean> filmList, ImageFetcher fetcher) {
        setBackgroundColor(getResources().getColor(R.color.film_gallary_bg));
		DisplayMetrics metric1 = new DisplayMetrics();
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metric1);
		Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
		int witdth = display.getWidth();
        int width_margin = getResources().getDimensionPixelSize(R.dimen.film_width_margin);
        int page_margin = getResources().getDimensionPixelSize(R.dimen.film_page_margin);
        int fading_edge_length = getResources().getDimensionPixelSize(R.dimen.film_fading_edge_length);
		LayoutParams pager_param = new LayoutParams(
				witdth-width_margin, LayoutParams.MATCH_PARENT,
                Gravity.CENTER_HORIZONTAL);

        ViewPager viewPage = getViewPager();
		viewPage.setLayoutParams(pager_param);
		viewPage.setPageMargin(page_margin);
		viewPage.setClipChildren(true);
		viewPage.setFadingEdgeLength(fading_edge_length);
		adapter = new PageViewAdapter(mContext, filmList,fetcher);
		viewPage.setAdapter(adapter);
		viewPage.setOffscreenPageLimit(adapter.getCount());
		invalidate();
	}
	
//	public void initViewPager(List<FilmBean> filmList, final View parent) {
//		ViewPager viewPage = null;
//		DisplayMetrics metric1 = new DisplayMetrics();
//		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metric1);
//		parent.measure(metric1.widthPixels, metric1.heightPixels);
//		int witdth = parent.getWidth();
//		int height = parent.getHeight();
//		LayoutParams pager_param = new LayoutParams(
//							witdth-60, 33*height/40, Gravity.CENTER_HORIZONTAL);
//		pager_param.topMargin = 5;
//		viewPage = getViewPager();
//		viewPage.setLayoutParams(pager_param);
//		viewPage.setPageMargin(15);
//		viewPage.setClipChildren(true);
//		viewPage.setFadingEdgeLength(30);
//		adapter = new PageViewAdapter(mContext, filmList);
//		viewPage.setAdapter(adapter);
//		LogUtil.i(getClass(), "adapter.getCount()=>"+adapter.getCount());
//		viewPage.setOffscreenPageLimit(adapter.getCount());
//		invalidate();
//	}

}