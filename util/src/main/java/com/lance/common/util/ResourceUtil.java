package com.lance.common.util;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * 资源获取工具
 */
public class ResourceUtil {
    private ResourceUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取drawable资源ID
     */
    public static int getDrawableByName(Context context, String name) {
        return getValueIntByName(context, name, "drawable");
    }

    /**
     * 获取mipmap资源ID
     */
    public static int getMipmapByName(Context context, String name) {
        return getValueIntByName(context, name, "mipmap");
    }

    /**
     * 获取layout资源ID
     */
    public static int getLayoutByName(Context context, String name) {
        return getValueIntByName(context, name, "layout");
    }

    /**
     * 获取id资源ID
     */
    public static int getIdByName(Context context, String name) {
        return getValueIntByName(context, name, "id");
    }

    /**
     * 获取string资源ID
     */
    public static int getStringByName(Context context, String name) {
        return getValueIntByName(context, name, "string");
    }

    /**
     * 获取dimen资源ID
     */
    public static int getDimenByName(Context context, String name) {
        return getValueIntByName(context, name, "dimen");
    }

    /**
     * 获取anim资源ID
     */
    public static int getAnimByName(Context context, String name) {
        return getValueIntByName(context, name, "anim");
    }

    /**
     * 获取animator资源ID
     */
    public static int getAnimatorByName(Context context, String name) {
        return getValueIntByName(context, name, "animator");
    }

    /**
     * 获取color资源ID
     */
    public static int getColorByName(Context context, String name) {
        return getValueIntByName(context, name, "color");
    }

    /**
     * 获取指定类型的资源ID
     *
     * @param context Context
     * @param name    资源名称
     * @param type    drawable/mipmap/layout/id/string/dimen/color/anim/animator
     */
    public static int getValueIntByName(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type,
                context.getPackageName());
    }

    /**
     * 对于 context.getResources().getIdentifier()无法获取的数据, 或者数组资源利用反射获取
     *
     * @param name    资源名称
     * @param type    drawable/mipmap/layout/id/string/dimen/color/anim/animator
     * @param context Context
     */
    private static Object getResourceId(Context context, String name, String type) {
        String className = context.getPackageName() + ".R";
        try {
            Class<?> cls = Class.forName(className);
            for (Class<?> childClass : cls.getClasses()) {
                String simple = childClass.getSimpleName();
                if (simple.equals(type)) {
                    for (Field field : childClass.getFields()) {
                        String fieldName = field.getName();
                        if (fieldName.equals(name)) {
                            return field.get(null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取styleable资源
     *
     * @param name 资源名称
     */
    public static int getStyleable(Context context, String name) {
        return (Integer) getResourceId(context, name, "styleable");
    }

    /**
     * 获取styleable的ID号数组
     *
     * @param name 资源名称
     */
    public static int[] getStyleableArray(Context context, String name) {
        return (int[]) getResourceId(context, name, "styleable");
    }
}
