package com.car.portal.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.internal.LinkedTreeMap;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class LinkMapToObjectUtil {

	private static final SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private LinkMapToObjectUtil() {
	}

	@SuppressWarnings({ "rawtypes"})
	public static void getObject(LinkedTreeMap<String, Object> map, Object cls) {
		if(map == null || cls == null) return;
		Field[] fs = cls.getClass().getDeclaredFields();
		for (Field field : fs) {
			Type type = field.getType();
			String name = field.getName();
			if(!name.contains("serialVersion")) {
				Object object = map.get(field.getName());
				if(object  != null) {
					StringBuffer setName = new StringBuffer("set");
					setName.append(name);
					if(setName.charAt(3) >= 'a' && setName.charAt(3) <= 'z') {
						setName.replace(3, 4, (char)(setName.charAt(3) - 32) + "");
					}
					try {
						Method m = cls.getClass().getMethod(setName.toString(), (Class) type);
						if(m != null && object != null) {
							if(type == Integer.class || type == int.class) {
								m.invoke(cls, ((Double)object).intValue());
							} else if(type == Byte.class || type == byte.class) {
								m.invoke(cls, ((Double)object).byteValue());
							} else if(type == Short.class || type == short.class) {
								m.invoke(cls, ((Double)object).shortValue());
							} else if(type == Long.class || type == long.class) {
								m.invoke(cls, ((Double)object).longValue());
							} else if(type == Date.class) {
								if(object != null) {
									m.invoke(cls,  sp.parse((String)object));
								}
							} else if(type == Character.class || type == char.class) {
								if(object instanceof String) {
									char[] chs = ((String) object).toCharArray();
									if(chs != null && chs.length > 0) {
										m.invoke(cls, chs[0]);
									}
								} else {
									m.invoke(cls, (char)((Double)object).intValue());
								}
							} else if(type == Boolean.class || type == boolean.class) {
								m.invoke(cls, object.equals("true") || object.equals(true));
							} else {
								m.invoke(cls, object);
							}
						}

					} catch (NoSuchMethodException e) {
						LogUtils.e("parseJSON", setName + " set" + object);
					} catch (IllegalAccessException e) {
						LogUtils.e("parseJSON", setName + "  set" + object);
					} catch (IllegalArgumentException e) {
						LogUtils.e("parseJSON", setName + "  set" + object);
					} catch (InvocationTargetException e) {
						LogUtils.e("parseJSON", setName + " set" + object);
					} catch (ParseException e) {
						LogUtils.e("parseJSON", setName + "  set" + object);
					}
				}
			}
		}
	}

}
