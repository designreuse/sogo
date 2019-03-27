package com.yihexinda.core.utils;
/*
 * 文件名：AESUtils.java
 * 版权：
 * 描述：
 * 修改人：tyj
 * 修改时间：2018-1-24
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 *
 * @author tyj
 * @version 2018-1-24
 * @see AESUtils
 * @since
 */
public class AESUtils
{
    public static boolean initialized = false;

    /**  
     * AES解密  
     * @param content 密文  
     * @return  
     * @throws InvalidAlgorithmParameterException   
     * @throws NoSuchProviderException   
     */
    public static byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte)
        throws InvalidAlgorithmParameterException
    {
        initialize();
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");

            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化     
            byte[] result = cipher.doFinal(content);
            return result;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchProviderException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void initialize()
    {
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    /**
     * 生成iv
     * @param iv
     * @return
     * @throws Exception 
     * @see
     */
    public static AlgorithmParameters generateIV(byte[] iv)
        throws Exception
    {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }

}
