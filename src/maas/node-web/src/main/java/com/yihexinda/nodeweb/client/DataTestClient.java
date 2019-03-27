package com.yihexinda.nodeweb.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Jack
 * @date 2018/10/12.
 */
@FeignClient(value = "data-service")
@RequestMapping("/testApi")
public interface DataTestClient {
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    List<Object> getTestList();

    @RequestMapping(method = RequestMethod.GET, value = "/test")
    Object testApi();

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    Object hello();

    @RequestMapping(method = RequestMethod.GET, value = "/test2")
    Object test2();
}
