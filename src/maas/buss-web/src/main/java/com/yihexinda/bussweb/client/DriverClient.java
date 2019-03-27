package com.yihexinda.bussweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCarDto;
import com.yihexinda.data.dto.TDriverDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yhs
 * @date 2018/11/28.
 */

@FeignClient(value = "data-service")
@RequestMapping("/driver/client")
public interface DriverClient {

    /**
     * ysh
     * 添加司机信息
     *
     * @param driver
     * @return
     */
    @RequestMapping(value = "/addDriver", method = RequestMethod.POST)
    ResultVo addDriver(@RequestBody TDriverDto driver);

    /**
     * emmet
     * 删除司机信息
     *
     * @param driverId
     * @return
     */
    @RequestMapping(value = "/deleteDriver", method = RequestMethod.POST)
    ResultVo deleteDriver(@RequestParam(value ="driverId" ) String driverId);

    /**
     * ysh
     * 修改司机信息
     *
     * @param driver
     * @return
     */
    @RequestMapping(value = "/updateDriver", method = RequestMethod.POST)
    ResultVo updateDriver(@RequestBody TDriverDto driver);

    /**
     * 修改司机信息
     * @param tDriverDto 司机信息
     * @return ResultVo
     */
    @RequestMapping(value = "/updateDriverInfo", method = RequestMethod.PUT)
    ResultVo updateDriverInfo(@RequestBody TDriverDto tDriverDto);

    /**
     * emmet
     * 查询司机信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getDriverList", method = RequestMethod.GET)
    ResultVo driverList();

    /**
     * emmet
     * 司机登录
     *
     *
     * @param condition
     */
    @RequestMapping(value = "/loginDriver", method = RequestMethod.POST)
    ResultVo loginDriver(@RequestBody Map<String,Object> condition);


   /* *//**
     * emmet
     * 短信发送
     *
     * @param telephone
     * @return
     *//*
    @RequestMapping(value = "/sendMessageDriver/{telephone}", method = RequestMethod.GET)
    ResultVo sendMessageDriver(@PathVariable(value = "telephone") String telephone);*/

    /**
     * ysh
     * 发送短信
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/addSmsLog", method = RequestMethod.POST)
    ResultVo addSmsLog(@RequestParam(value = "phone") String phone);

    /**
     * emmet
     * 设置密码
     *
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/setPasswordDriver", method = RequestMethod.POST)
    ResultVo setPasswordDriver(@RequestBody Map<String, Object> params);

    
    /**
     * 显示车辆列表
     *
     * @return
     */
    @RequestMapping(value = "/showCarList", method = RequestMethod.GET)
    ResultVo showCarList();

    /**
     * 查询司机信息
     * @param id 司机id
     * @return 司机详情
     */
    @RequestMapping(value = "/getDriver/{id}", method = RequestMethod.GET)
    ResultVo getDriver(@PathVariable("id") String id);

    /**
     * 查询司机是否处于上班状态
     * @param driverId 司机id
     * @return ResultVo
     */
    @RequestMapping(value = "/indexDriver/{driverId}", method = RequestMethod.GET)
    ResultVo indexDriver(@PathVariable("driverId") String driverId);

    /**
     * 退出登录
     * @param driverId 司机id
     * @return ResultVo
     */
    @RequestMapping(value = "/loginOutDriver/{driverId}", method = RequestMethod.GET)
    ResultVo loginOutDriver(@PathVariable("driverId") String driverId);


    /**
     * 司机退出登录
     * @author chenzeqi
     * @param data
     * @return
     */
    @PostMapping("driverLogout")
    ResultVo driverLogout(@RequestBody Map<String,Object> data);

    /**
     * 司机是否设置休息
     * @author wenbn
     * @param data
     * @return
     */
    @PostMapping("checkDriverStatus")
    ResultVo checkDriverStatus(@RequestBody Map<String, Object> data);

    /**
     * 系统自动完成行程
     * @author wenbn
     * @return
     */
    @PostMapping("resetRoute")
    ResultVo resetRoute(@RequestBody Map<String, Object> data);

    /**
     * 检查手机号是否存在
     * @param phone
     * @return
     */
    @PostMapping("checkExistsPhone")
    ResultVo checkExistsPhone(@RequestParam(value ="phone" ) String phone);

    /**
     * 开启早高峰
     * @param
     * @return
     */
    @PostMapping("openMorningPeak")
    ResultVo openMorningPeak();

    /**
     * 开启晚高峰
     * @param
     * @return
     */
    @PostMapping("openEveningPeak")
    ResultVo openEveningPeak();


}
