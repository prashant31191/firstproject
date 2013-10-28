package com.showu.common.http;

import android.content.Context;
import com.showu.common.util.FileUtil;
import com.showu.common.util.LogUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by showu on 13-7-5.
 */
public class FileDownload extends AsyncTask<Object,Object, File>{
    private final static Pattern p = Pattern.compile("^http.+");
    private IDownLoadListener downLoadListener ;
    private File file ;

    public FileDownload(IDownLoadListener downLoadListener, File file) {
        this.downLoadListener = downLoadListener;
        this.file = file;
    }

    @Override
    protected File doInBackground(Object... params) {
        String httpUrl = params[0].toString();
        if (httpUrl == null || !p.matcher(httpUrl.trim()).matches()) {
          return null ;
        }
        return download(params[0].toString());
    }

    @Override
    protected void onPostExecute(File file) {
        downLoadListener.downLoadEnd(file);
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
            byte[] buffer=new byte[1024]  ;
            int len=0;
            while ((len=is.read(buffer))!=-1){
               downLoadListener.downloading(len);
                outputStream.write(buffer);
            }
            outputStream.flush();
            outputStream.close();
            is.close();
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
