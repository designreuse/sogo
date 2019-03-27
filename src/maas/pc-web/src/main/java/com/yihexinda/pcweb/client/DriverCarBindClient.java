package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverCarBindDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/7
 */
@FeignClient(value = "data-service")
@RequestMapping("/driverCarBind/client")
public interface DriverCarBindClient {

    /**
     * pengFeng
     * 查询历史车辆绑定列表
     * @param condition map参数
     * @return 历史车辆绑定列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getDriverCarBind")
    ResultVo getDriverCarBind(@RequestBody Map<String, Object> condition);


    /**
     * pengFeng
     * 查询历史车辆绑定列表(导出)
     * @param driverId id
     * @return 历史车辆绑定列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getDriverCarBindExcel/{driverId}")
    List<TDriverCarBindDto> getDriverCarBindExcel(@PathVariable("driverId") String driverId);
}
