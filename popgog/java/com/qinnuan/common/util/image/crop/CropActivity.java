package com.qinnuan.common.util.image.crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.qinnuan.common.util.FileUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.R;

/**
 * The activity can crop specific region of interest from an image.
 */
public class CropActivity extends MonitoredActivity {

	// private static final String TAG = "CropImage";

	private static final boolean RECYCLE_INPUT = true;

	private int mAspectX, mAspectY;
	private final Handler mHandler = new Handler();

	// These options specifiy the output image size and whether we should
	// scale the output to fit it (or just crop it).
	private int mOutputX, mOutputY;
	private boolean mScale;
	private boolean mScaleUp = true;
	private boolean mCircleCrop = false;

	boolean mSaving; // Whether the "save" button is already clicked.

	private CropImageView mImageView;

	private Bitmap mBitmap;

	// private RotateBitmap rotateBitmap;
	HighlightView mCrop;

	Uri targetUri;

	HighlightView hv;

	private int rotation = 0;

	private static final int ONE_K = 1024;
	private static final int ONE_M = ONE_K * ONE_K;

	private ContentResolver mContentResolver;

	private static int DEFAULT_WIDTH = 512;
	private static int DEFAULT_HEIGHT = 384;

	private int width;
	private int height;
	private int sampleSize = 1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		DEFAULT_WIDTH = basePref.getDisplayWidth();
		DEFAULT_HEIGHT = DEFAULT_WIDTH;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Make UI fullscreen.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_crop);

		initViews();

		Intent intent = getIntent();
		targetUri = intent.getParcelableExtra(CropImageUtil.IMAGE_URI);
		if (targetUri == null) {
			mBitmap = intent.getParcelableExtra(CropImageUtil.BITMAP);
		}
		mContentResolver = getContentResolver();

		boolean isBitmapRotate = false;
		if (mBitmap == null) {
			String path = getFilePath(targetUri);
			// 判断图片是不是旋转了90度，是的话就进行纠正。
			isBitmapRotate = isRotateImage(path);
			getBitmapSize();
			getBitmap();
		}

		if (mBitmap == null) {
			finish();
			return;
		}

		startFaceDetection(isBitmapRotate);
	}

	private void initViews() {
		mImageView = (CropImageView) findViewById(R.id.image);
		mImageView.mContext = this;

		findViewById(R.id.discard).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});
		findViewById(R.id.rotate).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						
						onRotateClicked();
					}
				});

		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSaveClicked();
			}
		});
	}

	/**
	 * 获取Bitmap分辨率，太大了就进行压缩
	 */
	private void getBitmapSize() {
		InputStream is = null;
		try {

			is = getInputStream(targetUri);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, options);

			width = options.outWidth;
			height = options.outHeight;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	private void getBitmap() {
		InputStream is = null;
		try {

			try {
				is = getInputStream(targetUri);
			} catch (IOException e) {
				e.printStackTrace();
			}

			while ((width / sampleSize > DEFAULT_WIDTH * 2)
					|| (height / sampleSize > DEFAULT_HEIGHT * 2)) {
				sampleSize *= 2;
			}

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = sampleSize;

			mBitmap = BitmapFactory.decodeStream(is, null, options);

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	private boolean isRotateImage(String path) {

		try {
			ExifInterface exifInterface = new ExifInterface(path);

			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				return true;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取输入流
	 */
	private InputStream getInputStream(Uri mUri) throws IOException {
		try {
			if (mUri.getScheme().equals("file")) {
				return new java.io.FileInputStream(mUri.getPath());
			} else {
				return mContentResolver.openInputStream(mUri);
			}
		} catch (FileNotFoundException ex) {
			return null;
		}
	}

	/**
	 * 根据Uri返回文件路径
	 */
	private String getFilePath(Uri mUri) {
		try {
			if (mUri == null) {
				FileOutputStream b = null;
				File file = FileUtil.getNewFile(this);
				b = new FileOutputStream(file);
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, b);// 把数据写入文件
				LogUtil.e(getClass(), "file path:" + file.getAbsolutePath());
				return file.getAbsolutePath();
			} else {
				LogUtil.e(getClass(), "URIPATH:" + mUri.toString());
				if (mUri.getScheme().equals("file")) {
					return mUri.getPath();
				} else {
					return getFilePathByUri(mUri);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * @Title: getFilePathByUri
	 * @param mUri
	 * @return
	 * @return String
	 */
	private String getFilePathByUri(Uri mUri) throws FileNotFoundException {
		String imgPath;
		Cursor cursor = mContentResolver.query(mUri, null, null, null, null);
		cursor.moveToFirst();
		imgPath = cursor.getString(1); // 图片文件路径
		return imgPath;
	}

	/**
	 * @Title: startFaceDetection
	 * @param isRotate 是否旋转图片
	 * @return void
	 * @date 2012-12-14 上午10:38:29
	 */
	private void startFaceDetection(final boolean isRotate) {
		if (isFinishing()) {
			return;
		}
		if (isRotate) {
			initBitmap();
		}

		mImageView.setImageBitmapResetBase(mBitmap, true);

		startBackgroundJob(this, null, "请稍等...",
            new Runnable() {
                public void run() {
                    final CountDownLatch latch = new CountDownLatch(1);

                    mHandler.post(new Runnable() {
                        public void run() {

                            final Bitmap b = mBitmap;
                            if (b != mBitmap && b != null) {

                                mImageView.setImageBitmapResetBase(b, true);
                                mBitmap.recycle();
                                mBitmap = b;
                            }
                            if (mImageView.getScale() == 1F) {
                                mImageView.center(true, true);
                            }
                            latch.countDown();
                        }
                    });
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    mRunFaceDetection.run();
                }
            }, mHandler
        );
    }

	/**
	 * 旋转原图
	 */
	private void initBitmap() {
		Matrix m = new Matrix();
		m.setRotate(90);
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();

		try {
			mBitmap = Bitmap
					.createBitmap(mBitmap, 0, 0, width, height, m, true);
		} catch (OutOfMemoryError ooe) {
			m.postScale((float) 1 / sampleSize, (float) 1 / sampleSize);
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, m, true);
		}
	}

	private static class BackgroundJob extends
			MonitoredActivity.LifeCycleAdapter implements Runnable {

		private final MonitoredActivity mActivity;
		private final ProgressDialog mDialog;
		private final Runnable mJob;
		private final Handler mHandler;
		private final Runnable mCleanupRunner = new Runnable() {
			public void run() {
				mActivity.removeLifeCycleListener(BackgroundJob.this);
				if (mDialog.getWindow() != null)
					mDialog.dismiss();
			}
		};

		public BackgroundJob(MonitoredActivity activity, Runnable job,
				ProgressDialog dialog, Handler handler) {
			mActivity = activity;
			mDialog = dialog;
			mJob = job;
			mActivity.addLifeCycleListener(this);
			mHandler = handler;
		}

		public void run() {
			try {
				mJob.run();
			} finally {
				mHandler.post(mCleanupRunner);
			}
		}

		@Override
		public void onActivityDestroyed(MonitoredActivity activity) {
			// We get here only when the onDestroyed being called before
			// the mCleanupRunner. So, run it now and remove it from the queue
			mCleanupRunner.run();
			mHandler.removeCallbacks(mCleanupRunner);
		}

		@Override
		public void onActivityStopped(MonitoredActivity activity) {
			mDialog.hide();
		}

		@Override
		public void onActivityStarted(MonitoredActivity activity) {
			mDialog.show();
		}
	}

	private static void startBackgroundJob(MonitoredActivity activity,
			String title, String message, Runnable job, Handler handler) {
		// Make the progress dialog uncancelable, so that we can gurantee
		// the thread will be done before the activity getting destroyed.
		ProgressDialog dialog = ProgressDialog.show(activity, title, message,
				true, false);
		new Thread(new BackgroundJob(activity, job, dialog, handler)).start();
	}

	Runnable mRunFaceDetection = new Runnable() {
		float mScale = 1F;
		Matrix mImageMatrix;

		// Create a default HightlightView if we found no face in the picture.
		private void makeDefault() {
			// mImageView.re
			if (hv != null) {
				mImageView.remove(hv);
			}
			hv = new HighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			// make the default size about 4/5 of the width or height
			int cropWidth = Math.min(width, height) * 4 / 5;
			int cropHeight = cropWidth;

			if (mAspectX != 0 && mAspectY != 0) {
				if (mAspectX > mAspectY) {
					cropHeight = cropWidth * mAspectY / mAspectX;
				} else {
					cropWidth = cropHeight * mAspectX / mAspectY;
				}
			}

			int x = (width - cropWidth) / 2;
			int y = (height - cropHeight) / 2;

			RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
			hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
					mAspectX != 0 && mAspectY != 0);
			mImageView.add(hv);
		}

		public void run() {
			mImageMatrix = mImageView.getImageMatrix();

			mScale = 1.0F / mScale;
			mHandler.post(new Runnable() {
				public void run() {
					makeDefault();

					mImageView.invalidate();
					if (mImageView.mHighlightViews.size() == 1) {
						mCrop = mImageView.mHighlightViews.get(0);
						mCrop.setFocus(true);
					}
				}
			});
		}
	};

	/**
	 * 旋转图片，每次以90度为单位
	 */
	private void onRotateClicked() {
		startFaceDetection(true);
	}

	/**
	 * 点击保存的处理，这里保存成功回传的是一个Uri，系统默认传回的是一个bitmap图，
	 * 如果传回的bitmap图比较大的话就会引起系统出错。会报这样一个异常：
	 * android.os.transactiontoolargeexception。为了规避这个异常， 采取了传回Uri的方法。
	 */
	private void onSaveClicked() {
		// this code needs to change to use the decode/crop/encode single
		// step api so that we don't require that the whole (possibly large)
		// bitmap doesn't have to be read into memory
		if (mCrop == null) {
			return;
		}

		if (mSaving)
			return;
		mSaving = true;

		final Bitmap croppedImage;

		Rect r = mCrop.getCropRect();

		int width = r.width();
		int height = r.height();

		// If we are circle cropping, we want alpha channel, which is the
		// third param here.

		croppedImage = Bitmap
				.createBitmap(width, height, Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(croppedImage);
		Rect dstRect = new Rect(0, 0, width, height);

		canvas.drawBitmap(mBitmap, r, dstRect, null);

		// Release bitmap memory as soon as possible
		mImageView.clear();
		mBitmap.recycle();
		mBitmap = null;

		mImageView.setImageBitmapResetBase(croppedImage, true);
		mImageView.center(true, true);
		mImageView.mHighlightViews.clear();

		// String imgPath = getFilePath(targetUri);
		File file = FileUtil.getNewFile(this, UUID.randomUUID() + ".jpg");
		// final String cropPath = imgPath.replace(".", "_crop_image.").trim();
		// mHandler.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// saveDrawableToCache(croppedImage, cropPath);
		// }
		//
		// });
		saveDrawableToCache(croppedImage, file);

		Uri cropUri = Uri.fromFile(file);

		Intent intent = new Intent("inline-data");
        //Intent intent = getIntent();
		intent.putExtra(CropImageUtil.CROP_IMAGE_URI, cropUri);
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * 将Bitmap放入缓存，
	 */
	private boolean saveDrawableToCache(Bitmap bitmap, File file) {
		try {
			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
