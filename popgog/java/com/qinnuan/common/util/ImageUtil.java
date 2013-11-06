package com.qinnuan.common.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.qinnuan.engine.BuildConfig;
import com.qinnuan.engine.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtil {
    private final static String TAG = "ImageUtil";
    private final static boolean DEBUG = BuildConfig.DEBUG;

    // 缓存图片进sd卡
    public static void saveBitmap(String path, Bitmap mBitmap) {
        // 判断是否有sd卡
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                }
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    FileOutputStream fOut = null;
                    fOut = new FileOutputStream(file);
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    if (DEBUG)
                        Log.e(TAG, "保存图片成功");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    if (DEBUG)
                        Log.e(TAG, "保存图片失败");
                }
            }
        } else {
            if (DEBUG)
                Log.e(TAG, "没有sd卡无法保存图片");
        }
    }

    // 保存图片(用于头像部分)
    public static void savePic(String path, Bitmap mBitmap) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                }
            }
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                FileOutputStream fOut = null;
                fOut = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                if (DEBUG)
                    Log.e(TAG, "保存图片成功");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (DEBUG)
                    Log.e(TAG, "保存图片失败");
            }
        } else {
            if (DEBUG)
                Log.e(TAG, "没有sd卡无法保存图片");
        }
    }


    // 删除所有缓存图片
    public static boolean delAll(File f) throws IOException {
        if (!f.exists() || !f.isDirectory()) {
            return false;
        } else {
            boolean rslt = true;// 保存中间结果
            // 若文件夹非空。枚举、递归删除里面内容
            File subs[] = f.listFiles();
            for (int i = 0; i <= subs.length - 1; i++) {
                if (subs[i].isDirectory())
                    delAll(subs[i]);// 递归删除子文件夹内容
                rslt = subs[i].delete();// 删除子文件夹
            }
            rslt = f.delete();// 删除文件夹本身
            return true;
        }
    }

    /**
     * 压缩图片
     *
     * @param pathName , 压缩图片的路径
     * @param size     需要压缩到的大小
     * @return
     */
    public static String compressImage(String pathName, int size) {
        File dir = new File("/mnt/sdcard/engine/image");
        dir.mkdir();
        File file = new File(dir.getAbsoluteFile() + File.separator
                + UUID.randomUUID().toString() + ".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap image = BitmapFactory.decodeFile(pathName);
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        // ByteArrayInputStream isBm = new
        // ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray(), 0, baos.size());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    // 将assets文件中资源取出,并将图片从bitmap转换成drawable格式
    public static Drawable getDrawableFromAssetFile(Context context,
                                                    String fileName) {
        Bitmap image = null;
        BitmapDrawable drawable = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            drawable = new BitmapDrawable(context.getResources(), image);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    // 图片转圆角
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static String getSmallBitmap(Context context, String filePath,
                                        int angle) {
//		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		options.inSampleSize = calculateInSampleSize(options,
//				display.getWidth(), display.getHeight());
//        BitmapFactory.decodeFile(filePath, options);
//		options.inJustDecodeBounds = false;
        LogUtil.e(ImageUtil.class, filePath);
        int size = context.getResources().getDimensionPixelSize(R.dimen.image_upload_size);
        Bitmap bm = decodeSampledBitmapFromFile(filePath, size, size);
        Matrix m = new Matrix();
        int width = bm.getWidth();
        int height = bm.getHeight();
//        if (isCamara) {
            m.setRotate(angle); // 旋转angle度
//        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm = Bitmap.createBitmap(bm, 0, 0, width, height,
                m, true);// 从新生成图片
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        File dir = new File(FileUtil.getCachDir(context) + File.separator + "message");
        dir.mkdirs();
        File file = new File(dir.getAbsoluteFile() + File.separator
                + UUID.randomUUID().toString() + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray(), 0, baos.size());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        FileUtil.deleteFile(filePath);
        return file.getAbsolutePath();
    }

    public static Drawable getDrawableByAssets(String fileName, Context context) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Drawable drawable = Drawable.createFromStream(is, "");
        return drawable;
    }

    // decodes image and scales it to reduce memory consumption
    // 计算sampleSize
    public static int calculateInSampleSize2(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filepath, int reqWidth,
                                                     int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(filepath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize2(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    static final Pattern urlPattern = Pattern.compile("(.+)/(.+)");
    static final String urlTag2x = "/thumbnail_2x_";

    public static String get2xUrl(String url) {
        StringBuilder urlBuilder = new StringBuilder();
        Matcher m = urlPattern.matcher(url);
        if (m.matches()) {
            urlBuilder.append(m.group(1)).append(urlTag2x).append(m.group(2));
        }
        return urlBuilder.toString();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}