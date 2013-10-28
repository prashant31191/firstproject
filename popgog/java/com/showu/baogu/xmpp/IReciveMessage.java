package com.showu.baogu.xmpp;

import com.showu.baogu.xmpp.message.BaseMessage;

import org.jivesoftware.smack.packet.Message;

public interface IReciveMessage {
	public boolean  reciveMessage(BaseMessage msg) ;
}
