package com.showu.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.showu.baogu.R;

public class PressImageView extends FrameLayout {
	private ImageView imageView;

	public PressImageView(Context context) {
		super(context);
		imageView = new ImageView(context);
		addView(imageView);
		setForeground(context.getResources().getDrawable(
				R.drawable.photogrid_list_selector));
	}

	public PressImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		imageView = new ImageView(context, attrs);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		addView(imageView);
		setForeground(context.getResources().getDrawable(
				R.drawable.photogrid_list_selector));
	}

	public PressImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		imageView = new ImageView(context, attrs, defStyle);
		addView(imageView);
		setForeground(context.getResources().getDrawable(
				R.drawable.photogrid_list_selector));
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageBitmap(Bitmap bm) {
		imageView.setImageBitmap(bm);
	}

	public void setImageDrawable(Drawable drawable) {
		imageView.setImageDrawable(drawable);
	}
}
