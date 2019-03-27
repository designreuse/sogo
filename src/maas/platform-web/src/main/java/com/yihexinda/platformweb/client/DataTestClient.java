package com.yihexinda.platformweb.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Jack
 * @date 2018/10/12.
 */
@FeignClient(value = "data-service")
@RequestMapping("/test")
public interface DataTestClient {
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    List<Object> getTestList();
}
