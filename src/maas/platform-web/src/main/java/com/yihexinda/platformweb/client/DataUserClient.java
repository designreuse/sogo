package com.yihexinda.platformweb.client;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.data.dto.UserDto;
import com.yihexinda.data.dto.UserQueryDto;

/**
 *
 */
@FeignClient("data-service")
@RequestMapping("/user")
public interface DataUserClient {


    @RequestMapping(value = "/createOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    Json createOrUpdate(SysUserDto userDto);

    @RequestMapping(value = "/updateAuthorities/{userId}", method = RequestMethod.POST)
    @ResponseBody
    Json updateAuthorities(@PathVariable("userId") Integer userId, Set<String> authorities);

    @PostMapping("/findAvailableUsersToAssign")
    List<UserDto> findAvailableUsersToAssign(@RequestParam("orderId") String orderId);

    @PostMapping("/findUserByUserQueryDto")
    Page<UserDto> findUserByUserQueryDto (UserQueryDto dto);

    /**
     *
     * @param userDto
     */
    @RequestMapping(value = "/updateUserOnlineStatus",method = RequestMethod.POST)
    @ResponseBody
    Json updateUserOnlineStatus(@RequestBody UserDto userDto);
}
