package com.yihexinda.bussweb.scheduler;

import com.google.common.collect.Maps;
import com.yihexinda.bussweb.client.DriverClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/21 0021
 */
@Component
@Slf4j
@EnableScheduling
public class BussScheduler {

    @Autowired
    private DriverClient driverClient;

    /**
     * 开启早高峰
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void openMorningPeak() throws Exception {
        log.info("7点系统自动开启早高峰...start");
        driverClient.openMorningPeak();
        log.info("7点系统自动开启早高峰...start");
    }

    /**
     * 开启晚高峰高峰
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void openEveningPeak() throws Exception {
        log.info("11点系统自动开启晚高峰...start");
        driverClient.openEveningPeak();
        log.info("11点系统自动开启晚高峰...start");
    }


    /**
     * 系统自动完成高峰行程
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void resetRoute12() throws Exception {
        log.info("12系统自动完成高峰行程操作....start");
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("type","1");
        driverClient.resetRoute(map);
        log.info("12系统自动完成高峰行程操作....end");
    }


    /**
     * 系统自动完成高峰行程
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void resetRoute23() throws Exception {
        log.info("23系统自动完成高峰行程操作....start");
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("type","1");
        driverClient.resetRoute(map);
        log.info("23系统自动完成高峰行程操作....end");
    }


    /**
     * 系统自动完成行程
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void sysCancalOrder() throws Exception {
        log.info("开始执行行程操作....start");
        driverClient.resetRoute(null);
        log.info("开始执行行程操作....end");
    }
}
