package com.showu.baogu.xmpp;

import com.showu.baogu.xmpp.message.BaseMessage;
import org.jivesoftware.smack.packet.Packet;

public interface MessageSendListener {
	public enum FailType {
		NOT_CONNECTED, NOT_AUTHENTICATED
	};

	public void sendSuccess(Packet message);

	public void sendFail(BaseMessage message, FailType type);
}
