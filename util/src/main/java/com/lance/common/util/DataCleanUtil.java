package com.lance.common.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import java.io.File;


/**
 * 应用数据清除工具
 * 描    述:
 * 主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录
 */
class DataCleanUtil {
    private DataCleanUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     *
     * @param context Context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     *
     * @param context Context
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File(Environment.getDataDirectory(), context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     *
     * @param context Context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File(Environment.getDataDirectory(), context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库
     *
     * @param context Context
     * @param dbName  数据库文件名
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 按名字清除本应用SharePreference
     *
     * @param context Context
     * @param spName  SharePreference名称
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void cleanSharedPreferenceByName(Context context, String spName) {
        context.deleteSharedPreferences(spName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     *
     * @param context Context
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context Context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     *
     * @param filePath 文件路径
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除本应用所有的数据
     *
     * @param context  Context
     * @param filepath 文件路径
     */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * 只删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param directory 要删除的指定目录
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}