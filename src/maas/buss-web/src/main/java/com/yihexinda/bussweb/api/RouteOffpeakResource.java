package com.yihexinda.bussweb.api;

import com.yihexinda.bussweb.client.RouteOffpeakClient;
import com.yihexinda.bussweb.client.RoutePeakClient;
import com.yihexinda.core.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *
 * @author wenbn
 * @version 1.0
 * @date 2018/12/1 0001
 */
@Api(description = "我的平峰行程接口")
@RestController
@RequestMapping("/api/buss")
@Slf4j
public class RouteOffpeakResource {

    @Autowired
    private RouteOffpeakClient routeOffpeakClient;

    /**
     * wenbn
     * 查询我的平峰行程列表
     * @param
     * @return
     */
    @ApiOperation(value = "查询我的平峰行程列表", httpMethod = "POST")
    @PostMapping(value = "/routeOffpeakList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token令牌", required = true, paramType = "String"),
            @ApiImplicitParam(name = "routeId", value = "行程ID", required = true, paramType = "String"),
            @ApiImplicitParam(name = "type", value = "0 高峰 1平峰", required = true, paramType = "String"),
            @ApiImplicitParam(name = "pageIndex", value = "当前页", required = true, paramType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, paramType = "String")
    })
    public ResultVo routeOffpeakList(@RequestBody Map<String,Object> condition) {
        return routeOffpeakClient.bussRouteOffpeakList(condition);
    }

}
