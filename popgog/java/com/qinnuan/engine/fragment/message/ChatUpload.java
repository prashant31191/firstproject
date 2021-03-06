package com.qinnuan.engine.fragment.message;

import com.qinnuan.common.http.HttpStringResult;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.common.http.HttpClient;
import com.qinnuan.common.http.IResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ChatUpload implements IResponse {
    private ChatUploadListener chatUploadListener;
    private BaseMessage message;
    HttpClient client;

    public ChatUpload(BaseMessage message,
                      ChatUploadListener lis) {
        this.chatUploadListener = lis;
        this.message = message;
        client = new HttpClient(this);
    }

    public void uploadFile(String type, File file) {
        //String url = Const.upload_api_url
        String url = Const.urlBean.getUpload_api_url()
                + "/HttpHandlers/UploadFilesFromIphoneClient.ashx?filetype="
                + type;
        client.uploadFile(url, file);
    }


    @Override
    public void response(HttpStringResult result) {
        if (result.getStatus() == 200) {
            try {
                JSONObject jsonObject = new JSONObject(result.getJson());
                int stauts = jsonObject.getInt("status");
                if (stauts == 1) {
                    String urls = jsonObject.getString("httpurls");
                    chatUploadListener.endUpload(message, urls, true);
                } else {
                    chatUploadListener.endUpload(message, null, false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            chatUploadListener.endUpload(message,null,false);
        }
    }
}
