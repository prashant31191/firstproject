package com.showu.common.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.showu.common.util.ImageUtil;
import com.showu.common.util.LogUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

import java.io.File;

public class WXShare {
	private final String APP_ID = "wx644ba655b578815a";
	private IWXAPI api;
    private static final int THUMB_SIZE = 150;
	private static WXShare instance;

	public static WXShare getInstance(Context context) {
		if (instance == null) {
			instance = new WXShare(context);
		}
		return instance;
	}

	private WXShare(Context context) {
		api = WXAPIFactory.createWXAPI(context, APP_ID);
//		Toast.makeText(context, api.registerApp(APP_ID)+"", Toast.LENGTH_SHORT).show() ;
		api.registerApp(APP_ID);
	}

    public boolean isWXInstalled(){
        return  api.isWXAppInstalled();
    }
	public void share(String content) {
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = content;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = content;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene =  SendMessageToWX.Req.WXSceneTimeline ;
		// 调用api接口发送数据到微信
		LogUtil.e(getClass(), api.sendReq(req));
	}
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
    public void shareImg(String path) {
        File file = new File(path);
        if (!file.exists()) {
//            String tip = SendToWXActivity.this.getString(R.string.send_img_file_not_exist);
//            Toast.makeText(SendToWXActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
//            break;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = ImageUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene =  SendMessageToWX.Req.WXSceneTimeline ;
        api.sendReq(req);

    }
}
