package com.qinnuan.engine.xmpp.message;

import com.qinnuan.common.util.LogUtil;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by showu on 13-7-15.
 */
public class MessageExtensionProvider implements PacketExtensionProvider {
    @Override
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        LogUtil.e(MessageExtension.class, parser.getName() + "||" + parser.getText());
        return null;
    }
}
