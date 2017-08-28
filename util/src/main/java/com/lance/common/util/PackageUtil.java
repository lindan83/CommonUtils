package com.lance.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by lindan on 16-10-25.
 * 包管理工具
 */

public class PackageUtil {
    private PackageUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得软件版本名称
     *
     * @param context Context
     * @return version name
     */
    public static String getVersionName(Context context) {
        String versionName = "1.0.0";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取当前程序版本code
     *
     * @param context Context
     * @return package code
     */
    public static int getPackageCode(Context context) {
        int code;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            code = pi.versionCode;
        } catch (Exception e) {
            code = 1; // failed, ignored
        }
        return code;
    }

    /**
     * 获取当前程序包名
     *
     * @param context Context
     * @return package name
     */
    public static String getPackageName(Context context) {
        String version;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            version = pi.packageName;
        } catch (Exception e) {
            version = ""; // failed, ignored
        }
        return version;
    }

    /**
     * 清除缓存
     *
     * @param context Context
     */
    public static void clean(final Context context) {
        try {
            DataCleanUtil.cleanExternalCache(context.getApplicationContext());
            DataCleanUtil.cleanInternalCache(context.getApplicationContext());
            DataCleanUtil.cleanFiles(context.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
