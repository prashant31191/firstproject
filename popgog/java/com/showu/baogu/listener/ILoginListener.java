package com.showu.baogu.listener;

/**
 * Created by Administrator on 13-7-17.
 */
public interface ILoginListener {

    public void login(String phone, String psw);

    public void loginByQQ();

    public void loginByWeibo();

    public void forgetPass();

    public void regist();

}
