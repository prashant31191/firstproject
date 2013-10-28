package com.showu.baogu.util.view;

import com.showu.baogu.listener.OnScrollImageLoad;
import com.showu.baogu.listener.OnScrollItemChange;
import com.showu.common.util.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

public class MyXListView extends XListView {

	public MyXListView(Context context) {
		super(context);
	}

	public MyXListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyXListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void initWithContext(Context context) {
		super.initWithContext(context);
	}

	public interface IHasNext {
		public Integer getHasNext();
	}
	private IHasNext mIHasNext;
	public void setIHasNext(IHasNext l) {
        mIHasNext = l;
        Integer next = mIHasNext.getHasNext();
        LogUtil.i(getClass(), "next in myxlistview==>" + next);
        if (next == 0) {
            removeFooterView(mFooterView);
        } else if (this.getFooterViewsCount() < 1) {
            addFooterView(mFooterView);
        }
    }
	
	public void addFooter() {
		if(this.getFooterViewsCount()<1) {
			addFooterView(mFooterView);
		}
	}
	
	public void removeFooter() {
		if(this.getFooterViewsCount()>0) {
			removeFooterView(mFooterView);
		}
	}
	
	public View getFooterView() {
		return mFooterView;
	}
	
	public void onLoaded() {
		stopRefresh();
		stopLoadMore();
	}
	
	private OnScrollItemChange onScrollItemChange;
	private OnScrollImageLoad onScrollImageLoad;
	public void setOnScrollItemChange(OnScrollItemChange onScrollItemChange) {
		this.onScrollItemChange = onScrollItemChange;
	}
	
	public void setOnScrollImageLoad(OnScrollImageLoad onScrollImageLoad) {
		this.onScrollImageLoad = onScrollImageLoad;
	}
	
	private int scrollStatus = -1;
	private int firstItem = 0;
	private int visiable = 5;
	private int firstItemIndex;
	public void onScroll(AbsListView arg0, int firstVisiableItem, 
											int visiableCount, int totalCount) {
		firstItemIndex = firstVisiableItem;
		firstItem = firstVisiableItem;
		visiable = visiableCount;
		if (firstVisiableItem == 0) {
			for (int i=firstVisiableItem; i<firstVisiableItem+visiableCount; i++) {
				if (scrollStatus == -1||scrollStatus==OnScrollListener.SCROLL_STATE_IDLE) {
					if(onScrollItemChange != null) {
						onScrollItemChange.loadHead(arg0.getChildAt(i+1), i);
					}
					if(onScrollImageLoad != null) {
						onScrollImageLoad.loadImageContent(arg0.getChildAt(i+1), i);
					}
				}
			}
		}
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		scrollStatus = arg1;
		if (OnScrollListener.SCROLL_STATE_IDLE == arg1) {
			for (int i = firstItem; i < firstItem + visiable; i++) {
				if (onScrollItemChange != null) {
					View view = arg0.getChildAt(i - firstItem +1);
					if (view != null) {
						onScrollItemChange.loadHead(view, i);
					}
				}
				if (onScrollImageLoad != null) {
					View view = arg0.getChildAt(i - firstItem +1);
					if (view != null) {
						onScrollImageLoad.loadImageContent(view, i);
					}
				}
			}
		}
	}
	
}
