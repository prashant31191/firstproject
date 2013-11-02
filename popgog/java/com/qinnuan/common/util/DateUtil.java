package com.qinnuan.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateUtil {

	public static String getDisDate(String date) {
		Date target = new Date(date);
		if (getDisDay(date) == 0) {
			Date today = new Date();
			long distence = (today.getTime() - target.getTime()) / 1000;
			if (distence < 60) {
				return "刚刚";
			} else if (distence >= 60 && distence < 3600) {
				return (distence / 60) + "分钟前";
			} else {
				return (distence / 3600) + "小时前";
			}
		} else if (getDisDay(date) == 1) {
			return "昨天" + target.getHours() + ":" + target.getMinutes();
		} else if (getDisDay(date) == 2) {
			return "前天" + target.getHours() + ":" + target.getMinutes();
		} else {
			return "3天前" ;
			//return date.replace("/", "-");
		}
	}

	public static int getDisDay(String date) {
		Date today = new Date();
		Date target = new Date(date);
		int t = (int) today.getTime() / 24 / 3600 / 1000;
		int x = (int) target.getTime() / 24 / 3600 / 1000;
		return t - x;
	}

	public static String getDateString(Date date, String format) {
		SimpleDateFormat dateFarmat = new SimpleDateFormat(format);
		if (date != null) {
			return dateFarmat.format(date);
		} else {
			return null;
		}
	}

	public static Date getDate(String dateString) {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date date;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return date;
	}

	public static String getDayName(Date date, String format) {
		SimpleDateFormat dateFarmat = new SimpleDateFormat(format);
		int i = getBetweenDays(dateFarmat.format(date),
				dateFarmat.format(new Date()));
		if (i == 0) {
			return "今天";
		} else if (i == 1) {
			return "昨天";
		} else if (i == 2) {
			return "前天";
		}
		return dateFarmat.format(date);
	}
   public  static  String getDay(String d){
       Date date = new Date();
       String today = DateUtil.getDateString(date, "yyyy-MM-dd");
     if(d.equals(today)){
         return "今天" ;
     }

       Calendar c = Calendar.getInstance();
       c.setTime(date);
       c.add(c.DATE, 1);
       String tomorrow = DateUtil.getDateString(c.getTime(), "yyyy-MM-dd");
     if(tomorrow.equals(d)){
         return "明天" ;
     }
       c.setTime(date);
       c.add(c.DATE, 2);
       String after = DateUtil.getDateString(c.getTime(), "yyyy-MM-dd");

       if(after.equals(d)){
           return  "后天";
       }
       return "未知";
   }
	/**
	 * t2 要大于t1
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static int getBetweenDays(String t1, String t2) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int betweenDays = 0;
		try {
			Date d1 = format.parse(t1);
			Date d2 = format.parse(t2);
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(d1);
			c2.setTime(d2);
			// 保证第二个时间一定大于第一个时间
			if (c1.after(c2)) {
				c1 = c2;
				c2.setTime(d1);
			}
			int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
			betweenDays = c2.get(Calendar.DAY_OF_YEAR)
					- c1.get(Calendar.DAY_OF_YEAR);
			for (int i = 0; i < betweenYears; i++) {
				c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
				betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
			}
			return betweenDays;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public static int getBetweenDay(long time1, long time2) {
		Date date = DateUtil.getDate("1970-01-01 00:00:00");
		Date d1 = new Date(date.getTime() + time1 * 1000 + (8 * 3600000));
		Date d2 = new Date(date.getTime() + time2 * 1000 + (8 * 3600000));
		return getBetweenDays(getDateString(d2, "yyyy-MM-dd HH:mm:ss"),
				getDateString(d1, "yyyy-MM-dd HH:mm:ss"));
	}
	
	//添加天数
	@SuppressLint("SimpleDateFormat")
	public static String addDay(String createdate, long day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date beforeDate = getDateByString(createdate);
		long time = beforeDate.getTime();
		day = day * 24 * 60 * 60 * 1000;
		time += day;
		return format.format(new Date(time));
	}
	
	public static Date getDateByString(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date returnDate = null;
		try {
			returnDate = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnDate;
	}
}
