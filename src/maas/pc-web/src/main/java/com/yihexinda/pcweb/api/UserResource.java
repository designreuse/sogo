package com.yihexinda.pcweb.api;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.data.dto.TSysUserDto;
import com.yihexinda.data.dto.TUserDto;
import com.yihexinda.data.dto.TUserFeedDto;
import com.yihexinda.pcweb.client.OrderClient;
import com.yihexinda.pcweb.client.SysUserClient;
import com.yihexinda.pcweb.client.UserClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/08
 */
@Api(description = "客户端用户接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class UserResource {

    @Autowired
    private UserClient userClient;


    /**
     * 查询用户信息列表
     * @return 用户信息列表
     */
    @ApiOperation(value = "查询用户信息列表", httpMethod = "POST")
    @RequestMapping(value = "/userList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
    })
    public ResultVo userList(@RequestBody Map<String,Object> condition) {
        return userClient.userList(condition);
    }


    /**
     * 查询用户信息列表(导出)
     */
    @ApiOperation(value = "查询用户信息列表（导出）", httpMethod = "GET")
    @RequestMapping(value = "/getUserListExcel")
    @NoRequireLogin
    public void getUserListExcel(HttpServletResponse response) {
        try {
            String fileName = "用户列表.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"用户昵称", "手机号", "性别", "地址", "注册时间"};
            List<TUserDto> tUserDtoList =  userClient.getUserListExcel();
            String[][] objects = new String[tUserDtoList.size()]
                    [tUserDtoList.get(0).toString().split(",").length];
            for (int i = 0; i < tUserDtoList.size(); i++) {
                TUserDto tUserDto = tUserDtoList.get(i);
                objects[i][0] = tUserDto.getNick();
                objects[i][1] = tUserDto.getPhone();
                if ("0".equals(tUserDto.getGender())){
                    objects[i][2] = "未知";
                }
                if ("1".equals(tUserDto.getGender())){
                    objects[i][2] = "男";
                }
                if ("2".equals(tUserDto.getGender())){
                    objects[i][2] = "女";
                }
                objects[i][3] = tUserDto.getCountry()+tUserDto.getProvince()+tUserDto.getCity();
                if ("".equals(StringUtil.trim(tUserDto.getCreateDate()))){
                    objects[i][4] ="";
                }else{
                    objects[i][4] =  DateUtils.formatDate(tUserDto.getCreateDate(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
                }
            }
            HSSFWorkbook workbook = ExcelXUtils.getHSSFWorkbook(fileName, title, objects, null);
            workbook.write(output);
            output.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


}
