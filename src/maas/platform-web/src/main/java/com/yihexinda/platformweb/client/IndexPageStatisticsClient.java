package com.yihexinda.platformweb.client;


import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yihexinda.data.dto.UserOnlineDto;

/**
 * Created by wxf on 2018/11/23.
 */

@FeignClient(value = "data-service")
@RequestMapping("/IndexPageStatistics")
public interface IndexPageStatisticsClient {
    /**
     * 用户在线统计
     * @return
     */
    @RequestMapping("/userOnline")
    List<UserOnlineDto> UserOnline();
}
