package com.yihexinda.userweb.client;

import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.data.dto.TCarDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Jack
 * @date 2018/10/12.
 */
@FeignClient(value = "data-service")
@RequestMapping("/api/carApi")
public interface CarClient {

    @RequestMapping(method = RequestMethod.GET, value = "/getCars")
    List<TCarDto> getCars();
}
