package com.qinnuan.engine.fragment.message;

/**
 * Created by showu on 13-7-18.
 */
public interface IChatOption {
    public  void startCamara();
    public void  startImageSelect();
    public void startLocation() ;
    public void sendTextMessage(String text) ;
    public void sendVoiceMessage(String path,int length) ;
}
