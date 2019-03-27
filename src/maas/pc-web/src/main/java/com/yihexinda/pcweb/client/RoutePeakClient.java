package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TRoutePeakDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/11/30 0030
 */
@FeignClient(value = "data-service")
@RequestMapping("/routePeak/client")
public interface RoutePeakClient {

    /**
     * wenbn
     * 添加高峰行程
     * @param routePeak
     * @return
     */
    @RequestMapping(value = "/addRoutePeak" ,method = RequestMethod.POST)
    ResultVo addRoutePeak(TRoutePeakDto routePeak);

    /**
     * wenbn
     * 删除高峰行程
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteRoutePeak/{id}" ,method = RequestMethod.POST)
    ResultVo deleteRoutePeak(@PathVariable("id") String id);

    /**
     * wenbn
     * 修改高峰行程
     * @param routePeak
     * @return
     */
    @RequestMapping(value = "/updateRoutePeak" ,method = RequestMethod.POST)
    ResultVo updateRoutePeak(TRoutePeakDto routePeak);

    /**
     * wenbn
     * 查询高峰行程详情
     * @param id 高峰行程id
     * @return  ResultVo
     */
    @GetMapping(value = "/getRoutePeakId/{id}")
    ResultVo getRoutePeakId(@PathVariable("id")String id);


    /**
     * wenbn
     * 查询高峰行程
     * @param
     * @return
     */
    @PostMapping(value = "/routePeakList")
    ResultVo routePeakList(Map<String,Object> condition);

    /**
     * 高峰线路设置司机
     * @param data map
     * @return ResultVo
     */
    @PostMapping(value = "/distribution")
    ResultVo distribution(Map<String,Object> data);

    /**

     * 查询高峰行程(导出)
     * @return  ResultVo
     */
    @GetMapping(value = "/getRoutePeakListExcel")
    List<TRoutePeakDto> getRoutePeakListExcel();
}
