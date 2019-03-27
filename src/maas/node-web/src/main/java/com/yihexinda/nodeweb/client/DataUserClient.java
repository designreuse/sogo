package com.yihexinda.nodeweb.client;

import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.UserDto;
import com.yihexinda.data.enums.UserOnlineStatusEnum;

/**
 *
 */
@FeignClient("data-service")
@RequestMapping("/user")
public interface DataUserClient {


    @RequestMapping(value = "/createOrUpdate",method = RequestMethod.POST)
    @ResponseBody
    void createOrUpdate(SysUserDto userDto);

    /**
     *
     * @param userId
     */
    @RequestMapping(value = "/checkUserOnlineStatus",method = RequestMethod.GET)
    @ResponseBody
    Json<UserOnlineStatusEnum> checkUserOnlineStatus(@RequestParam("userId") Long userId);

    /**
     *
     * @param userDto
     */
    @RequestMapping(value = "/updateUserOnlineStatus",method = RequestMethod.POST)
    @ResponseBody
    Json updateUserOnlineStatus(@RequestBody UserDto userDto);


    /**
     *  获取用户状态
     * @param
     */
    @RequestMapping(value = "/findUserStatus",method = RequestMethod.POST)
    @ResponseBody
    UserDto findUserStatus(@RequestParam("userId") Long userId);


    @RequestMapping(value = "/updateAuthorities/{userId}", method = RequestMethod.POST)
    @ResponseBody
    Json updateAuthorities(@PathVariable("userId") Integer userId, Set<String> authorities);

}
