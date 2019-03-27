package com.yihexinda.pcweb.api;


import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.pcweb.client.RegionClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/08
 */
@Api(description = "系统区域接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class RegionResource {

    @Autowired
    private RegionClient regionClient;

    /**
     * 查询区域列表信息
     * @return 区域列表
     */
    @ApiOperation(value = "查询区域列表", httpMethod = "POST")
    @RequestMapping(value = "/getRegionList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getRegionList(@RequestBody Map<String,Object> data) {
       if (data==null){
       }
        return regionClient.regionList();
    }


}
