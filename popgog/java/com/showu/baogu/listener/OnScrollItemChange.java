package com.showu.baogu.listener;

import android.view.View;

public interface OnScrollItemChange {
	/**
	 * @param view  The return Item View
	 * @param position  The Item View position
	 */
	public void loadHead(View view, int position);
}
