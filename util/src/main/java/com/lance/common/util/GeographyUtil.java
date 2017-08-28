package com.lance.common.util;

import java.util.Locale;

/**
 * 地理计算工具
 */
public class GeographyUtil {
    private GeographyUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 地球半径 单位米
     */
    public static final double EARTH_RADIUS = 6378137.0;

    /**
     * 返回单位米
     *
     * @param longitude1 第一个地理位置的经度
     * @param latitude1  第一个地理位置的纬度
     * @param longitude2 第二个地理位置的经度
     * @param latitude2  第二个地理位置的纬度
     * @return distance in meter
     */
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 将度数转换为弧度
     *
     * @param d degree
     * @return result
     */
    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 将弧度转换为度数
     * @param rad radian
     * @return result
     */
    public static double degree(double rad) {
        return rad * 180 / Math.PI;
    }

    /**
     * 将距离值转换为带单位的字符串
     *
     * @param distance distance
     * @return String
     */
    public static String formatDistance(double distance) {
        if (distance > 1000) {
            return String.format(Locale.getDefault(), "%.2f", (distance / 1000)) + "Km";
        }
        return distance + "m";
    }
}
