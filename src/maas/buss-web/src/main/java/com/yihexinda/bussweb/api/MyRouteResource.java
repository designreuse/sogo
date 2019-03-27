package com.yihexinda.bussweb.api;

import com.yihexinda.bussweb.client.MyRouteClient;
import com.yihexinda.bussweb.client.RouteOffpeakClient;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 *
 * @author chenzeqi
 * @date 2018/12/24
 */
@Api(description = "我的行程接口")
@RestController
@RequestMapping("/api/buss")
@Slf4j
public class MyRouteResource {


    @Autowired
    private MyRouteClient myRouteClient;

    /**
     * @author chenzeqi
     * 查询我的行程列表
     * @type 1请求平峰列表 2 请求高峰列表
     * @deviceId 司机id
     * @param
     * @return
     */
    @ApiOperation(value = "查询我的行程列表", httpMethod = "POST")
    @PostMapping(value = "/getMyRouteList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")
    })
    public ResultVo getMyRouteList(@RequestBody Map<String,Object> condition) {
        String token = StringUtil.trim(condition.get("token"));
        if ("".equals(token)){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token为空");
        }
        PayLoadVo payLoadVo= RequestUtil.analysisToken(token);
        if (null!=payLoadVo) {
            if (StringUtil.isEmpty(payLoadVo.getUserId())) {
                return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, "token解析失败，司机id为空");
            }
        }
        condition.put("driverId",payLoadVo.getUserId());
        return myRouteClient.getMyRouteList(condition);
    }

    /**
     * @author chenzeqi
     * 查询我的行程详情
     * @type 1请求平峰列表 2 请求高峰列表
     * @deviceId 司机id
     * @routeId  行程id
     * @param
     * @return
     */
    @ApiOperation(value = "查询我的行程详情", httpMethod = "POST")
    @PostMapping(value = "/getMyRouteDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(name = "routeId", value = "行程ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "行程类型(0平峰 1高峰)", required = true, dataType = "String"),
    })
    public ResultVo getMyRouteDetails(@RequestBody Map<String,Object> condition) {
        return myRouteClient.getMyRouteDetails(condition);
    }


    /**
     * 查询我的行程列表
     * @type 1检查是否有行程 2 可以派单
     * @deviceId 司机id
     * @param
     * @return
     */
    @ApiOperation(value = "查询我的行程列表", httpMethod = "POST")
    @PostMapping(value = "/getRouteInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "type", required = true, dataType = "String"),
    })
    public ResultVo getRouteInfo(@RequestBody Map<String,Object> data) {
        String token = StringUtil.trim(data.get("token"));
        if (StringUtil.isEmpty(token)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token为空");
        }
        data.put("driverId",RequestUtil.analysisToken(token).getUserId());
        return myRouteClient.getRouteInfo(data);
    }

    /**
     * 检查行程是否结束
     * @type type 0 高峰 1平峰
     * @routeId 行程ID
     * @param
     * @return
     */
    @ApiOperation(value = "检查行程是否结束", httpMethod = "POST")
    @PostMapping(value = "/checkFinish")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token令牌", required = true, paramType = "String"),
            @ApiImplicitParam(name = "routeId", value = "行程ID", required = true, paramType = "String"),
            @ApiImplicitParam(name = "type", value = "0 高峰 1平峰", required = true, paramType = "String")
    })
    public ResultVo checkFinish(@ApiIgnore @RequestBody Map<String,Object> data) {
        String routeId = StringUtil.trim(data.get("routeId"));
        String type = StringUtil.trim(data.get("type"));
        if(StringUtil.isEmpty(routeId) || StringUtil.isEmpty(type)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        return myRouteClient.checkFinish(data);
    }


    /**
     * 结束行程
     * @type type 0 高峰 1平峰
     * @routeId 行程ID
     * @param
     * @return
     */
    @ApiOperation(value = "结束行程", httpMethod = "POST")
    @PostMapping(value = "/finishRoute")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token令牌", required = true, paramType = "String"),
            @ApiImplicitParam(name = "routeId", value = "行程ID", required = true, paramType = "String"),
            @ApiImplicitParam(name = "type", value = "0 高峰 1平峰", required = true, paramType = "String")
    })
    public ResultVo finishRoute(@ApiIgnore @RequestBody Map<String,Object> data) {
        String routeId = StringUtil.trim(data.get("routeId"));
        String type = StringUtil.trim(data.get("type"));
        if(StringUtil.isEmpty(routeId) || StringUtil.isEmpty(type)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        return myRouteClient.finishRoute(data);
    }



}
