package com.qinnuan.common.widget;

import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.qinnuan.common.util.LogUtil;

public class CircleImageView extends ImageView {
	private Paint paint = new Paint();

	public CircleImageView(Context context) {
		super(context);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();
		if (null != drawable) {
            Matrix matrix=new Matrix() ;
            matrix.postScale(0.9f,0.9f);
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false) ;
            while (bitmap.getWidth()>getWidth()||bitmap.getHeight()>getHeight()){
                bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false) ;
            }
			Bitmap b = toRoundBitmap(bitmap);
            LogUtil.d(getClass(), "||>" + b.getWidth() + "|" + getWidth() + "," + b.getHeight() + "," + getHeight());
			paint.reset();
//			canvas.drawBitmap(b, getWidth() / 2 - b.getWidth() / 2, getHeight()
//					/ 2 - b.getHeight() / 2, paint);
            canvas.drawBitmap(b,getWidth()/2-b.getWidth()/2, getWidth()/2-b.getHeight()/2 ,paint);
		} else {
			super.onDraw(canvas);
		}
	}

	/**
	 * 转换图片成圆形
	 *
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
//        if(dst_right>100){
//            dst_right=100 ;
//            dst_bottom=100 ;
//            width=100 ;
//            height=100 ;
//        }
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
//        final Rect dst = new Rect(100,100,100,100);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 23, 44, 78);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, dst, dst, paint);
		return output;
	}

}

