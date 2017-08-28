package com.lance.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lindan on 16-10-25.
 * Base64编解码工具
 */

public class Base64Util {
    private Base64Util() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes 原始数据
     * @return base字符串
     */
    public static String encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 将文件编码成Base64字符串
     *
     * @param filePath 文件路径
     * @return base64 String
     */
    public static String encode(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }
        return encode(file);
    }

    /**
     * 将文件编码成Base64字符串
     *
     * @param file 文件对象
     * @return base64 String
     */
    public static String encode(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        ByteArrayOutputStream bos = null;
        BufferedInputStream bis = null;
        int count;
        byte[] data = null;
        try {
            bos = new ByteArrayOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            while ((count = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            data = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (data != null) {
            return Base64.encodeToString(data, Base64.DEFAULT);
        }
        return "";
    }

    /**
     * 将位图编码成Base64字符串
     *
     * @param bitmap bitmap to encode
     * @return base64 string
     */
    public static String encode(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }
        String result = "";
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            byte[] bitmapBytes = bos.toByteArray();
            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * BASE64字符串解码为二进制数据
     *
     * @param base64 要还原的base64字符串
     * @return 原始数据
     */
    public static byte[] decode(String base64) {
        return Base64.decode(base64, Base64.DEFAULT);
    }

    /**
     * 解码base64到文件
     *
     * @param base64       base64 string
     * @param saveFilePath 要保存的文件绝对路径
     * @return file 原始文件
     */
    public static File decodeToFile(String base64, String saveFilePath) {
        if (TextUtils.isEmpty(base64)) {
            return null;
        }
        File file = new File(saveFilePath);
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bytes, 0, bytes.length);
            bos.flush();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解码base64到Bitmap
     *
     * @param base64 base64 string
     * @return bitmap
     */
    public static Bitmap decodeToBitmap(String base64) {
        if (TextUtils.isEmpty(base64)) {
            return null;
        }
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
