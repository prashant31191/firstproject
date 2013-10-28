package com.showu.baogu.xmpp.message;

/**
 * Created by showu on 13-7-24.
 */
public enum SessionType {
    HELLO(0),MESSAGE(1),HELP(2),ACCOUNT(3),DYNAMICNUMBER(4),LASTHEAD(5);
    int value ;
    SessionType(int i) {
       this.value=i ;
    }

    @Override
    public String toString() {
        return  String.valueOf(value) ;
    }
}
