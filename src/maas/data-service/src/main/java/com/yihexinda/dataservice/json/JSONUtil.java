package com.yihexinda.dataservice.json;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/6 0006
 */
import java.util.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yihexinda.core.utils.JsonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
/**
 * JSON格式转换类
 * @author Administrator
 *
 */
public class JSONUtil {
    /**
     * 将一个对象直接转换为一个JSONObject对象，
     * 同样适合于JSON格式的字符串
     * 但是如果存在java.sql.Date或者java.sql.Timestamp时间格式，调用例外一个toJson转换方法
     * @param obj
     * @return
     */
    public static JSONObject toJson(Object obj) {
        return JSONObject.fromObject(obj);
    }

    /**
     *
     * @param obj 需要转换的参数
     * @param processors 类型转换器的集合,参数是一个Map集合，键代表需要转换类型的全路径，值是类型转换器
     * @return
     * @throws ClassNotFoundException
     */
    public static JSONObject toJson(Object obj,Map<String,JsonValueProcessor> processors) throws ClassNotFoundException{
        //定义一个JSONConfig对象，该对象可以制定一个转换规则
        JsonConfig config = new JsonConfig();
        if(processors != null && !processors.isEmpty()){
            Iterator<java.util.Map.Entry<String, JsonValueProcessor>> it = processors.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<java.lang.String, net.sf.json.processors.JsonValueProcessor> entry = (Map.Entry<java.lang.String, net.sf.json.processors.JsonValueProcessor>) it
                        .next();
                String key = entry.getKey();
                JsonValueProcessor processor = processors.get(key);
                //反射获取到需要转换的类型
                Class<?> cls = Class.forName(key);
                config.registerJsonValueProcessor(cls, processor);
            }
        }
        return JSONObject.fromObject(obj, config);
    }


    public static JSONArray toJsonArray(Object obj) throws ClassNotFoundException{
        Map<String, JsonValueProcessor> processors = new HashMap<String, JsonValueProcessor>();
        processors.put("java.util.Date", new UtilDateProcessor("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        processors.put("java.sql.Date", new UtilDateProcessor("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        //定义一个JSONConfig对象，该对象可以制定一个转换规则
        JsonConfig config = new JsonConfig();
        if(processors != null && !processors.isEmpty()){
            Iterator<java.util.Map.Entry<String, JsonValueProcessor>> it = processors.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<java.lang.String, net.sf.json.processors.JsonValueProcessor> entry = (Map.Entry<java.lang.String, net.sf.json.processors.JsonValueProcessor>) it
                        .next();
                String key = entry.getKey();
                JsonValueProcessor processor = processors.get(key);
                //反射获取到需要转换的类型
                Class<?> cls = Class.forName(key);
                config.registerJsonValueProcessor(cls, processor);
            }
        }
        return JSONArray.fromObject(obj, config);
    }

    public static void main(String[] args) throws ClassNotFoundException {

        String jsonString = "[{'Value':'0','Name':'测试0','statusInfo':{'Status':'0'}},{'Value':'1','Name':'测试1','statusInfo':{'Status':'10'}},{'Value':'2','Name':'测试2','statusInfo':{'Status':'20'}},{'Value':'3','Name':'测试3','statusInfo':{'Status':'30'}},{'Value':'4','Name':'测试4','statusInfo':{'Status':'40'}},{'Value':'5','Name':'测试5','statusInfo':{'Status':'50'}},{'Value':'6','Name':'测试6','statusInfo':{'Status':'60'}},{'Value':'7','Name':'测试7','statusInfo':{'Status':'70'}},{'Value':'8','Name':'测试8','statusInfo':{'Status':'80'}},{'Value':'9','Name':'测试9','statusInfo':{'Status':'90'}}]";
        JSONArray jsonString1 = JSONArray.fromObject(jsonString);
        System.out.println(jsonString1);

        // 定义一个类型转化器集合，键是需要转换的类型全路径，值是用于转换的类型转换器
        Map<String, JsonValueProcessor> processors = new HashMap<String, JsonValueProcessor>();
        processors.put("java.util.Date", new UtilDateProcessor("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        Map<String,Object> data = Maps.newHashMap();
        Map<String,Object> nowtime = Maps.newHashMap();
        List<Map<String,Object>> list = Lists.newArrayList();
        data.put("dest","dest");
        data.put("orderID","orderID");
        data.put("ordertime",new Date());
        list.add(data);
        data.put("dest1","dest");
        data.put("orderID1","orderID");
        data.put("ordertime1",new Date());
        list.add(data);
        nowtime.put("completeOrder",list);
        JSONObject json = JSONUtil.toJson(nowtime, processors);


        String jsonON = "{\n\\'vehicleRouteList\\':[\n{\n\\'vehicleID\\': \'车辆1ID\'," +
                "\n\'stationList\': [\n\'站点3ID\',\n\'站点1ID\',\n\'站点2ID\'\n],\n           \'linkMap\': {\n \'站点3ID，站点1ID\': [\n\'link1ID\',\n\'link2ID\',\n" +
                "\'link3ID\'\n                ],\n                \'站点1ID，站点2ID\': [\n                  \'link4ID\',\n \'link5ID\',\n \'link3ID\'\n  " +
                "              ]\n            }\n        },\n        {\n \'vehicleID\': \'车辆2ID\',\n         \'stationList\': [\n   \'站点3ID\',\n  \'站点1ID\',\n " +
                "\'站点2ID\'\n ],\n\'linkMap\': {\n\'站点3ID，站点1ID\': [\n\'link1ID\\',\n \'link2ID\',\n \\'link3ID\'\n                ],\n              " +
                "  \'站点1ID，站点2ID\': [\n                  \'link4ID\',\n   \'link5ID\',\n       \'link3ID\'\n                ]\n            }\n       " +
                " },\n        {\n \'vehicleID\': \'车辆3ID\',\n            \'stationList\': [\n\'站点3ID\',\n\'站点1ID\',\n" +
                " \'站点2ID\'\n            ],\n            \'linkMap\': {\n                \'站点3ID，站点1ID\': [\n              " +
                "      \'link1ID\',\n                    \'link2ID\',\n                    \'link3ID\'\n                " +
                "],\n                \'站点1ID，站点2ID\': [\n                    \'link4ID\',\n                   " +
                " \'link5ID\',\n                    \'link3ID\'\n                ]\n            }\n        }\n    ],\n    \'userVehicleList\': " +
                "[\n        {\n            \'userID\': \'乘客1ID\',\n            \'vehicleID\': \'车辆1ID\'\n        },\n       " +
                " {\n            \'userID\': \'乘客2ID\',\n            \'vehicleID\': \'车辆2ID\'\n        },\n        " +
                "{\n            \'userID\': \'乘客3ID\',\n            \'vehicleID\': \'车辆3ID\'\n        }\n    ]\n}" +
                "'";

        JSONObject.fromObject(jsonON);
        Map<Object, Object> objectObjectMap = JsonUtil.parseJSON2Map(jsonON);
        System.out.println("======"+objectObjectMap);
        System.out.println(json.toString());



    }
}
