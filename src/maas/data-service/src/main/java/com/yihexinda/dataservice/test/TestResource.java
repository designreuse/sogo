package com.yihexinda.dataservice.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jack
 * @date 2018/10/11.
 */
@RestController
@RequestMapping("/test")
public class TestResource {

    @RequestMapping("/testInvokeAsms")
    @ResponseBody
    public String testInvokeAsms() {
        return "success";
    }


}
