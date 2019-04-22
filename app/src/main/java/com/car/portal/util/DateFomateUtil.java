package com.car.portal.util;

/***
 * 日期转换类
 */
public class DateFomateUtil {

    /**
     * 获取系统当前时间
     * @return
     */
    public static long getDate(){
        long time=System.currentTimeMillis();
        return time;
    }

    /**
     * 转换时间
     * @param time
     * @return
     */
    public static String formatDate(long time) {
        String str="";
        long miao=time/1000;
        long fen=miao/60;
        long hour=fen/60;
        long f=(miao-(hour*60*60))/60;
        long m=miao%3600%60;

        if(hour<=0&&f<=0)
            str=m+"秒";
        else if(hour<=0)
            str=f+"分"+m+"秒";
        else
            str=hour+"小时"+f+"分"+m+"秒";
        return str;
    }

}
