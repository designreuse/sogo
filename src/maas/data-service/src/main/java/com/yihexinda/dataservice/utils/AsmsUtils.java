package com.yihexinda.dataservice.utils;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;

/**
 * 测试工具类
 * Created by youj on 2018/10/8.
 */
public class AsmsUtils {

    private static AsmsUtils instance = null;

    private AsmsUtils() {
    }

    public static AsmsUtils getInstance() {
        if (instance == null) {
            synchronized (AsmsUtils.class) {
                if (instance == null) {
                    instance = new AsmsUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 请求地址
     */
    public static String PATH = "/openapi/b2g/lticket";

    public static Map<String, String> getB2GParams(String dataXml, String Method, String hyid, String secrect) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", hyid);//会员id（必填）
        map.put("service", Method);//方法（必填）
        map.put("data", dataXml);//业务参数（必填）
        map.put("responseType", "1");//1:为xml  2:为josn
        map.put("sign", MD5Tool.MD5Encode(MD5Tool.MD5Encode(hyid) + dataXml + secrect));//加密方式:md5(md5(会员ID)+xml+密钥)（必填）
        map.put("logOpen", "");
        map.put("channel", "WEB");
        map.put("version", "1");
        map.put("compid", "SHGM");
        map.put("start", "0");
        map.put("count", "20");
        return map;
    }


    public static String sendHttpClient(String url, org.apache.commons.httpclient.NameValuePair[] params, String encode) throws Exception {
        if (org.apache.commons.lang3.StringUtils.isBlank(encode)) {
            encode = "UTF-8";
        }
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);
        method.addParameters(params);
        int statusCode = 0;
        String res = "";
        try {
            statusCode = client.executeMethod(method);
            byte[] responseBody = method.getResponseBody();
            res = StringUtils.trimToEmpty(new String(responseBody, encode));
        } catch (UnknownHostException e) {
            //域名解析异常
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            method.releaseConnection();
            client.getHttpConnectionManager().closeIdleConnections(0);
        }
        if (statusCode == HttpStatus.SC_OK) {
            return res;
        } else {
            throw new Exception("HTTP接收数据异常，状态码：" + statusCode + "错误内容:" + res);
        }
    }
}
