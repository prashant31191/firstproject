package com.qinnuan.engine.xmpp.message;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * Created by showu on 13-7-12.
 */
public class MessageExtension implements PacketExtension{
    protected String name ;
    protected String value ;
    public static String NAME_SPACE="http://com.showu.engine";

    public MessageExtension(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getElementName() {
        return name;
    }

    @Override
    public String getNamespace() {
        return NAME_SPACE;
    }

    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder("<") ;
        builder.append(name).append(">").append(value).append("</").append(name).append(">");
        return builder.toString();
    }
}
