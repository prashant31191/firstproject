package com.qinnuan.engine.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.R;

public class ListViewPro extends ListView {

	private Context mContext;
	private Scroller mScroller;
	private TouchTool tool;
	int left, top;
	float startX, startY, currentX, currentY;
	int bgViewH, iv1W;
	int rootW, rootH;
	private View headView;
	private View bgView;
	boolean scrollerType;
	static final int len = 0xc8;
    private XListViewFooter footerview;
    //private View footerview;

	public ListViewPro(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public ListViewPro(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		mScroller = new Scroller(mContext);
        footerview = new XListViewFooter(mContext);
	}

	public ListViewPro(Context context) {
		super(context);

	}

    public void setHeadViews(View itemHead, View bgV) {
        headView = itemHead;
        bgView = bgV;
    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (!mScroller.isFinished()) {
			return super.onTouchEvent(event);
		}
		//headView = DynamicFragment.itemHead;
		//bgView = headView.findViewById(R.id.item_dynamic_head_bg);
		currentX = event.getX();
		currentY = event.getY();
		headView.getTop();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
            LogUtil.i(getClass(), "ACTION_DOWN!!!");
			left = bgView.getLeft();
			top = bgView.getBottom();
			rootW = getWidth();
			rootH = getHeight();
			bgViewH = bgView.getHeight();
			startX = currentX;
			startY = currentY;
			tool = new TouchTool(bgView.getLeft(), bgView.getBottom(),
					bgView.getLeft(), bgView.getBottom() + len);
			break;
		case MotionEvent.ACTION_MOVE:
			LogUtil.i(getClass(), "ListView2 ACTION_MOVE!!!");
            LogUtil.i(getClass(), "currentX" + currentX);
            LogUtil.i(getClass(), "currentY" + currentY);
            LogUtil.i(getClass(), "headView.getTop()=" + headView.getTop());
            LogUtil.i(getClass(), "headView.isShown()=" + headView.isShown());

			if (headView.isShown() && headView.getTop() >= 0) {
				if (tool != null) {
					int t = tool.getScrollY(currentY - startY);
					if (t >= top && t <= headView.getBottom() + len) {
						bgView.setLayoutParams(new RelativeLayout.LayoutParams(
								bgView.getWidth(), t));
					}
				}
				scrollerType = false;
			}
			break;
		case MotionEvent.ACTION_UP:
            LogUtil.i(getClass(), "ACTION_UP!!!");
			scrollerType = true;
			mScroller.startScroll(bgView.getLeft(), bgView.getBottom(),
					0 - bgView.getLeft(), bgViewH - bgView.getBottom(), 200);
			invalidate();
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			LogUtil.e(getClass(), "x=" + x +", y=" + y);
			bgView.layout(0, 0, x + bgView.getWidth(), y);
			invalidate();
           int hDp = GUIUtil.getDpi(mContext, R.dimen.margin_250);
            if(y >= hDp) {
                if(isOnLoaded) {
                    ilist.onRefresh();
                    isOnLoaded = false;
                }
            }
			if (!mScroller.isFinished() && scrollerType && y > 200) {
				bgView.setLayoutParams(new RelativeLayout.LayoutParams(bgView
						.getWidth(), y));
			}
		}
	}

    public View getFooterView() {
        return footerview;
    }

    private int isnext = 0;
    private void setFooter(int isnext) {
        this.isnext = isnext;
        LogUtil.e(getClass(), "isnext==>"+isnext);
        if(isnext == 1) {

                addFooterView(footerview);
                footerview.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        footerview.setState(XListViewFooter.STATE_LOADING);
                        ilist.onLoadMore();
                    }
                });

        } else {
            removeFooterView(footerview);
        }
    }

    private IListVPro ilist;
    public void setIListVPro(IListVPro l) {
        ilist = l;
    }

    public interface IListVPro {
        public void onRefresh();
        public void onLoadMore();
    }

    private boolean isOnLoaded = true;
    public void onLoaded(int isnext) {
        this.isnext = isnext;
        isOnLoaded = true;
        if(isnext==1) {
            removeFooterView(footerview);
            addFooterView(footerview);
            footerview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    footerview.setState(XListViewFooter.STATE_LOADING);
                    ilist.onLoadMore();
                }
            });
            footerview.setState(XListViewFooter.STATE_NORMAL);
        } else {
            removeFooterView(footerview);
        }
    }

    public void removFooter() {
        removeFooterView(footerview);
    }

}
