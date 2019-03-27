package com.yihexinda.userweb.scheduler;

import com.yihexinda.userweb.client.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/21 0021
 */
@Component
@Slf4j
@EnableScheduling
public class UserScheduler {

    @Autowired
    private OrderClient orderClient;


    /**
     * 系统自动取消实时出行订单，每隔30秒执行一次计算
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void sysCancalOrder() throws Exception {
        log.info("start Scheduled scheduled!");
        orderClient.sysCancalOrder();
    }
}
