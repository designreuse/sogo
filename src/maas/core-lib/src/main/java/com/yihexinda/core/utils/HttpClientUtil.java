package com.yihexinda.core.utils;

import com.google.common.collect.Lists;
import com.yihexinda.core.constants.Constants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/04
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class); // 日志记录
    private static RequestConfig requestConfig = null;
    static
    {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
    }

    /**
     * post请求传输json参数
     * @param url  url地址
     * @param jsonParam 参数
     * @return
     */
    public static JSONObject httpPost(String url, JSONObject jsonParam) {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            if (null != jsonParam)
            {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String str = "";
                try {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    // 把json字符串转换成json对象
                    jsonResult = JsonUtil.object2JsonObject(str);
                } catch (Exception e){
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            logger.error("post请求提交失败:" + url, e);
        }finally{
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * post请求传输json参数
     * @param url  url地址
     * @param jsonParam 参数
     * @return
     */
    public static JSONArray httpPost(String url, JSONArray jsonParam) {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONArray jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            if (null != jsonParam)
            {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String str = "";
                try {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    // 把json字符串转换成json对象
                    jsonResult = JSONArray.fromObject(str);
                } catch (Exception e){
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            logger.error("post请求提交失败:" + url, e);
        }finally{
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * post请求传输json参数
     * @param url  url地址
     * @param jsonParam 参数
     * @return
     */
    public static String httpPost2(String url, JSONObject jsonParam) {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            if (null != jsonParam)
            {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String str = "";
                try {
                    // 读取服务器返回过来的json字符串数据
                    jsonResult = EntityUtils.toString(result.getEntity(), "utf-8");
                } catch (Exception e){
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            logger.error("post请求提交失败:" + url, e);
        }finally{
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * post请求传输String参数 例如：name=Jack&sex=1&type=2
     * Content-type:application/x-www-form-urlencoded
     * @param url            url地址
     * @param strParam       参数
     * @return
     */
    public static JSONObject httpPost(String url, String strParam) {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try {
            if (null != strParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = "";
                try {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    // 把json字符串转换成json对象
                    jsonResult = JsonUtil.object2JsonObject(str);
                }catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            logger.error("post请求提交失败:" + url, e);
        } finally {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * 发送get请求
     * @param strUrl 路径
     * @return
     */
    public static JSONObject httpGet(String strUrl) {
        // get请求返回结果
        JSONObject jsonResult = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = null;
        try {
            // 发送get请求
            URL url = new URL(strUrl);
            URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
            request = new HttpGet(uri);
            //设置代理IP，设置连接超时时间 、 设置 请求读取数据的超时时间 、 设置从connect Manager获取Connection超时时间、
            HttpHost proxy = new HttpHost("127.0.0.1",1080);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setProxy(proxy)
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectionRequestTimeout(3000)
                    .build();
            request.setConfig(requestConfig);

            CloseableHttpResponse response = client.execute(request);

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                String strResult = EntityUtils.toString(entity, "utf-8");
                // 把json字符串转换成json对象
                jsonResult = JsonUtil.object2JsonObject(strResult);
            } else {
                logger.error("get请求提交失败:" + strUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            logger.error("get请求提交失败:" + strUrl, e);
        } finally {
            request.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * 发送get请求
     * @param strUrl 路径
     * @return
     */
    public static String get(String strUrl) {
        // get请求返回结果
        String jsonResult = null;
        HttpGet request = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            // 发送get请求
            URL url = new URL(strUrl);
            URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
            request = new HttpGet(uri);
            //设置代理IP，设置连接超时时间 、 设置 请求读取数据的超时时间 、 设置从connect Manager获取Connection超时时间、
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectionRequestTimeout(3000)
                    .build();
            request.setConfig(requestConfig);

            CloseableHttpResponse response = client.execute(request);
            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                jsonResult = EntityUtils.toString(entity, "utf-8");
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            logger.error("get请求提交失败:" + strUrl, e);
        } finally {
            request.releaseConnection();
        }
        return jsonResult;
    }






    public static void main(String[] args) {
        /**
         * 交通中心平峰算法接口
         */
        Map json = new HashMap<Object,Object>();
        Map temp = new HashMap<Object,Object>();
        //已完成订单
        List<Map<Object,Object>> compleList =  new ArrayList<>();

        //已完成订单
        HashMap<Object, Object> dest = new HashMap<>();
        dest.put("id",0);
        dest.put("lati",122.12345678);
        dest.put("lng",25.12345678);
        temp.put("dest",dest);
        temp.put("orderID","o_20181205105600");
        temp.put("ordertime",new Date());
        HashMap<Object, Object> orignal = new HashMap<>();
        orignal.put("id",1);
        orignal.put("lati",122.12345679);
        orignal.put("lng",25.12345679);
        temp.put("orignal",orignal);
        temp.put("people",1);
        temp.put("ordertime",new Date().getTime());
        temp.put("userID",12);
        compleList.add(temp);
        json.put("completeOrder",compleList);

        //未上车
        List<Map<Object,Object>> unTakeList =  new ArrayList<>();
        Map data = new HashMap<Object,Object>();
        data.put("priority",1);
        data.put("untakeOrderList",compleList);
        unTakeList.add(data);
        json.put("unTakeOrder",unTakeList);

        //车辆信息
        List<Map<Object,Object>> vehicleList =  new ArrayList<>();
        data.clear();
        data.put("curLocation",dest);
        data.put("dirAngle",2);
        data.put("holdPeople",20);
        data.put("vehicleID","c_id100");
        vehicleList.add(data);
        json.put("vehicleList",vehicleList);
//        json = JsonUtil.object2JsonObject(data);
        System.out.println(json);
//        System.out.println(JsonUtil.object2JsonObject(json));
//        JSONObject jsonObject = httpPost("https://maas.sutpc.com/api/algorithm/maas/common" ,JsonUtil.object2JsonObject(json));
//////        JSONObject jsonObject = httpPost("https://maas.sutpc.com/api/algorithm/maas/common" ,JSONObject.fromObject(json));
//        System.out.println("response======"+jsonObject);



        //高峰接口
        List<Map<Object,Object>> peak = Lists.newArrayList();
        data.put("curLocation",dest);
        data.put("dirAngle",1);
        data.put("index",0);
        data.put("lineID","lineID001");
        data.put("vehicleID","vehicleID001");
        peak.add(data);
        JSONArray _json = JSONArray.fromObject(peak);
        System.out.println(_json.toString());
        JSONArray peakJson = httpPost("https://maas.sutpc.com/api/algorithm/maas/peak" ,_json);
        System.out.println(peakJson);

    }
}
