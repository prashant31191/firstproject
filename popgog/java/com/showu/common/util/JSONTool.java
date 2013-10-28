package com.showu.common.util;


import com.google.gson.Gson;
import com.showu.common.josn.EAJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JSONTool {
	public static Field[] getField(Class cls, Class an) {
		List<Field> list = new ArrayList<Field>();
		Field[] fs = cls.getDeclaredFields();
		for (Field t : fs) {
			if (t.isAnnotationPresent(an)) {
				list.add(t);
			}
		}
		Class superClass = cls.getSuperclass();
		while (superClass != null) {
			Field[] fields = superClass.getDeclaredFields();
			for (Field t : fields) {
				if (t.isAnnotationPresent(an)) {
					list.add(t);
				}
			}
			superClass = superClass.getSuperclass();
		}
		return list.toArray(new Field[0]);
	}



	public static <T> T jsonToBean(Class<T> classse, JSONObject json) {
		try {
			T t = classse.newInstance();
			Field[] fields = JSONTool.getField(classse, EAJson.class);
			for (Field field : fields) {
				field.setAccessible(true);
				EAJson eAnotation = field.getAnnotation(EAJson.class);
				String key = "".equals(eAnotation.jsonKey()) ? field.getName()
						: eAnotation.jsonKey();
				if (!json.isNull(field.getName())) {
					if (field.getType() == String.class) {
						field.set(t, json.getString(key));
					} else if (field.getType() == long.class) {
						field.set(t, json.getLong(key));
					} else if (field.getType() == int.class) {
						field.set(t, json.getInt(key));
					} else if (field.getType() == float.class) {
						// field.set(t, json.getString(key));
						field.set(t, json.getLong(key));
					} else if (field.getType() == boolean.class) {
						field.set(t, json.getBoolean(key));
					}
				}
			}
			return t;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> List<T> getJsonToListBean(Class<T> classes,
			JSONArray array) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length(); i++) {
			try {
				T t = jsonToBean(classes, array.getJSONObject(i));
				list.add(t);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

    public static <T> T jsonToBean(Class<T> classse, String json) {
        try {
            JSONObject jsonObject=new JSONObject(json) ;
            return jsonToBean(classse,jsonObject) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}