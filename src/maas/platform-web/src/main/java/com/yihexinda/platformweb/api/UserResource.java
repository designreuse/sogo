package com.yihexinda.platformweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.core.Execption.BussException;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.core.utils.AssertUtils;
import com.yihexinda.data.dto.UserDto;
import com.yihexinda.data.dto.UserQueryDto;
import com.yihexinda.data.enums.UserOnlineStatusEnum;
import com.yihexinda.platformweb.client.DataUserClient;
import com.yihexinda.platformweb.security.SecurityHelper;
import com.yihexinda.platformweb.service.UserService;

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
    private UserService userService;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private DataUserClient userClient;

    @ApiOperation(value = "查询可被派单网点用户列表", httpMethod = "POST")
    @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "String", paramType = "form",
            required = true)
    @PostMapping("/findAvailableUsersToAssign")
    public Json<List<UserDto>> findAvailableUsersToAssign(@RequestParam("orderId") String orderId) {
        return userService.findAvailableUsersToAssign(orderId);
    }


    /**
     * 根据条件查询用户信息
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "根据条件查询用户信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "compId", value = "公司ID", dataType = "String", paramType = "json"),
            @ApiImplicitParam(name = "deptId", value = "部门ID", dataType = "String", paramType = "json"),
            @ApiImplicitParam(name = "name", value = "姓名", dataType = "String", paramType = "json"),
            @ApiImplicitParam(name = "username", value = "登陆账号", dataType = "String", paramType = "json")
    })
    @PostMapping("/findUserPage")
    public Json<Page<UserDto>> findUserByUserQueryDto(@RequestBody(required = false) UserQueryDto dto) {
        return userService.findUserByUserQueryDto(dto);

    }

    @ApiOperation(value = "更新用户在线状态", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "onlineStatus", value = "用户状态：在线，离线", dataType = "String", paramType = "json", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "json", required = true)
    })
    @PostMapping("/updateUserOnlineStatus")
    @ResponseBody
    public Json updateUserOnlineStatus(@RequestBody UserDto userDto) {
        Json json = new Json();
        try {
            AssertUtils.notNullAndEmpty(userDto.getUserId(), "用户ID不能为空");
            AssertUtils.notNullAndEmpty(userDto.getOnlineStatus(), "用户在线状态不能为空");
            if (userDto.getOnlineStatus().equals(UserOnlineStatusEnum.忙碌)) {
                throw new BussException("用户在线状态不可置为忙碌");
            }
            userDto.setForce(true);
            json = userClient.updateUserOnlineStatus(userDto);
        } catch (BussException e) {
            json.setMsg(e.getMessage());
        } catch (Exception e) {
            json.setMsg("服务器内部错误");
        }
        return json;
    }

    @ApiOperation(value = "登出", httpMethod = "GET")
    @RequestMapping("/logout")
    @ResponseBody
    public Json logout() {
        Json json = new Json();
        try {
            SecurityContextHolder.clearContext();
        } catch (BussException e) {
            log.warn("logout{}", e);
            json.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("logout{}", e);
            json.setMsg("服务器内部错误");
        }
        return json;
    }

}
