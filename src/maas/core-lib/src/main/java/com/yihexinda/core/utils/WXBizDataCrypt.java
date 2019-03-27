package com.yihexinda.core.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/*
 * 文件名：WXBizDataCrypt.java
 * 版权：
 * 描述：
 * 修改人 tyj
 * 修改时间：2018-1-24
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */



/**
 * 对微信小程序用户加密数据的解密示例代码.
 * @author tyj
 * @version 2018-1-24
 * @see WXBizDataCrypt
 * @since
 */
public class WXBizDataCrypt
{

    private String appid;

    private String sessionKey;

    public WXBizDataCrypt(String appid, String sessionKey)
    {
        this.appid = appid;
        this.sessionKey = sessionKey;
    }

    /**
                *          检验数据的真实性，并且获取解密后的明文.
     * @param encryptedData string 加密的用户数据
     * @param iv string 与用户数据一同返回的初始向量
     *
     * @return data string 解密后的原文
     */
    public String decryptData(String encryptedData, String iv)
    {
        if (StringUtil.length(sessionKey) != 24)
        {
            return "ErrorCode::$IllegalAesKey;";
        }
        // 对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
        byte[] aesKey = Base64.decodeBase64(sessionKey);

        if (StringUtil.length(iv) != 24)
        {
            return "ErrorCode::$IllegalIv;";
        }
        // 对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
        byte[] aesIV = Base64.decodeBase64(iv);

        // 对称解密的目标密文为 Base64_Decode(encryptedData)
        byte[] aesCipher = Base64.decodeBase64(encryptedData);

        Map<String, String> map = new HashMap<>();

        try
        {
            byte[] resultByte = AESUtils.decrypt(aesCipher, aesKey, aesIV);

            if (null != resultByte && resultByte.length > 0)
            {
                String userInfo = new String(resultByte, "UTF-8");
                map.put("code", "0000");
                map.put("msg", "succeed");
                map.put("userInfo", userInfo);
                
                // watermark参数说明：
                // 参数  类型  说明
                // watermark   OBJECT  数据水印
                // appid   String  敏感数据归属appid，开发者可校验此参数与自身appid是否一致
                // timestamp   DateInt 敏感数据获取的时间戳, 开发者可以用于数据时效性校验'
                
                // 根据微信建议：敏感数据归属appid，开发者可校验此参数与自身appid是否一致
                // if decrypted['watermark']['appid'] != self.appId:
                JSONObject jsons = JSON.parseObject(userInfo);
                String id = jsons.getJSONObject("watermark").getString("appid");
                if(!StringUtils.equals(id, appid))
                {
                    return "ErrorCode::$IllegalBuffer;";
                }
            }
            else
            {
                map.put("status", "1000");
                map.put("msg", "false");
            }
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return JSON.toJSONString(map);
    }
    
    
    public static JSONObject getJsonObject( String appId,  String sessionKey, String encryptedData, String iv ) {
    	  WXBizDataCrypt biz = new WXBizDataCrypt(appId, sessionKey);
    	  String result = biz.decryptData(encryptedData, iv);
    	  if ( null != result ) {
    		  return JSONObject.parseObject( result );
    	  }
    	  return null;
    }
    
    
    
    
    
    /**
	 * 验证第三方合法性
	 * 
	 * @param uid
	 * @param accessToken
	 * @param oauthType
	 * @return
	 */
	public static JSONObject jscode2session2(String appid, String appsercet, String jsCode) {
		int i = 2;
		if (i == 1) {
			return JSONObject.parseObject("{'openid':'wechat', 'session_key':'wechat_session_key'}");
			// return "wechat";
		}

		String connurl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appsercet
				+ "&js_code=" + jsCode + "&grant_type=authorization_code";
		int mTimeout = 10000;
		StringBuilder resultData = new StringBuilder("");
		HttpURLConnection conn = null;
		try {
			URL url = new URL(connurl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setUseCaches(false);

			conn.setConnectTimeout(mTimeout);
			conn.connect();
			int resultCode = conn.getResponseCode();
			InputStreamReader in;
			if (resultCode == 200) {
				in = new InputStreamReader(conn.getInputStream());
				BufferedReader buffer = new BufferedReader(in);
				String inputLine;
				while ((inputLine = buffer.readLine()) != null) {
					resultData.append(inputLine);
					resultData.append("\n");
				}
				buffer.close();
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		String string = resultData.toString();
		if (string.contains("openid")) {
			JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(string);
			return json;
		}
		return null;
	}
    /**
     *  
     * @param args 
     * @see 
     */
    public static void main(String[] args)
    {
    	String appId = "wx1a5b0ee65f79eded&secret";
    	String sessionKey = "vhC9jMpeJ4DqOBMBpFxoyA==";
    	String encryptedData = "8u5IZkW44W6U42hRfwFFJtSHDPjizkYL06MmyMlB1bhIcK7LCLioBOzvWTEVGDsxW8yqwexwMk1uBwfKhW9kKcLTDEFKt28RIY772xJ7Qg60SJzCNbCkpZCGAkfk75NAqUqhdXan5/QAnpQtrNrNEWMH1vXKJOWwgckgqChR3U61YSvbA1j3c0SSPQk7KOj7t2KYGkVgqjZ/mDEVp0XymA==";
    	 

    	
    	  JSONObject code = jscode2session2("wx1a5b0ee65f79eded","e3cddb784dcf245cf8a88983ee14ef1f","011wP4Xd14nr5z0vVTZd1QyjXd1wP4X9");
    	  String object = (String)code.get("session_key");
    	  System.out.println("object--" + object);
          WXBizDataCrypt biz = new WXBizDataCrypt(appId, object);
           System.out.println(biz.decryptData(encryptedData,"zoq07DcG9e1vXFqjDDZlNg=="));
       
   
    
     
    }
}