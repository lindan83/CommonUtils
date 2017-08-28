package com.lance.common.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {
    private PatternUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 长度1-10的字母数字和中文字符串
     */
    public static final String REGEXP_VALID_INPUT_10 = "^[a-z|A-Z|0-9|\\u4E00-\\u9FA5]{1,10}$";

    /**
     * 长度1-20的字母数字和中文字符串
     */
    public static final String REGEXP_VALID_INPUT_20 = "^[a-z|A-Z|0-9|\\u4E00-\\u9FA5]{1,20}$";

    /**
     * 长度1-30的字母数字和中文字符串
     */
    public static final String REGEXP_VALID_INPUT_30 = "^[a-z|A-Z|0-9|\\u4E00-\\u9FA5]{1,30}$";

    /**
     * 长度1-100的字母数字和中文字符串
     */
    public static final String REGEXP_VALID_INPUT_100 = "^[a-z|A-Z|0-9|\\u4E00-\\u9FA5]{1,100}$";

    /**
     * 长度1-1000的字母数字和中文字符串
     */
    public static final String REGEXP_VALID_INPUT_1000 = "^[a-z|A-Z|0-9|\\u4E00-\\u9FA5]{1,1000}$";

    /**
     * Email
     */
    public static final String REGEXP_EMAIL = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * Mobile
     */
    public static final String REGEXP_MOBILE = "^[1][3458][0-9]{9}$";

    /**
     * URL
     */
    public static final String REGEXP_URL = "^(http|https|ftp|svn)://([a-zA-Z0-9]+[/?.?])" +
            "+[a-zA-Z0-9]*\\??([a-zA-Z0-9]*=[a-zA-Z0-9]*&?)*$";

    /**
     * URI
     */
    public static final String REGEXP_URI = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";

    /**
     * Phone
     */
    public static final String REGEXP_PHONE = "(^[0][1-9]{2,3}-[0-9]{5,10}$)|(^[1-9]{1}[0-9]{5,8}$)";

    /**
     * ID Card No.
     */
    public static final String REGEXP_ID_CARD_NUMBER = "(^\\d{14}[0-9a-zA-Z])|(^\\d{17}[0-9a-zA-Z])$";

    /**
     * 手机号验证
     *
     * @param str input
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        return !TextUtils.isEmpty(str) && str.matches(REGEXP_MOBILE);
    }

    /**
     * Email验证
     *
     * @param str input
     * @return 验证通过返回true
     */
    public static boolean isEmail(String str) {
        return !TextUtils.isEmpty(str) && str.matches(REGEXP_EMAIL);
    }

    /**
     * 电话号码验证
     *
     * @param str input
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        return !TextUtils.isEmpty(str) && str.matches(REGEXP_PHONE);
    }

    /**
     * 判断是否一个合法的URL
     *
     * @param str input
     * @return 验证通过返回true
     */
    public static boolean isUrl(String str) {
        return !TextUtils.isEmpty(str) && str.matches(REGEXP_URL);
    }

    /**
     * 是否是uri
     *
     * @param str input
     * @return 验证通过返回true
     */
    public static boolean isUri(String str) {
        return !TextUtils.isEmpty(str) && str.matches(REGEXP_URI);
    }

    /**
     * 是否身份证号码
     *
     * @param str input
     * @return 验证通过返回true
     */
    public static boolean isIdCardNumber(String str) {
        return !TextUtils.isEmpty(str) && str.matches(REGEXP_ID_CARD_NUMBER);
    }

    /**
     * 只允许字母、数字和汉字
     *
     * @param value     input
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 验证通过返回true
     */
    public static boolean isOnlyLetterNumberChineseCharacter(String value, int minLength, int maxLength) {
        if (value != null && (value.length() < minLength || value.length() > maxLength)) {
            return false;
        }
        String regEx = "^[^a-zA-Z0-9\u4E00-\u9FA5]{" + minLength + "," + maxLength + "}$";
        return Pattern.matches(regEx, value);
    }

    /**
     * 判断是否为指定长度范围的数字
     *
     * @param value     input
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 验证通过返回true
     */
    public static boolean isNumber(String value, int minLength, int maxLength) {
        if(TextUtils.isEmpty(value)) {
            return false;
        }
        value = value.trim();
        int len = value.length();
        if (len < minLength || len > maxLength) {
            return false;
        }
        Pattern p = Pattern.compile("^[0-9]{" + minLength + "," + maxLength + "}$");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    /**
     * 值是否符合正则表达式
     *
     * @param pattern pattern
     * @param value   value
     * @return 验证通过返回true
     */
    public static boolean isValid(String pattern, String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        return m.matches();
    }
}
