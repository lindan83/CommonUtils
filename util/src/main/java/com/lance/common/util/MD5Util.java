package com.lance.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lindan on 2015/12/16.
 * md5转换工具
 */
public class MD5Util {
    private static final int STREAM_BUFFER_LENGTH = 1024;
    private static final String CHARSET_NAME = "UTF-8";

    private MD5Util() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static MessageDigest getDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5");
    }

    private static MessageDigest updateDigest(final MessageDigest digest, final InputStream data) throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        while (read > -1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }
        return digest;
    }

    public static byte[] md5(String txt) {
        try {
            return md5(txt.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] md5(byte[] bytes) {
        try {
            MessageDigest digest = getDigest();
            digest.update(bytes);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] md5(InputStream is) throws NoSuchAlgorithmException, IOException {
        return updateDigest(getDigest(), is).digest();
    }

    /**
     * 获取指定字符串的MD5字符串
     *
     * @param string 输入
     * @return 结果
     */
    public static String getMd5String(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(CHARSET_NAME));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
