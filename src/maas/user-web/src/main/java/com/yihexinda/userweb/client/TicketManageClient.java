package com.yihexinda.userweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCarDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/1 0001
 */
@FeignClient(value = "data-service")
@RequestMapping("/ticket/client")
public interface TicketManageClient {

    /**
     * 检查预约数据
     * @return
     */
    @PostMapping(value = "/checkAppointment")
    ResultVo checkAppointment(Map<String,Object> condition);

    /**
     * 检测是否还有未出行的票
     * @param condition
     * @return
     */
    @PostMapping(value = "/checkExitsNotTravelPeak")
    ResultVo checkExitsNotTravelPeak(Map<String,Object> condition);
}
