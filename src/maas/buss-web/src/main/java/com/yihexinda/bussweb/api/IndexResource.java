package com.yihexinda.bussweb.api;

import com.yihexinda.bussweb.client.DriverClient;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Administrator
 * @date 2018/12/21 0021 14:26
 */
@Api(description = "首页接口")
@RestController
@RequestMapping("/api/buss")
@Slf4j
public class IndexResource {

    @Resource
    private DriverClient driverClient;

    /**
     * 进入首页时，查询该司机是否上班状态
     * @param data  token参数
     * @return ResultVo
     */
    @ApiOperation(value = "查询司机是否上班", httpMethod = "POST")
    @RequestMapping(value = "/indexDriver", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, dataType = "String")
    })
    public ResultVo indexDriver(@RequestBody Map<String, Object> data) {
        String token = StringUtil.trim(data.get("token"));
        if ("".equals(token)){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token为空");
        }
        PayLoadVo payLoadVo= RequestUtil.analysisToken(token);
        if (null!=payLoadVo){
            if (StringUtil.isEmpty(payLoadVo.getUserId())){
                return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析失败，司机id为空");
            }
            return   driverClient.indexDriver(payLoadVo.getUserId());
        }
        return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析失败，请核对token参数");
    }


}
