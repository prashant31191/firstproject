package com.qinnuan.engine.bean.film;

import com.qinnuan.common.josn.EAJson;

import java.io.Serializable;

public class Seat implements Serializable {
	@EAJson
	private String loveind;
	@EAJson
	private String damagedflg;
	@EAJson
	private String typeind;
	@EAJson
	private String columnnum;
	@EAJson
	private String columnid;
	@EAJson
	private String rownum;
	@EAJson
	private String rowid;
	@EAJson
	private String issell;
    @EAJson
    private String userid;  //g用户ID
    @EAJson
    private String profileimg;  //g用户头像
    @EAJson
    private int sex;  //g0=未知-1=默认1=男2=女

	private boolean hi=false;

    private boolean mySelect=false ;

    public boolean isMySelect() {
        return mySelect;
    }

    public void setMySelect(boolean mySelect) {
        this.mySelect = mySelect;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isHi() {
		return hi;
	}

	public void setHi(boolean hi) {
		this.hi = hi;
	}

	public String getLoveind() {
		return loveind;
	}

	public void setLoveind(String loveind) {
		this.loveind = loveind;
	}

	public String getDamagedflg() {
		return damagedflg;
	}

	public void setDamagedflg(String damagedflg) {
		this.damagedflg = damagedflg;
	}

	public String getTypeind() {
		return typeind;
	}

	public void setTypeind(String typeind) {
		this.typeind = typeind;
	}

	public int getColumnnum() {
		return Integer.parseInt(columnnum);
	}

	public void setColumnnum(String columnnum) {
		this.columnnum = columnnum;
	}

	public String getColumnid() {
		return columnid;
	}

	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}

	public int getRownum() {
		return Integer.parseInt(rownum);
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

	public String getRowid() {
		return rowid;
	}

	public void setRowid(String rowid) {
		this.rowid = rowid;
	}

	public String getIssell() {
		return issell;
	}

	public void setIssell(String issell) {
		this.issell = issell;
	}

    public boolean isSystemSeat(){
        if(damagedflg.equals("Y")) return true ;
        if(issell.equals("0"))return  true ;
        return false ;
    }

    public boolean isLostSeat(){
        if(damagedflg.equals("Y")){
            return false ;
        }

        if(issell.equals("0")){
            return false ;
        }
        if(mySelect){
            return  false ;
        }
        return true  ;
    }

}
