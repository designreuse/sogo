package com.yihexinda.userweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCarDto;
import com.yihexinda.data.dto.TStationDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wenbn
 * @version 1.0
 * @date 2018/12/3 0003
 */
@FeignClient(value = "data-service")
@RequestMapping("/station/client")
public interface StationClient {

    /**
     * 加载站点信息
     * @return
     */
    @PostMapping(value = "/stationList")
    ResultVo loadStations(Map<String,Object> condition);

    /**
     * 加载高峰站点
     * @param data
     * @return
     */
    @PostMapping(value = "/loadPeakLineStations")
    ResultVo loadPeakLineStations(Map<String, Object> data);

    /**
     * 加载高峰途经站点
     * @param
     * @return
     */
    @PostMapping(value = "/loadPeakViaStations")
    ResultVo loadPeakViaStations(HashMap<Object, Object> condition);

    /**
     * 加载高峰站点
     * @param
     * @return
     */
    @PostMapping(value = "/queryStationsByLines")
    ResultVo queryStationsByLines(Map<String, Object> condition);
}
