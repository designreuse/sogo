package com.yihexinda.bussweb.api;

import com.yihexinda.bussweb.client.SystemLogClient;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.vo.ResultVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2019/3/13 0013
 */
@RestController
@RequestMapping("/api/buss/syslog")
public class SystemLogResource {

    @Resource
    private SystemLogClient systemLogClient;

    /**
     * 保存日志
     * @param condition
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, dataType = "String")
    })
    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    @NoRequireLogin
    ResultVo save(@RequestBody Map<String,Object> condition){
       return systemLogClient.save(condition);
    }

}
