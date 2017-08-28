package com.lance.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA加密解密工具
 */
public class RSAUtil {
    private RSAUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJFBnk78A4CN5gBpIV/pGOGqi/CzvwjvXoj2gYXtbEIg+ZxpRrVi7Is6dwIK4+xrDr35ExaN1s4GnyF3g88z93iYpM5URhQTRJ/GGENNlozkLNARRdTJfLuJxBMZHnAGOtuNTXIcIo5/k8klllBYHTqG6xIVnjRN0vsV2UGlnW7VAgMBAAECgYBMoT9xD8aRNUrXgJ7YyFIWCzEUZN8tSYqn2tPt4ZkxMdA9UdS5sFx1/vv1meUwPjJiylnlliJyQlAFCdYBo7qzmib8+3Q8EU3MDP9bNlpxxC1go57/q/TbaymWyOk3pK2VXaX+8vQmllgRZMQRi2JFBHVoep1f1x7lSsf2TpipgQJBANJlO+UDmync9X/1YdrVaDOi4o7g3w9u1eVq9B01+WklAP3bvxIoBRI97HlDPKHx+CZXeODx1xj0xPOK3HUz5FECQQCwvdagPPtWHhHx0boPF/s4ZrTUIH04afuePUuwKTQQRijnl0eb2idBe0z2VAH1utPps/p4SpuT3HI3PJJ8MlVFAkAFypuXdj3zLQ3k89A5wd4Ybcdmv3HkbtyccBFALJgs+MPKOR5NVaSuF95GiD9HBe4awBWnu4B8Q2CYg54F6+PBAkBKNgvukGyARnQGc6eKOumTTxzSjSnHDElIsjgbqdFgm/UE+TJqMHmXNyyjqbaA9YeRc67R35HfzgpvQxHG8GN5AkEAxSKOlfACUCQ/CZJovETMmaUDas463hbrUznp71uRMk8RP7DY/lBnGGMeUeeZLIVK5X2Ngcp9nJQSKWCGtpnfLQ==";
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRQZ5O/AOAjeYAaSFf6Rjhqovws78I716I9oGF7WxCIPmcaUa1YuyLOncCCuPsaw69+RMWjdbOBp8hd4PPM/d4mKTOVEYUE0SfxhhDTZaM5CzQEUXUyXy7icQTGR5wBjrbjU1yHCKOf5PJJZZQWB06husSFZ40TdL7FdlBpZ1u1QIDAQAB";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 生成公钥和私钥
     *
     * @return 字符串数组 0为公钥 1为私钥
     */
    public static String[] getKeys() {
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            String publicKeyStr = getPublicKeyString(publicKey);
            String privateKeyStr = getPrivateKeyString(privateKey);
            return new String[]{publicKeyStr, privateKeyStr};
        } catch (NoSuchAlgorithmException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 公钥指数
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data      要加密的数据
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encryptByPublicKey(String data, String publicKey) {
        byte[] dataByte = data.getBytes();
        byte[] keyBytes = Base64Util.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory;
        ByteArrayOutputStream out = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            // Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = dataByte.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataByte, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return Base64Util.encode(encryptedData);
        } catch (NoSuchAlgorithmException |
                InvalidKeyException |
                BadPaddingException |
                NoSuchPaddingException |
                InvalidKeySpecException |
                IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 私钥解密
     *
     * @param data       要解密的密文
     * @param privateKey 私钥
     * @return 明文
     */
    public static String decryptByPrivateKey(String data, String privateKey) {
        byte[] encryptedData = Base64Util.decode(data);
        byte[] keyBytes = Base64Util.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory;
        ByteArrayOutputStream out = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            // Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData);
        } catch (NoSuchAlgorithmException |
                InvalidKeyException |
                NoSuchPaddingException |
                InvalidKeySpecException |
                IllegalBlockSizeException |
                BadPaddingException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 获取模数和密钥
     */
    public static Map<String, String> getModulusAndKeys() {
        Map<String, String> map = new HashMap<>();
        try {
            InputStream in = RSAUtil.class
                    .getResourceAsStream("/rsa.properties");
            Properties prop = new Properties();
            prop.load(in);

            String modulus = prop.getProperty("modulus");
            String publicKey = prop.getProperty("publicKey");
            String privateKey = prop.getProperty("privateKey");

            in.close();

            map.put("modulus", modulus);
            map.put("publicKey", publicKey);
            map.put("privateKey", privateKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyString 公钥数据字符串Base64
     */
    public static PublicKey loadPublicKey(String publicKeyString) {
        try {
            byte[] buffer = Base64Util.decode(publicKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从字符串中加载私钥
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyString 私钥base64字符串
     */
    public static PrivateKey loadPrivateKey(String privateKeyString) {
        try {
            byte[] buffer = Base64Util.decode(privateKeyString);
            // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取私钥字符串Base64
     *
     * @param privateKey 私钥
     */
    public static String getPrivateKeyString(PrivateKey privateKey) {
        return Base64Util.encode(privateKey.getEncoded());
    }

    /**
     * 获取公钥字符串Base64
     *
     * @param publicKey 公钥
     */
    public static String getPublicKeyString(PublicKey publicKey) {
        return Base64Util.encode(publicKey.getEncoded());
    }
}