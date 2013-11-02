package com.qinnuan.engine.fragment.message;

import com.qinnuan.engine.xmpp.message.BaseMessage;

public interface ChatUploadListener {
	public void startUpload(BaseMessage message);

	public void endUpload(BaseMessage message,String url, boolean isUpLoadSuccess);
}
