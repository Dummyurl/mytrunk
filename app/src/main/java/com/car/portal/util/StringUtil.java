package com.car.portal.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;

import com.car.portal.R;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.ApplicationConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class StringUtil {
    public final static long RELOGIN_TIME = 1000*10;  //重新登录的时间间隔
    private StringUtil () {
    }

    public static boolean isNullOrEmpty (String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isCarNum (String carnumber) {
    /*
        车牌号格式：汉字 + A-Z + 5位A-Z或0-9
        （只包括了普通车牌号，教练车和部分部队车等车牌号不包括在内）
        */
        String carnumRegex = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
        if (TextUtils.isEmpty(carnumber)) {
            return false;
        } else {
            return carnumber.matches(carnumRegex);
        }
    }

    /**
     * 服务器地址
     *
     * @param context
     * @return
     */
    public static String getHost (Context context) {
        StringBuffer bf = new StringBuffer("https://");
        bf.append(ApplicationConfig.getIntance().getHost());
        String port = ApplicationConfig.getIntance().getPort();
        if (port != null && !port.equals("80")) {
            bf.append(":");
            bf.append(port);
        }
        String project = context.getResources().getString(R.string.project_name);
        if (project == null || "".equals(project)) {
            bf.append("/");
        } else {
            bf.append("/");
            bf.append(project);
            bf.append("/");
        }
        return bf.toString();
    }

    /**
     * 电话号码是否正确
     *
     * @param phone
     * @return
     */
    public static boolean isPhone (String phone) {
        String reg = "^1[3-9][0-9]{9}$";
        return Pattern.matches(reg, phone);
    }

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    public static String isIDCardValidate (String IDStr) {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }

        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }

        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (java.text.ParseException e) {
            System.out.println(e.getMessage());
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        Hashtable<String, String> h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        return "";
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable<String, String> GetAreaCode () {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric (String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean getBooleanValue (String value) {
        if (value.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isDate (String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFixedPhone (String phone) {
        String rule = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static Spannable createDiffrentStyle (String string, int[] styles, int[] lens) {
        if (isNullOrEmpty(string)) {
            return new SpannableStringBuilder("");
        } else {
            SpannableStringBuilder builder = new SpannableStringBuilder(string);
            if (styles == null || styles.length == 0) {
                styles = new int[]{R.style.text_Sixteen_Black};
            }
            if (lens == null || lens.length == 0) {
                lens = new int[]{string.length()};
            }
            int min_len = Math.min(lens.length, styles.length);
            int sum = 0;
            for (int i = 0; i < min_len - 1; i++) {
                if (sum >= string.length()) {
                    break;
                } else {
                    TextAppearanceSpan sp = new TextAppearanceSpan(MyApplication.getContext(), styles[i]);
                    int len = sum + lens[i] > string.length() ? string.length() : sum + lens[i];
                    builder.setSpan(sp, sum, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sum = len;
                }
            }
            if (sum < string.length()) {
                TextAppearanceSpan sp = new TextAppearanceSpan(MyApplication.getContext(), styles[styles.length - 1]);
                builder.setSpan(sp, sum, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return builder;
        }
    }

    public static Spannable createImageString (String string, int res, Context context) {
        if (isNullOrEmpty(string)) {
            return new SpannableStringBuilder("");
        } else {
            SpannableStringBuilder builder = new SpannableStringBuilder(string);
            builder.append("icon");
            int len = string.length();
            Bitmap b = BitmapFactory.decodeResource(context.getResources(), res);
            ImageSpan imgSpan = new ImageSpan(context, b);
            builder.setSpan(imgSpan, len, len + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
    }
}
