package com.qinnuan.common.http;

import com.qinnuan.common.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by showu on 13-7-23.
 */
public class ImageDowanLoad extends Thread {
    private final static Pattern p = Pattern.compile("^http.+");
    private IDownLoadListener downLoadListener ;
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private File file ;
    private String url ;
    public ImageDowanLoad(IDownLoadListener downLoadListener, File file,String url) {
        this.downLoadListener = downLoadListener;
        this.file = file;
        this.url=url ;
    }

    @Override
    public void run() {
          download(url) ;
    }

    private File download(String httpUrl) {
        LogUtil.d(getClass(), "GET=>" + httpUrl);
        InputStream is = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            if(response==200){
                downLoadListener.startDownload(conn.getContentLength());
                is = conn.getInputStream();
                FileOutputStream outputStream=new FileOutputStream(file) ;
                BufferedInputStream  in = new BufferedInputStream(is, IO_BUFFER_SIZE);
                int b;
                while ((b = in.read()) != -1) {
                    downLoadListener.downloading(b);
                    outputStream.write(b);
                }
                outputStream.flush();
                outputStream.close();
                is.close();
                downLoadListener.downLoadEnd(file);
                return file ;
            }else{
                return null ;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.i(getClass(), httpUrl + ":" + e.getMessage());
        }
        return null;

    }
}
