package com.lance.common.util;

import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密工具类，密钥必须是16位字符串
 */
public class AESUtil {
    private AESUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 偏移量,必须是16位字符串
     */
    public static final String IV_STRING = "16-Bytes--String";

    /**
     * 默认的密钥
     */
    public static final String DEFAULT_KEY = "1bd83b249a414036";

    /**
     * 产生随机密钥(这里产生密钥必须是16位)
     */
    public static String generateKey() {
        String key = UUID.randomUUID().toString();
        key = key.replace("-", "").substring(0, 16);// 替换掉-号
        return key;
    }

    /**
     * 加密数据
     *
     * @param key     16位密钥
     * @param content 要加密的内容
     * @param offset  16位偏移量
     * @return 密文
     */
    public static String encryptData(String key, String content, String offset) {
        byte[] encryptedBytes;
        try {
            byte[] byteContent = content.getBytes("UTF-8");
            // 注意，为了能与 iOS 统一
            // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = offset.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedBytes = cipher.doFinal(byteContent);
            // 同样对加密后数据进行 base64 编码
            return Base64Util.encode(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密数据
     *
     * @param key     16位密钥
     * @param content 密文
     * @param offset  16位偏移量
     * @return 明文
     */
    public static String decryptData(String key, String content, String offset) {
        try {
            // base64 解码
            byte[] encryptedBytes = Base64Util.decode(content);
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = offset.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(encryptedBytes);
            return new String(result, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
