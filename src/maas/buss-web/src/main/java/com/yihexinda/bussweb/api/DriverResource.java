package com.yihexinda.bussweb.api;

import com.google.common.collect.Maps;
import com.yihexinda.bussweb.client.DriverClient;
import com.yihexinda.bussweb.config.SmsConstant;
import com.yihexinda.bussweb.utils.RedisUtil;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * @author ysh
 * @version 1.0
 * @date 2018/11/29 0029
 */
@Api(description = "司机端接口")
@RestController
@RequestMapping("/api/buss")
@Slf4j
public class DriverResource {

    @Autowired
    private DriverClient diverClient;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * ysh
     * 司机登录
     *
     * @param condition  参数
     * @return ResultVo
     */
    @NoRequireLogin
    @ApiOperation(value = "司机登录", httpMethod = "POST")
    @RequestMapping(value = "/loginDriver", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "no", value = "司机工号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "司机登录密码", required = true, dataType = "String")
    })
    public ResultVo loginDriver(@RequestBody Map<String, Object> condition) {
        return diverClient.loginDriver(condition);

    }


    /**
     * 司机退出登录
     *
     * @param data  参数
     * @return ResultVo
     */
    @NoRequireLogin
    @ApiOperation(value = "司机退出登录", httpMethod = "POST")
    @RequestMapping(value = "/loginOutDriver", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")
    })
    public ResultVo loginOutDriver(@RequestBody Map<String, Object> data) {
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
        if (null!=payLoadVo){
            return diverClient.loginOutDriver(payLoadVo.getUserId());
        }
        return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析失败，请核对token参数");

    }


    /**
     * 查询司机详情
     * @param data  token参数
     * @return ResultVo
     */
    @ApiOperation(value = "查询司机详情", httpMethod = "POST")
    @RequestMapping(value = "/getDriverInfo", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token参数", required = true, dataType = "String")
    })
    public ResultVo getDriverInfo(@RequestBody Map<String, Object> data) {
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
       if (null!=payLoadVo){
         return   diverClient.getDriver(payLoadVo.getUserId());
       }
        return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析失败，请核对token参数");
    }


    /**
     * ysh
     * 验证码校验
     *
     * @param paramter
     * @return
     */
    @NoRequireLogin
    @ApiOperation(value = "验证码校验", httpMethod = "POST")
    @RequestMapping(value = "/checkCacheCodeDriver", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "短信验证码", required = true, paramType = "String"),
            @ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "String")
    })
    public ResultVo checkCacheCodeDriver(@RequestBody Map<String, String> paramter) {
        //手机号
        String phone = StringUtil.trim(paramter.get("phone"));
        //验证码
        String code = StringUtil.trim(paramter.get("code"));
        //校验手机号-验证码是否正确
        String checkCode = redisUtil.get(SmsConstant.SMS_LOGIN_USER + phone);
        if (!StringUtil.isEmpty(checkCode)) {
            String[] telephoneCode = checkCode.split("-");
            //校验 手机号和验证码
            if (null != telephoneCode && telephoneCode[0].equals(phone) && telephoneCode[1].equals(code)) {
                redisUtil.remove(SmsConstant.SMS_LOGIN_USER + phone);
                //将手机号码 再次设置到redis缓存中，以便修改密码的时候使用
                redisUtil.set("phone" + phone, phone);
                return ResultVo.success();
            }
        }
        //验证失败
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * ysh
     * 根据关联的用户设置密码
     *
     * @param params
     * @return
     */
    @NoRequireLogin
    @ApiOperation(value = "设置密码", httpMethod = "POST")
    @RequestMapping(value = "/setPasswordDriver", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "新密码", required = true, paramType = "String"),
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, paramType = "String")
    })
    public ResultVo setPasswordDriver(@RequestBody Map<String, Object> params) {
        String phoneToClient = StringUtil.trim(params.get("phone"));
        //取出redis里面的关联用户手机号码
        String phone = redisUtil.get("phone" + phoneToClient);
        //如果有数据则传过来的和存的一致
        if (phone != null) {
            //将获取的手机号传给后台
            params.put("phone", phone);
            return diverClient.setPasswordDriver(params);
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 司机休息
     *
     * @param data 参数
     * @return ResultVo
     */
    @ApiOperation(value = "司机休息操作", httpMethod = "POST")
    @RequestMapping(value = "/restDriver", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token令牌", required = true, paramType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, paramType = "String"),
    })
    public ResultVo restDriver(@RequestBody Map<String, Object> data) {
        if ("".equals(StringUtil.trim(data.get("status")))){
            ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"缺少参数");
        }
        PayLoadVo payLoadVo= RequestUtil.analysisToken(StringUtil.trim(data.get("token")));
        if (null!=payLoadVo){
            TDriverDto tDriverDto =  new TDriverDto();
//            tDriverDto.setStatus("2");
            tDriverDto.setStatus(String.valueOf(data.get("status")));
            tDriverDto.setId(payLoadVo.getUserId());
            return  diverClient.updateDriverInfo(tDriverDto);
        }
        return  ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR,"token解析失败，请核对token参数");
    }



    /**
     * 检查司机是否设置休息
     * @param data
     * @return ResultVo
     */
    @ApiOperation(value = "检查司机是否设置休息", httpMethod = "POST")
    @RequestMapping(value = "/checkDriverStatus", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token令牌", required = true, paramType = "String"),
            @ApiImplicitParam(name = "driverId", value = "司机ID", required = true, paramType = "String"),
    })
    public ResultVo checkDriverStatus(@RequestBody Map<String, Object> data ) {
        String token = StringUtil.trim(data.get("token"));
        data.put("driverId",RequestUtil.analysisToken(token).getUserId());
        return diverClient.checkDriverStatus(data);
    }

    /**
     * 司机退出登录
     * @author chenzeqi
     * @param data
     * @return
     */
    @PostMapping("driverLogout")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token令牌", required = true, paramType = "String")
    })
    public ResultVo driverLogout(@RequestBody Map<String,Object> data){
        if(data==null){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        String token = StringUtil.trim(data.get("token"));
        data.put("driverId",RequestUtil.analysisToken(token).getUserId());
        return diverClient.driverLogout(data);
    }


}
