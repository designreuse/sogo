package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSysUserDto;
import com.yihexinda.data.dto.TUserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/user/client")
public interface UserClient {

    /**
     * pengFeng
     * 查询用户信息列表
     * @param  condition 参数map
     * @return 用户信息列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/userList")
    ResultVo userList(@RequestBody Map<String, Object> condition);

    /**
     * 查询用户信息列表(导出)
     * @return 用户信息列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUserListExcel")
    List<TUserDto> getUserListExcel();
}
