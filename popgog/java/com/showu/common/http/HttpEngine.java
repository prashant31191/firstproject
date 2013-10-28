package com.showu.common.http;

import com.showu.common.util.LogUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;

public class HttpEngine extends AsyncTask<Object, Object, HttpStringResult> {
    private IResponse response;
    private final static Pattern p = Pattern.compile("^http.+");
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String BOUNDARY =  UUID.randomUUID().toString(); // 边界标识 随机生成
    public HttpEngine(IResponse res) {
        // TODO Auto-generated constructor stub
        this.response = res;
    }

    @Override
    protected HttpStringResult doInBackground(Object... params) {
        int type = (Integer) params[0];
        if (type == 0) {
            return getMethod((String) params[1]);
        } else if (type == 1) {
            return postMethod((String) params[1], (String) params[2]);
        } else if (type == 2) {
            return uploadFileByUrl((String) params[1], (File) params[2]);
        } else {
            LogUtil.e(getClass(), "Not  type to support");
            return null;
        }
    }

    @Override
    protected void onPostExecute(HttpStringResult result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        response.response(result);

    }

    public HttpStringResult getMethod(String httpUrl) {
        LogUtil.d(getClass(), "GET=>" + httpUrl);
        HttpStringResult httpStringResult = new HttpStringResult();
        if (httpUrl == null || !p.matcher(httpUrl.trim()).matches()) {
            httpStringResult.setStatus(-1);
            return httpStringResult;
        }
        InputStream is = null;
        httpStringResult.setUrl(httpUrl.trim());
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(40000 /* milliseconds */);
            conn.setConnectTimeout(35000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            httpStringResult.setStatus(response);
            is = conn.getInputStream();
            String contentAsString = readIt(is);
            httpStringResult.setJson(contentAsString);

        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.i(getClass(), httpUrl + ":" + e.getMessage());
        }
        return httpStringResult;

    }

    public HttpStringResult postMethod(String httpUrl, String urlParmas) {
        LogUtil.d(getClass(), "POST=>" + httpUrl);
        LogUtil.d(getClass(), "POST PARAM=>" + urlParmas);
        InputStream is = null;
        HttpStringResult httpStringResult = new HttpStringResult();
        httpStringResult.setUrl(httpUrl);
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(40000 /* milliseconds */);
            conn.setConnectTimeout(35000 /* milliseconds */);
            // 因为这个是post请求，设立需要设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置以POST方式
            conn.setRequestMethod("POST");
            // Post请求不能使用缓存
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // 配置本次连接的Content-type,配置为application/x-www-form-urlencoded的
            conn.setRequestProperty("Contet-Type",
                    "application/x-www-form-urlencoded");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            conn.connect();
            BufferedOutputStream out = new BufferedOutputStream(
                    conn.getOutputStream());
            out.write(urlParmas.getBytes());
            out.flush();
            out.close();

            int response = conn.getResponseCode();
            httpStringResult.setStatus(response);
            is = conn.getInputStream();
            String contentAsString = readIt(is);
            httpStringResult.setJson(contentAsString);

        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.i(getClass(), httpUrl + ":" + e.getMessage());
        }
        return httpStringResult;

    }


    public HttpStringResult uploadFileByUrl(String urls, File file) {
        return UploadUtil.getInstance().toUploadFile(file,"android-upload file",urls,null);
    }


    public String readIt(InputStream stream) throws IOException,
            UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[1];
        CharArrayWriter writer = new CharArrayWriter();
        while (reader.read(buffer) > -1) {
            writer.write(buffer);
        }
        return new String(writer.toCharArray());
    }

}
