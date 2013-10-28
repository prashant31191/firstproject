package com.showu.baogu.api;

import com.showu.baogu.application.BaoguApplication;
import com.showu.common.util.GUIUtil;

import java.lang.reflect.Field;

public abstract class AbstractParam {
    public abstract String getApi();

    public String getString() {
        StringBuilder sb = new StringBuilder("?");
        reflectFiled(sb);
        return sb.toString().substring(0, sb.length() - 1);
    }

    public String postString() {
        StringBuilder sb = new StringBuilder("");
        reflectFiled(sb);
        return sb.toString().substring(0, sb.length() - 1);
    }

    private void reflectFiled(StringBuilder sb) {
        Class clazz = getClass();
        Field[] field = clazz.getDeclaredFields();
        try {
            for (Field f : field) {
                f.setAccessible(true);
                if (f.get(this)!=null&&!f.getName().equals("api")) {
                    sb.append(f.getName()).append("=")
                            .append(f.get(this).toString()).append("&");
                }
            }
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
