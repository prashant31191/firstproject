package com.qinnuan.engine.bean;

import com.qinnuan.engine.xmpp.message.SessionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageListBean implements Serializable {

    private long time=0;
    private int count=-1;

	private String targetId;
	private String name;

	private String distanc;
	private String content;
	private SessionType type;
	private String headImage;
	private int relationShip=-1 ;
    private String room="0" ;
    private int membercount=0 ;
    private boolean needTime=false ;

    public int getMembercount() {
        return membercount;
    }

    public void setMembercount(int membercount) {
        this.membercount = membercount;
    }

    public boolean isNeedTime() {
        return needTime;
    }

    public void setNeedTime(boolean needTime) {
        this.needTime = needTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    private List<MessageListBean> list = new ArrayList<MessageListBean>();
	private  String isOfficalUser ;

	public int getRelationShip() {
		return relationShip;
	}

	public void setRelationShip(int relationShip) {
		this.relationShip = relationShip;
	}


	public String getIsOfficalUser() {
		return isOfficalUser;
	}

	public void setIsOfficalUser(String isOfficalUser) {
		this.isOfficalUser = isOfficalUser;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getDistanc() {
		return distanc;
	}

	public void setDistanc(String distanc) {
		this.distanc = distanc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }
}
