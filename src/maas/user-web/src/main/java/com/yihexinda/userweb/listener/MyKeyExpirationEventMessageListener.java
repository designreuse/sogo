package com.yihexinda.userweb.listener;

import com.google.common.collect.Maps;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.userweb.client.OrderClient;
import com.yihexinda.userweb.utils.SpringUtil;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/13 0013
 */
public class MyKeyExpirationEventMessageListener extends KeyExpirationEventMessageListener {

    private OrderClient orderClient = SpringUtil.getBean(OrderClient.class);

    public MyKeyExpirationEventMessageListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //获取过期的key
        String expireKey = new String(message.getBody());
        //获取value
        String value = new String(pattern);
        System.out.println(expireKey);
        //截取订单号
        int size = expireKey.indexOf("&");
        int index = expireKey.indexOf("=");
        int overdueRouteIndex = expireKey.indexOf("^");
        //保存过期订单
        if( size != -1){
            String tradeNo = expireKey.substring(size+1);
            Map<String,Object> condition = Maps.newHashMap();
            condition.put("id",tradeNo);
            condition.put("type","0");//创建订单失效
            orderClient.setOrderExpire(condition);
        }
        if( index != -1){
            String tradeNo = expireKey.substring(index+1);
            Map<String,Object> condition = Maps.newHashMap();
            condition.put("id",tradeNo);
            condition.put("type","1");//订单未在指定时间消费
            orderClient.setOrderExpire(condition);
        }
        if( overdueRouteIndex != -1){
            String overdueRouteString = expireKey.substring(overdueRouteIndex+1);
            String[] split = overdueRouteString.split(":");
            Map<String,Object> condition = Maps.newHashMap();
            condition.put("routeId",split[0]);
            condition.put("currStation",split[1]);//订单未在指定时间消费
            orderClient.setRouteOrderExpire(condition);
//            routeOffPeakClient.setRouteOrderExpire(condition);
        }
    }
}
