package com.qinnuan.engine.xmpp;

import android.content.Context;

import com.qinnuan.engine.xmpp.message.MessageUtil;
import com.qinnuan.engine.xmpp.message.SessionType;
import com.qinnuan.engine.xmpp.provider.FanBlacklistManager;
import com.qinnuan.engine.xmpp.provider.SessionManager;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageSRC;
import com.qinnuan.engine.xmpp.message.MessageType;
import com.qinnuan.engine.xmpp.provider.AbstractSessionDao;
import com.qinnuan.engine.xmpp.provider.ChatDBManager;
import com.qinnuan.engine.xmpp.provider.SessionUtil;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;

public class MessagePacketListener implements PacketListener {
    public static final String MESSAGE_ACTION = "com.engine.message";
    private Context mContext;
    private XmppManager xmppManager;



    public MessagePacketListener(Context context, XmppManager xmppManager) {
        this.mContext = context;
        this.xmppManager = xmppManager;
    }

    @Override
    public void processPacket(Packet packet) {
        if (packet instanceof Message
                && ((Message) packet).getType() == Type.chat) {
            String xml = packet.toXML().replaceAll("&quot;","\"");
            LogUtil.e(MessagePacketListener.class, "xml=" + xml);
            BaseMessage message = MessageHelp.parseMessage(xml);
            message.setMessageSRC(MessageSRC.FROME);

            //查询黑名单，如果在黑名单内，不接收
            String fanid = message.getTargetId();
            if(FanBlacklistManager.getInstance(mContext).isInBlacklist(fanid))
                return;

            MessageUtil.getInstance(xmppManager.getContext()).messageJsonToObject(message);
            if ( message.getMessageType() != MessageType.DELETEFRIEND) {
                saveSessionAndChat(message);
            }else{
                SessionManager.getInstance(xmppManager.getContext()).deleteChatSession(message.getTargetId()) ;
                ChatDBManager.getInstance(xmppManager.getContext()).deletMessageByTargetId(message.getTargetId());
            }
            for (IReciveMessage reciveMessage : xmppManager.getReciveMessageList()) {
                reciveMessage.reciveMessage(message);
            }
        }
    }



    public void saveSession(BaseMessage message) {
        MessageListBean session = message.getSession();
        AbstractSessionDao sessionDao = SessionUtil.getInstance(xmppManager.getContext()).getDao(session.getType());
        if (sessionDao != null) {
            sessionDao.saveOrUpdateSession(session, MessageSRC.FROME);
        }
        if (message.getRoom().equals("0")) {//非群聊消息
            if (isHelp(message)) {//爆谷助手发来的消息
                session.setType(SessionType.HELP);
                SessionManager.getInstance(xmppManager.getContext()).saveOrUpdateHelpSession(session, MessageSRC.FROME);
            } else {//普通消息
                SessionManager.getInstance(xmppManager.getContext()).saveOrUpdateSession(message.getSession(), MessageSRC.FROME);
            }
        } else {//群聊消息
            SessionManager.getInstance(xmppManager.getContext()).saveOrUpdateRoomSession(message.getSession(), MessageSRC.FROME);
        }
    }

    private void saveSessionAndChat(BaseMessage message) {
        if (isHelp(message)) {
            message.setHelpType(1);
        }
        if (message.getMessageType() != MessageType.DYNAMICNUMBER && message.getMessageType() != MessageType.LASTHEAED) {
            ChatDBManager.getInstance(xmppManager.getContext()).saveMessage(message);
        }
        saveSession(message);
    }

    public boolean isHelp(BaseMessage msg) {
        if (msg.getMessageType() == MessageType.HELPTEXT || msg.getMessageType() == MessageType.HELPCINEMA || msg.getMessageType() == MessageType.HELPACCOUNT) {
            return true;
        }
        return false;
    }
}
