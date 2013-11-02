package com.qinnuan.engine.xmpp.message;

import com.qinnuan.engine.bean.MessageListBean;

import java.util.Comparator;

public class ComparatorMessage implements Comparator<MessageListBean> {

	@Override
	public int compare(MessageListBean object1, MessageListBean object2) {
		// TODO Auto-generated method stub
		return (int) (object2.getTime() - object1.getTime());
	}

}
