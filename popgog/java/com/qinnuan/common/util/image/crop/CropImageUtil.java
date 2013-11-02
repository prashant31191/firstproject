package com.qinnuan.common.util.image.crop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.qinnuan.common.util.FileUtil;
import com.qinnuan.common.util.LogUtil;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 *  JinghuiLiu
 */
public class CropImageUtil {

    private static Context mContext;
    private static CropImageUtil instance;
    public static final int IMG_CROP = 111;
    public static final int IMG_CROP_READ = 112;
    public static final int CHOICE_IMAGE = 0x1f;

    public final static String IMAGE_URI = "iamge_uri";
    public final static String BITMAP = "bitmap";
    public final static String CROP_IMAGE_URI = "crop_image_uri";

    private static File imgFile;
    private static Bitmap photo;
    private static String filePath;

    private CropImageUtil(Context c) {
        mContext = c;
    }

    public static CropImageUtil getInstance(Context c) {
        if(instance == null)
            instance = new CropImageUtil(c);
        return instance;
    }

    private Uri selectedImageUri = null;

    /**
     * 开启照相机
     */
    public void startCamera(Activity a) {
        String FILE_NAME = UUID.randomUUID() + ".jpg";
        File photo = FileUtil.getNewFile(mContext, FILE_NAME);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (photo != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            selectedImageUri = Uri.fromFile(photo);
            a.startActivityForResult(intent, IMG_CROP);
        }
    }

    /**
     * 取得照相图片的Uri
     */
    public Uri getCameraImgUrl() {
        return selectedImageUri;
    }

    /**
     * 开始选取本地相册图片
     */
    public void startPicPhoto(Activity a) {
        Intent mIntent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        a.startActivityForResult(mIntent, CHOICE_IMAGE);
    }

    /**
     * 取得本地相册图片Uri
     */
    public Uri getChoiceLocalImgUrl(Intent data) {
        return data.getData(); // 获得图片的uri
    }

    /**
     * 开始裁剪图片
     * @param uri
     * @param reqCode
     */
    public void startPhotoCrop(Uri uri, int reqCode, Activity a) {
        Intent intent = new Intent(mContext, CropActivity.class);
        intent.putExtra(IMAGE_URI, uri);
        a.startActivityForResult(intent, reqCode);
    }

    public Bitmap getPhotoBitmap() {
        return photo;
    }

    public File getPhotoFile() {
        return imgFile;
    }

    /**
     * 读取裁剪好的图片
     */
    public static void readCropImage(Intent data) {

        if (data != null) {
            Uri uri = data.getParcelableExtra(CROP_IMAGE_URI);
            photo = getBitmap(uri);
            if (photo != null) {
                LogUtil.i(mContext.getClass(), "URI:" + uri);
                String thePath = FileUtil.getAbsolutePathFromNoStandardUri(uri);
                filePath = thePath;
                imgFile = new File(thePath);
                LogUtil.i(mContext.getClass(), "readCropImage->imgFile:" + imgFile);
            }
        }
    }

    public String getFilePath() {
        return filePath;
    }

    private static Bitmap getBitmap(Uri uri) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = getInputStreamByUri(uri);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 获取输入流
     */
    private static InputStream getInputStreamByUri(Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return new java.io.FileInputStream(mUri.getPath());
            } else {
                return mContext.getContentResolver().openInputStream(mUri);
            }
        } catch (Exception ex) {
            return null;
        }
    }

}
