package com.showu.baogu.xmpp.message;

import java.util.Comparator;

public class ComparatorChat implements Comparator<BaseMessage> {

	@Override
	public int compare(BaseMessage lhs, BaseMessage rhs) {
		// TODO Auto-generated method stub
		return (int) (lhs.getSendTime() - rhs.getSendTime());
	}

}
