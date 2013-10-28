package com.showu.baogu.fragment.message;

import android.view.View;

import com.showu.baogu.xmpp.message.BaseMessage;

public interface ChatUploadListener {
	public void startUpload(BaseMessage message);

	public void endUpload(BaseMessage message,String url, boolean isUpLoadSuccess);
}
