package com.showu.baogu.xmpp.message;

/**
 * Created by showu on 13-7-8.
 */
public enum MessageType {
    TEXT(0),IMAGE(1),VOICE(2),LOCATION(3), HELPTEXT(4),HELPCINEMA(5),HELPACCOUNT(6), LINKTEXT(7),ACCOUNTTEXT(8),ACCOUNTSIGLE(9),
    ACCOUNTMULT(10),ACCOUNTIMAGE(11),ACCOUNTACTIVITY(12),DEFUILTLINKTEXT(13),DYNAMICNUMBER(14),LASTHEAED(15),LOOKACTIVITY(16),
    ADDFRIEND(17),DELETEFRIEND(18);
    private int value =-1;
    MessageType(int i) {
      this.value=i ;
    }

    @Override
    public String toString() {
        return String.valueOf(value) ;
    }
}
