package com.yihexinda.nodeweb.web;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yihexinda.nodeweb.client.DataTestClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.data.dto.OrderDetailsDto;
import com.yihexinda.data.dto.OrderDto;
import com.yihexinda.data.dto.OrderPageQueryDto;
import com.yihexinda.nodeweb.client.DataOrderClient;
import com.yihexinda.nodeweb.security.SecurityHelper;
import com.yihexinda.nodeweb.security.SecurityUtils;
import com.yihexinda.nodeweb.service.AuthService;
import com.yihexinda.nodeweb.service.UserService;

/**
 * @author Jack
 * @date 2018/10/12.
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private DataOrderClient orderClient;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @Autowired
    private DataTestClient dataTestClient;

    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("name", "test-name");
        return "test";
    }

    @RequestMapping("/testUser")
    @ResponseBody
    public Object testUser(Model model) {
        return securityHelper.getCurrentUser();
    }

    @RequestMapping("/testUsername")
    @ResponseBody
    public Object testUsername(Model model) {
        return SecurityUtils.getCurrentUserLogin();
    }

    @RequestMapping("/testLogin")
    @ResponseBody
    public Object testLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (StringUtils.isEmpty(username)) username = "node-user";
        securityHelper.login(username, request);
        SysUserDto userDto = authService.findSysUserDtoByUsername(username);
        userService.createOrUpdateUser(userDto);
        return SecurityUtils.getCurrentUserLogin();
    }

    @RequestMapping("/testSessionId")
    @ResponseBody
    public Object testToken(HttpServletRequest request) {
        return request.getSession().getId();
    }

    @RequestMapping("/clearCookie")
    @ResponseBody
    public Object testToken(HttpServletResponse response) {
//        Cookie cookie = new Cookie("SESSION", null); // Not necessary, but saves bandwidth.
        Cookie cookie = new Cookie("scheduling-session", null); // Not necessary, but saves bandwidth.
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
        response.addCookie(cookie);
        return "clear";
    }

    @RequestMapping("/testOrderPage")
    @ResponseBody
    public Page<OrderDto> testOrderPage() {
        OrderPageQueryDto findOrderPageDto = new OrderPageQueryDto();
        findOrderPageDto.setPage(0);
        findOrderPageDto.setPageSize(10);
        findOrderPageDto.setStartDate("2018-10-10");
        findOrderPageDto.setEndDate("2018-10-20");
        return orderClient.findOrderPage(findOrderPageDto);
    }

    @RequestMapping("/testAuthorities")
    @ResponseBody
    public Object testAuthorities() throws InterruptedException {
//        return userService.findAuthoritiesByUserId(1002);
//        return SecurityUtils.hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_ALL);
//        return SecurityUtils.getAuthorities();
        return "";
    }

    /**
     * @return
     */
    @RequestMapping("/testUserRoot")
    @ResponseBody
    public List<Integer> testUserRoot() {
        List<Integer> list = Lists.newArrayList();
//        boolean b = SecurityUtils.hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_ALL);
        if (true) {

        }
        return null;

    }

    @RequestMapping("/testFindOrderDetails")
    @ResponseBody
    public Json<OrderDetailsDto> testFindOrderDetails() {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        orderDetailsDto.setDdbh("TK17122003024");
        return orderClient.findOrderDetails("TK17122003024");
    }


    @GetMapping("/test1")
    @ResponseBody
    public Object test1() {
        return dataTestClient.testApi();
    }



    @GetMapping("/testHello")
    @ResponseBody
    public String testHello() {
        return dataTestClient.hello().toString();
    }

    @GetMapping("/test2")
    @ResponseBody
    public Object test2() {
        return  dataTestClient.test2();
    }

}
