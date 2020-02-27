/**
 * 文件名：AesUtils
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/8/22
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Security;
import java.security.SecureRandom;

/**
 * AES加密解密工具类
 *
 * AES相比同类对称加密算法速度算是非常快，比如在有AES-NI的x86服务器至少能达到几百M/s的速度。安全性在可预见的未来是基本等同的，
 * 因为即使是128位也足够复杂无法被暴力破解。现在112位密码还在商业应用，而128位是112位的几万倍，所以在实务中用128位比较划算(稍节约资源)。
 *
 * AES256比128大概需要多花40%的时间，用于多出的4轮round key生成以及对应的SPN操作。
 * 另外，产生256-bit的密钥可能也需要比128位密钥多些开销，不过这部分开销应该可以忽略。
 * 安全程度自然是256比128安全，因为目前除了暴力破解，并没有十分有效的代数攻击方法。
 *
 * AES128和AES256主要区别是密钥长度不同（分别是128bits,256bits)、加密处理轮数不同（分别是10轮，14轮），后者强度高于前者。当前AES是较为安全的公认的对称加密算法。
 *
 * @author 周赟
 * @date 2019/8/22
 */
public class AesUtils {

    /**
     *  算法/模式/填充                 字节加密后数据长度       不满16字节加密后长度
     *  AES/CBC/NoPadding                   16                          不支持
     *  AES/CBC/PKCS5Padding                32                          16
     *  AES/CBC/ISO10126Padding             32                          16
     *  AES/CFB/NoPadding                   16                          原始数据长度
     *  AES/CFB/PKCS5Padding                32                          16
     *  AES/CFB/ISO10126Padding             32                          16
     *  AES/ECB/NoPadding                   16                          不支持
     *  AES/ECB/PKCS5Padding                32                          16
     *  AES/ECB/ISO10126Padding             32                          16
     *  AES/OFB/NoPadding                   16                          原始数据长度
     *  AES/OFB/PKCS5Padding                32                          16
     *  AES/OFB/ISO10126Padding             32                          16
     *  AES/PCBC/NoPadding                  16                          不支持
     *  AES/PCBC/PKCS5Padding               32                          16
     *  AES/PCBC/ISO10126Padding            32                          16
     */
    private static final String AES_CBC_PKCS7PADDING = "AES/CBC/PKCS7Padding";

    /**
     * 默认向量常量
     */
    private static final String IV = "1234567890123456";

    /**
     * 默认编码格式
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 密钥生成KEY
     */
    private static final String SECRET_KEY_AES = "AES";

    /**
     * AES256
     */
    private static final int AES_256 = 256;

    /**
     * 使用PKCS7Padding填充必须添加一个支持PKCS7Padding的Provider
     * 类加载的时候就判断是否已经有支持256位的Provider,如果没有则添加进去
     */
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 加密
     *
     * @param content 明文
     * @param pkey    生成密钥的密码值
     * @return
     */
    public static String aesEncrypt(String content, String pkey) throws Exception{
        byte[] result = aesEncryptOrDecrypt(Cipher.ENCRYPT_MODE, content.getBytes(CHARSET), pkey, AES_256, AES_CBC_PKCS7PADDING);
        return Base64.encodeBase64String(result);
    }


    /**
     * 解密
     *
     * @param content 密文
     * @param pkey    生成密钥的密码值
     * @return result  解密后的明文
     * @throws Exception
     */
    public static String aesDecode(String content, String pkey) throws Exception {
        byte[] result = aesEncryptOrDecrypt(Cipher.DECRYPT_MODE, Base64.decodeBase64(content), pkey, AES_256, AES_CBC_PKCS7PADDING);
        return new String(result,CHARSET);
    }

    /**
     * AES对称加密解密公共方法
     * @param mode 加密解密模式
     * @param byteContent 加密解密内容
     * @param key 生成密钥的密码值
     * @param aesType aes加解密方式（128/192/256）,主要影响密钥位数
     * @param modeAndPadding 算法/模式/填充
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    private static byte[] aesEncryptOrDecrypt(int mode, byte[] byteContent, String key, int aesType, String modeAndPadding)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        /**
         * 生成密钥【注意：128密钥位数16，192密钥位数24，256密钥位数32】
         */
        KeyGenerator kGen = KeyGenerator.getInstance(SECRET_KEY_AES);
        // 此处解决mac，linux报错
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes());
        kGen.init(aesType, random);
        SecretKey secretKey = kGen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, SECRET_KEY_AES);

        /**
         * 创建密码器
         */
        Cipher cipher = Cipher.getInstance(modeAndPadding);

        /**
         * 指定一个初始化向量 (Initialization vector，IV)， IV 必须是16位
         */
        cipher.init(mode, keySpec, new IvParameterSpec(IV.getBytes(CHARSET)));

        /**
         * 加/解密
         */
        byte [] result = cipher.doFinal(byteContent);

        return result;
    }

    /**
     * Demo
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String content = "Hello World!";
        String key = "admin@qinjee.cn";

        System.out.println("明文:" + content);
        String aesEncryptStr = aesEncrypt(content, key);
        System.out.println("密文:" + aesEncryptStr);
        String aesDecodeStr = aesDecode(aesEncryptStr, key);
        System.out.println("解密明文:" + aesDecodeStr);
    }
}
