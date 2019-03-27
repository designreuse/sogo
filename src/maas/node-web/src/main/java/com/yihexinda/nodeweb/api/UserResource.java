package com.yihexinda.nodeweb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.UserDto;
import com.yihexinda.data.enums.UserOnlineStatusEnum;
import com.yihexinda.nodeweb.client.DataUserClient;
import com.yihexinda.nodeweb.security.SecurityHelper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2018-11-01.
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserResource {
    @Autowired
    private DataUserClient userClient;
    @Autowired
    private SecurityHelper securityHelper;

    @ApiOperation(value = "获取用户在线状态", httpMethod = "GET")
    @GetMapping("/checkUserOnlineStatus")
    @ResponseBody
    public Json<UserOnlineStatusEnum> checkUserOnlineStatus() {
        Long userId = securityHelper.getCurrentUser().getId();
        Json json = userClient.checkUserOnlineStatus((long) userId);
        return json;
    }

    @ApiOperation(value = "更新用户在线状态", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "onlineStatus", value = "用户状态：在线，离线", dataType = "String", paramType = "json", required = true)
    })
    @PostMapping("/updateUserOnlineStatus")
    @ResponseBody
    public Json updateUserOnlineStatus(@RequestBody UserDto userDto) {
        Long userId = securityHelper.getCurrentUser().getId();
        userDto.setUserId(userId.intValue());
        Json json = userClient.updateUserOnlineStatus(userDto);
        return json;
    }

}
