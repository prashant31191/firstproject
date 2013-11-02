package com.qinnuan.engine.xmpp;

import com.qinnuan.engine.xmpp.message.BaseMessage;

import org.jivesoftware.smack.packet.Packet;

public interface MessageSendListener {
	public enum FailType {
		NOT_CONNECTED, NOT_AUTHENTICATED
	};

	public void sendSuccess(Packet message);

	public void sendFail(BaseMessage message, FailType type);
}
