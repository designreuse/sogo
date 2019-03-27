package com.yihexinda.platformweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.platformweb.client.DataTestClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Jack
 * @date 2018/10/12.
 */
@Api(description = "测试接口")
@RestController()
@RequestMapping("/api/testApi")
public class TestResource {
    @Autowired
    private DataTestClient dataTestClient;

    @ApiOperation(value = "测试dataTestClient", httpMethod = "GET")
    @RequestMapping("/dataTestClient")
    public List<Object> testDataTestClient() {
        return dataTestClient.getTestList();
    }

//    @ApiOperation(value = "测试dataCompanyClient", httpMethod = "GET")
//    @ApiImplicitParams({
////            @ApiImplicitParam(name = "id", value = "ID", dataType = "Long", paramType = "path", required = true)
//    })
//    @RequestMapping("/testCompanyClient")
//    public String testClient() {
//        return companyClient.addCompanyTest();
//    }


}
