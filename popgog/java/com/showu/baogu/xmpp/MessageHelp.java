package com.showu.baogu.xmpp;

import com.showu.baogu.application.BaoguApplication;
import com.showu.baogu.xmpp.message.BaseMessage;
import com.showu.common.util.LogUtil;

import org.jivesoftware.smack.packet.Packet;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MessageHelp {
	public static final String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	public static BaseMessage parseMessage(String message) {
		return parseXMl(xmlHead + message, new MessageHandler());
	}


	private static BaseMessage parseXMl(String xml, MessageHandler handler) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			// 获取事件源
			XMLReader xmlReader = parser.getXMLReader();
			// 设置处理器
			xmlReader.setContentHandler(handler);
			// 解析xml文档
			InputSource inputSource = new InputSource(new ByteArrayInputStream(
					xml.getBytes("UTF-8")));
			xmlReader.parse(inputSource);
			return handler.getMessage();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
