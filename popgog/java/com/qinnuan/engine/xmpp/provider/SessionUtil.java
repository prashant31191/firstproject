package com.qinnuan.engine.xmpp.provider;

import android.content.Context;

import com.qinnuan.engine.xmpp.message.SessionType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by showu on 13-8-14.
 */
public class SessionUtil {
    private static Map<SessionType,AbstractSessionDao> map = new HashMap<SessionType, AbstractSessionDao>();
    private static  SessionUtil instance ;
    public static SessionUtil getInstance(Context context){
        if(instance==null){
            instance=new SessionUtil(context) ;
        }
        return instance ;
    }
    private  SessionUtil(Context context){
        map.put(SessionType.DYNAMICNUMBER,new DynamicNumberSessionDaoImpl(context));
        map.put(SessionType.LASTHEAD,new LastHeadSessionDaoImpl(context));
    }
    public  AbstractSessionDao getDao(SessionType type){
        return  map.get(type) ;
    }
}
