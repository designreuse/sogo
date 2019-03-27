package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/driver/client")
public interface DriverClient {

    /**
     * 查询司机信息列表
     * @param  condition 参数map
     * @return 司机信息列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getDriverList")
    ResultVo getDriverList(@RequestBody Map<String,Object> condition);

    /**
     * 根据司机id获取信息
     * @param id 司机id
     * @return  司机信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getDriverInfo/{id}")
    ResultVo getDriverInfo(@PathVariable("id") String id);


    /**
     * 根据司机id获取车辆历史列表
     * @param driverId 司机id
     * @return  司机历史车辆列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getDriverBindCarList/{driverId}")
    ResultVo getDriverBindCarList(@PathVariable("driverId") String driverId);

    /**
     *  新增司机信息
     * @param tDriverDto 司机dto
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addDriverInfo")
    ResultVo addDriverInfo(@RequestBody TDriverDto tDriverDto);

    /**
     *  修改司机信息
     * @param tDriverDto 司机信息
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updateDriverInfo")
    ResultVo updateDriverInfo(@RequestBody TDriverDto tDriverDto);

    /**
     *  司机批量排班
     * @param driverDtoList 司机列表
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.POST, value = "/batchScheduling")
    ResultVo batchScheduling(@RequestBody Map<String, Object> driverDtoList);

    /**
     * 查询司机信息列表
     * @return 司机信息列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getDriverExcel")
    List<TDriverDto> getDriverExcel();

    /**
     *  修改司机状态
     * @param tDriverDto 司机信息
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updateDriverStatus")
    ResultVo updateDriverStatus(@RequestBody TDriverDto tDriverDto);


    /**
     * 查询司机信息列表（状态为1，用于批量）
     * @param  condition 参数map
     * @return 司机信息列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getDriverListStatus")
    ResultVo getDriverListStatus(@RequestBody Map<String,Object> condition);
}
