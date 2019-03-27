package com.yihexinda.userweb.api;


import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TTravelEvaluateDto;
import com.yihexinda.userweb.client.TravelEvaluateClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 行程评价管理
 *
 * @author yhs
 * @version 1.0
 * @date 2018/12/4 0003
 */
@Api(description = "行程评价接口")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class TravelEvaluateResource {

    @Autowired
    private TravelEvaluateClient travelEvaluateClient;


    /**
     * 添加行程评价
     *
     * @param condition
     * @return
     */

    @ApiOperation(value = "添加行程评价", httpMethod = "POST")
    @PostMapping(value = "/saveTravelEvaluate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeId", value = "行程id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", value = "行程内容评价", required = true, dataType = "String"),
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, dataType = "String"),
            @ApiImplicitParam(name = "startNo", value = "司机服务", required = true, dataType = "String"),
            @ApiImplicitParam(name = "carEnvStartNo", value = "车内环境", required = true, dataType = "String"),
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String")


    })
    public ResultVo saveTravelEvaluate(@RequestBody Map<String, Object> condition) {

        return travelEvaluateClient.saveTravelEvaluate(condition);
    }

    /**
     * 删除行程评价
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除行程评价", httpMethod = "GET")
    @RequestMapping(value = "/deleteTravelEvaluate/{id}", method = RequestMethod.GET)
    public ResultVo deleteTravelEvaluate(@PathVariable(value = "id") String id) {
        return travelEvaluateClient.deleteTravelEvaluate(id);
    }

    /**
     * 修改行程评价
     *
     * @param travelEvaluateDto
     * @return
     */

    @ApiOperation(value = "修改行程评价", httpMethod = "POST")
    @PostMapping(value = "/updateTravelEvaluate")
    public ResultVo updateTravelEvaluate(@RequestBody TTravelEvaluateDto travelEvaluateDto) {
        return travelEvaluateClient.updateTravelEvaluate(travelEvaluateDto);
    }


    /**
     * 查询用户行程评价
     *
     * @param userId 用户id
     * @return 行程列表
     */

    @ApiOperation(value = "查询用户行程评价", httpMethod = "GET")
    @GetMapping("/getTravelEvaluateList/{userId}")
    @NoRequireLogin
    public ResultVo getTravelEvaluateList(@PathVariable(value = "userId") String userId) {
        return travelEvaluateClient.getTravelEvaluateList(userId);
    }


    /**
     * 行程管理列表
     */
    @ApiOperation(value = "行程管理列表", httpMethod = "GET")
    @GetMapping("/getTravelEvaluateManager")
    public ResultVo getTravelEvaluateManager() {
        return travelEvaluateClient.getTravelEvaluateManager();
    }

}
