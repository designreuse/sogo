package com.yihexinda.bussweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverCarBindDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author emmet
 * @date 2018/11/28.
 */

@FeignClient(value = "data-service")
@RequestMapping("/driverCarBind/client")
public interface DriverCarBindClient {

    /**
     *
     * 添加绑定信息
     *
     * @param tDriverCarBindDto 绑定信息
     * @return ResultVo
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    ResultVo add(@RequestBody TDriverCarBindDto tDriverCarBindDto);


    /**
     * 司机解绑车辆
     *
     * @param tDriverCarBindDto 解绑信息
     * @return ResultVo
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    ResultVo update(@RequestBody TDriverCarBindDto tDriverCarBindDto);

    /**
     * 获取司机登陆的时候所有绑定与未绑定车辆
     *
     * @return
     */

    @GetMapping(value = "/getCarAndBindStatusList")
    ResultVo getCarAndBindStatusList();

    /**
     * 司机端查询当前绑定的车辆
     *
     * @param condition token
     * @return ResultVo
     */
    @PostMapping(value = "/getBindCar")
    ResultVo getBindCar(@RequestBody Map<String, Object> condition);




    /**
     * emmet
     * 司机添加绑定车辆
     *
     * @param condition 绑定信息
     * @return ResultVo
     */
    @RequestMapping(value = "/addDriver", method = RequestMethod.POST)
    ResultVo addDriver(@RequestBody Map<String, Object> condition);


    /**
     * 司机解绑车辆
     *
     * @param condition 解绑信息
     * @return ResultVo
     */
    @RequestMapping(value = "/updateDriver", method = RequestMethod.POST)
    ResultVo updateDriver(@RequestBody Map<String, Object> condition);

}
