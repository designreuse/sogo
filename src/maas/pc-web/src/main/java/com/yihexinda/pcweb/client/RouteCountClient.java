package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/6
 */
@FeignClient(value = "data-service")
@RequestMapping("/routeCount/client")
public interface RouteCountClient {

    /**
     * pengFeng
     * 查询历史工时段统计列表
     * @param condition map参数
     * @return 历史工时段统计列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getListCount")
    ResultVo getListCount(@RequestBody Map<String,Object> condition);

    /**
     *  当天工时段统计信息
     * @param condition map参数
     * @return 工时段统计
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getRouteStation")
    ResultVo getRouteStation(@RequestBody Map<String,Object> condition);

    /**
     * 查询历史工时段统计列表（导出）
     * @return 历史工时段统计列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getListCountExcel")
    List<Map> getListCountExcel();

    /**
     *  当天工时段统计信息(导出)
     * @return 工时段统计
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getRouteStationExcel")
    List<Map> getRouteStationExcel();

}
