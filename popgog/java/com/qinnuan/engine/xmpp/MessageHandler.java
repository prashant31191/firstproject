package com.qinnuan.engine.xmpp;

import com.qinnuan.engine.xmpp.message.BaseMessage;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MessageHandler extends DefaultHandler {
	private BaseMessage message;
    private StringBuilder sb = new StringBuilder() ;
	public BaseMessage getMessage() {
		return message;
	}

	private String currentTag;



	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.currentTag = localName;
		if (currentTag.equals("message")) {
		    message=new BaseMessage();
            for (int i = 0; i < attributes.getLength(); i++) {
                message.parseXml(attributes.getQName(i), attributes.getValue(i));
            }
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
        if(sb.length()>0){
            message.parseXml(currentTag,sb.toString());
            sb=new StringBuilder() ;
        }
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String value = new String(ch, start, length).trim();
        sb.append(value);
//		if (value.length() > 0) {
//			message.parseXml(currentTag, value);
//		}
	}



}
