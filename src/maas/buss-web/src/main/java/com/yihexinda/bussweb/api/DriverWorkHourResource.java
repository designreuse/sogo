package com.yihexinda.bussweb.api;


import com.yihexinda.bussweb.client.DriverWorkHourClient;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
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
 * @author ysh
 * @version 1.0
 * @date 2018/11/29 0029
 */
@Api(description = "司机工时接口")
@RestController
@RequestMapping("/api/buss")
@Slf4j
public class DriverWorkHourResource {

    @Autowired
    private DriverWorkHourClient driverWorkHourClient;

    /**
     * 根据司机Id查询司机工时
     *
     * @param paramter
     * @return
     */

    @ApiOperation(value = "根据司机Id查询司机工时", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "String")
    })
    @PostMapping(value = "/getDriverWorkHour")
    public ResultVo getDriverWorkHour(@RequestBody Map<String, String> paramter) {
        String token = StringUtil.trim(paramter.get("token"));
        String driverId = RequestUtil.analysisToken(token).getUserId();
        if (driverId==null) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        paramter.put("driverId",driverId);
        return driverWorkHourClient.getDriverWorkHour(paramter);
    }


}
