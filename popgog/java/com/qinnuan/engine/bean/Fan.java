package com.qinnuan.engine.bean;

import com.qinnuan.common.josn.EAJson;

import java.util.List;

/**
 * Created by Administrator on 13-7-16.
 */
public class Fan extends BaseBean {

    @EAJson private String userid;      //用户ID
    @EAJson private String profileimg;  //头像
    @EAJson private String nickname;    //昵称
    @EAJson private String signature;   //签名
    @EAJson private int distance;       //距离(米)
    @EAJson private int datediff;       //时间差(分钟)
    @EAJson private int usertype;       //用户类型 0=普通用户1=枪手用户2=官方/系统用户3=公共帐号
    @EAJson private String popgogid;    //爆谷号
    @EAJson private String remarkname;  //备注名
    @EAJson private int sex;            //性别 1=男 2=女 0=未知
    @EAJson private int ispullblack;    //是否拉黑 0=否 1=是
    @EAJson private int isfriend;       //是否好友 0=否 1=是
    @EAJson private String iconimage;   //icon图片(公共账号属性)
    @EAJson private String iconremark;  //icom描述(公共账号属性)
    @EAJson private String uorderid;    //订单ID
    @EAJson private String togetherseefilmid;  //一起看电影ID
    @EAJson private String phonenunber; //手机号码
    @EAJson private String phonename;   //手机联系人
    @EAJson private int isadded; //0=添加为影友（显示添加按钮）  1=（已是影友）

    private Dynamic dynamic;
    private Sns sns;

    private List<Dynamic> dynamics;
    private List<Sns> snss;


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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDatediff() {
        return datediff;
    }

    public void setDatediff(int datediff) {
        this.datediff = datediff;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public String getPopgogid() {
        return popgogid;
    }

    public void setPopgogid(String popgogid) {
        this.popgogid = popgogid;
    }

    public String getRemarkname() {
        return remarkname;
    }

    public void setRemarkname(String remarkname) {
        this.remarkname = remarkname;
    }

    /**
     * 性别 1=男 2=女 0=未知
     */
    public int getSex() {
        return sex;
    }

    /**
     * 性别 1=男 2=女 0=未知
     * @param sex
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public String getIconimage() {
        return iconimage;
    }

    public void setIconimage(String iconimage) {
        this.iconimage = iconimage;
    }

    public String getIconremark() {
        return iconremark;
    }

    public void setIconremark(String iconremark) {
        this.iconremark = iconremark;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }

    public Sns getSns() {
        return sns;
    }

    public void setSns(Sns sns) {
        this.sns = sns;
    }

    public int getIspullblack() {
        return ispullblack;
    }

    public void setIspullblack(int ispullblack) {
        this.ispullblack = ispullblack;
    }

    public String getUorderid() {
        return uorderid;
    }

    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }

    public String getTogetherseefilmid() {
        return togetherseefilmid;
    }

    public void setTogetherseefilmid(String togetherseefilmid) {
        this.togetherseefilmid = togetherseefilmid;
    }

    public List<Dynamic> getDynamics() {
        return dynamics;
    }

    public void setDynamics(List<Dynamic> dynamics) {
        this.dynamics = dynamics;
    }

    public List<Sns> getSnss() {
        return snss;
    }

    public void setSnss(List<Sns> snss) {
        this.snss = snss;
    }

    public String getPhonenunber() {
        return phonenunber;
    }

    public void setPhonenunber(String phonenunber) {
        this.phonenunber = phonenunber;
    }

    public String getPhonename() {
        return phonename;
    }

    public void setPhonename(String phonename) {
        this.phonename = phonename;
    }

    public int getIsadded() {
        return isadded;
    }

    public void setIsadded(int isadded) {
        this.isadded = isadded;
    }
}
