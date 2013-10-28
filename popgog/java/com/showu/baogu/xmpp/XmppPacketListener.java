package com.showu.baogu.xmpp;

import android.content.Context;
import android.content.Intent;
import com.showu.common.util.LogUtil;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;

public class XmppPacketListener implements PacketListener {
	public static final String MESSAGE_ACTION = "com.baogu.message";
	private Context mContext;
	private XmppManager xmppManager;

	public XmppPacketListener(Context context, XmppManager xmppManager) {
		this.mContext = context;
		this.xmppManager = xmppManager;
	}

	@Override
	public void processPacket(Packet packet) {
		// TODO Auto-generated method stub
		LogUtil.e(PacketListener.class, packet.toXML());
		if (packet instanceof Message
				&& ((Message) packet).getType() == Type.chat) {
			Intent intent = new Intent(MESSAGE_ACTION);
			intent.putExtra("message", packet.toXML());
			mContext.sendBroadcast(intent);
		}
	}

}
