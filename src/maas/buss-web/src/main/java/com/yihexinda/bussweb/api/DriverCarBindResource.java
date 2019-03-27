package com.yihexinda.bussweb.api;

import com.yihexinda.bussweb.client.DriverCarBindClient;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverCarBindDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author pengfeng
 * @version 1.0
 * @date 2018/12/3 0029
 */
@Api(description = "司机绑定车辆")
@RestController
@RequestMapping("/api/buss")
@Slf4j
public class DriverCarBindResource {

    @Autowired
    private DriverCarBindClient driverCarBindClient;


    /**
     * pengfeng
     * 添加绑定车辆信息
     *
     * @param data 绑定信息
     * @return ResultVo
     */
    @ApiOperation(value = "添加绑定车辆信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "String"),
            @ApiImplicitParam(name = "carId", value = "车辆id", required = true, paramType = "String"),
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultVo add(@RequestBody Map<String,Object> data) {
        if ("".equals(StringUtil.trim(data.get("carId")))||"".equals(StringUtil.trim(data.get("token")))){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,"参数缺少，请检查相应参数");
        }
        //解析token
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
        if (null==payLoadVo){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析异常");
        }
        String driverId = payLoadVo.getUserId();
        if (StringUtil.isEmpty(driverId)) {
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析异常,用户为空");
        }

        TDriverCarBindDto tDriverCarBindDto = new TDriverCarBindDto();
        tDriverCarBindDto.setBindStatus("1");
        tDriverCarBindDto.setDriverId(driverId);
        tDriverCarBindDto.setCarId(StringUtil.trim(data.get("carId")));
        return driverCarBindClient.add(tDriverCarBindDto);
    }


    /**
     * pengfeng
     * 解绑车辆
     *
     * @param data 解绑信息
     * @return ResultVo
     */
    @ApiOperation(value = "车辆解绑", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "String")
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultVo update(@RequestBody Map<String,Object> data) {
        if ("".equals(StringUtil.trim(data.get("token")))){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,"参数缺少，请检查相应参数");
        }
        //解析token
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
        if (null==payLoadVo){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析异常");
        }
        String driverId = payLoadVo.getUserId();
        if (StringUtil.isEmpty(driverId)) {
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析异常,用户为空");
        }
        TDriverCarBindDto tDriverCarBindDto = new TDriverCarBindDto();
        tDriverCarBindDto.setBindStatus("2");
        tDriverCarBindDto.setDriverId(driverId);
        return driverCarBindClient.update(tDriverCarBindDto);
    }

    /**
     * 获取司机登陆的时候所有绑定与未绑定车辆
     *
     * @return
     */
    @ApiOperation(value = "获取绑定与未绑定车辆", httpMethod = "GET")
    @GetMapping(value = "/getCarAndBindStatusList")
    public ResultVo getCarAndBindStatusList() {
        return driverCarBindClient.getCarAndBindStatusList();
    }

    /**
     * 司机端查询当前绑定的车辆列
     *
     * @param condition token
     * @return 绑定的车辆信息
     */
    @ApiOperation(value = "查询司机绑定的车俩", httpMethod = "POST")
    @PostMapping(value = "/getBindCar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "String")
    })
    public ResultVo getBindCar(@RequestBody Map<String, Object> condition) {
        if ("".equals(StringUtil.trim(condition.get("token")))){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token为空");
        }
        //解析token
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(condition.get("token")));
        if (null==payLoadVo){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析异常");
        }
        String driverId = payLoadVo.getUserId();
        if (StringUtil.isEmpty(driverId)) {
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析异常,用户为空");
        }
        condition.put("driverId", driverId);
        return driverCarBindClient.getBindCar(condition);
    }


    /**
     * 司机添加绑定车辆信息
     *
     * @param condition 绑定信息
     * @return ResultVo
     */
    @ApiOperation(value = "司机添加绑定车辆信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "String"),
            @ApiImplicitParam(name = "carId", value = "车辆id", required = true, paramType = "String"),

    })
    @RequestMapping(value = "/addDriver", method = RequestMethod.POST)
    public ResultVo addDriver(@RequestBody Map<String, Object> condition) {
        //解析token
        String driverId = RequestUtil.analysisToken(StringUtil.trim(condition.get("token"))).getUserId();
        if (driverId == null) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        condition.put("driverId", driverId);
        return driverCarBindClient.addDriver(condition);
    }


    /**
     * 司机车辆解绑
     *
     * @param condition 解绑信息
     * @return ResultVo
     */
    @ApiOperation(value = "司机车辆解绑", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "String"),
    })
    @RequestMapping(value = "/updateDriver", method = RequestMethod.POST)
    public ResultVo updateDriver(@RequestBody Map<String, Object> condition) {
        //解析token
        String driverId = RequestUtil.analysisToken(StringUtil.trim(condition.get("token"))).getUserId();
        if (driverId == null) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        condition.put("driverId", driverId);
        return driverCarBindClient.updateDriver(condition);
    }




}
