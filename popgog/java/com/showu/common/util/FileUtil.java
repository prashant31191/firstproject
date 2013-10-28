package com.showu.common.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 2013-01-29
 *
 * @author 李红波
 */
public class FileUtil {

    /**
     * 根据用户是否有SD卡而创建文件
     *
     * @param name 需要创建文件的名字
     * @return 返回文件
     */
    public static File getNewFile(Context context, String name) {
        File fileDir = new File(getCachDir(context));
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }

        File file = new File(fileDir.getAbsoluteFile() + File.separator + name);
        if (file.exists()) {
            return file;
        } else {
            try {
                file.createNewFile();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static boolean isExit(Context context, String name){
        File fileDir = new File(getCachDir(context));
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File file = new File(fileDir.getAbsoluteFile() + File.separator + name);
        if(file.exists()){
            return true ;
        }
        return false;
    }

    public static File getRAM(Context context,String url){
        File fileDir = new File(getCachDir(context)+File.separator+"arm");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File file = new File(fileDir.getAbsoluteFile() + File.separator + getMD5Hex(url));
        if(file.exists()){
            return file ;
        }
        return  null ;
    }
    public static File creatRAM(Context context,String url){
        File fileDir = new File(getCachDir(context)+File.separator+"arm");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File file = new File(fileDir.getAbsoluteFile() + File.separator + getMD5Hex(url));
        if (file.exists()) {
            return file;
        } else {
            try {
                file.createNewFile();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    /**
     *  获取缓存目录
     * @param context
     * @return
     */
    public static String getCachDir(Context context) {
        StringBuilder sb = new StringBuilder();
        String sdcardpath = Environment.getExternalStorageDirectory().getPath();
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return sb.append(sdcardpath).append(File.separator).append("baogu")
                    .toString();
        } else {
            return sb.append(context.getCacheDir().getPath())
                    .append(File.separator).append("baogu").toString();
        }
    }

    public static File getNewFile(Context context) throws IOException {
        String path = getCachDir(context) + File.separator + "temp"
                + File.separator + UUID.randomUUID().toString() + ".jpg";
        File dirs = new File(getCachDir(context) + File.separator + "temp");
        if(!dirs.exists()) {
            dirs.mkdirs();
        }
        File file = new File(path);
        file.createNewFile();
        return file;
    }

    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";
    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     */
    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if( mUriString.startsWith(pre1) )
        {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring( pre1.length() );
        }
        else if( mUriString.startsWith(pre2) )
        {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring( pre2.length() );
        }  else {
            filePath = Environment.getExternalStorageDirectory().getPath() +
                    mUriString.substring(("file://" + Environment.getExternalStorageDirectory().getPath()).length());
        }
        System.out.println("FilePath:" + filePath);
        return filePath;
    }

    /** 获取输入流 */
    public static InputStream getInputStreamByUri(Context context, Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return new java.io.FileInputStream(mUri.getPath());
            } else {
                return context.getContentResolver().openInputStream(mUri);
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static void deleteFile(String fileName) {
            File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
    }
    public static String getMD5Hex(String str) {
        if (str == null) {
            return null;
        }
        byte[] data = getMD5(str.getBytes());
        BigInteger bi = new BigInteger(data).abs();
        String result = bi.toString(36);
        return result;
    }


    private static byte[] getMD5(byte[] data) {

        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(data);
            byte[] hash = digest.digest();
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;

    }
}

