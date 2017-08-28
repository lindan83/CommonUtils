package com.lance.common.util;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * Created by lindan on 16-10-12.
 * 拼音工具
 */

public class PinyinUtil {
    private PinyinUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取指定字符串的拼音
     *
     * @param s input
     * @return pinyin result
     */
    public static String getPinyin(String s) {
        if (!TextUtils.isEmpty(s)) {
            StringBuilder tagBuilder = new StringBuilder(s.length() * 4);
            char[] charArray = s.toCharArray();
            for (char c : charArray) {
                if (Pinyin.isChinese(c)) {
                    tagBuilder.append(Pinyin.toPinyin(c));
                } else {
                    tagBuilder.append(String.valueOf(c));
                }
            }
            return tagBuilder.toString();
        }
        return "";
    }

    /**
     * 检测是否中文
     *
     * @param c Character input
     * @return result
     */
    public static boolean isChinese(char c) {
        return Pinyin.isChinese(c);
    }
}
