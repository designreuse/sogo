package com.yihexinda.nodeweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.data.dto.OrderDto;
import com.yihexinda.data.enums.OrderTypeEnum;
import com.yihexinda.nodeweb.client.DataTestClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Jack
 * @date 2018/10/12.
 */
@Api(description = "测试接口")
@RestController()
@RequestMapping("/api/testApi")
public class TestResource {
    @Autowired
    private DataTestClient dataTestClient;
    @Autowired
    private KafkaTemplate<String, OrderDto> kafkaTemplate;

    @ApiOperation(value = "测试dataTestClient", httpMethod = "GET")
    @RequestMapping("/dataTestClient")
    public List<Object> testDataTestClient() {
        return dataTestClient.getTestList();
    }

    @ApiOperation(value = "测试kafka", httpMethod = "GET")
    @RequestMapping("/testKafka")
    public Object testKafka() {
        OrderDto order = new OrderDto();
        order.setId("123");
        order.setOrderType(OrderTypeEnum.出票);
        kafkaTemplate.send("order", order);
//        kafkaTemplate.send(KAFKA_TOPIC_GRABBING_ORDER_CONFIRM_OVERTIME_KEY_PREFIX + order.getOrderType().getCode(), order);
        return "ok";
    }

//    @KafkaListener(topics = "topic1")
//    public void receiveTopic1(ConsumerRecord<String, OrderDto> consumerRecord) {
//        System.out.println("Receiver on topic1: " + consumerRecord.value());
//    }

    @GetMapping("/testClient")
    public String testClient() {
        return "ok";
    }

    @GetMapping("/test1")
    public Object test1() {
        return dataTestClient.testApi();
    }
}
