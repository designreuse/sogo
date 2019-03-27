package com.yihexinda.userweb.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.alibaba.fastjson.JSONObject;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.param.QRCodeParam;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.data.dto.TUserDto;
import com.yihexinda.userweb.client.OrderClient;
import com.yihexinda.userweb.client.UserClient;
import com.yihexinda.userweb.config.SmsConstant;
import com.yihexinda.userweb.config.WxMaConfiguration;
import com.yihexinda.userweb.utils.RedisUtil;
import com.yihexinda.userweb.utils.WxMaProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(description = "用户接口")
@RestController()
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WxMaProperties wxMaProperties;

    @Autowired
    private  OrderClient orderClient;

    @Autowired
    private  QRCodeResource qRCodeResource;

    /**
     * 获取openid
     * @param jsCode
     * @return
     */
    @PostMapping("/userAuthorization")
    public ResultVo userAuthorization(@RequestParam("jsCode")  String jsCode){
        if(StringUtil.isEmpty(jsCode)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }
        //获取appId
        List<WxMaProperties.Config> configs = wxMaProperties.getConfigs();
        String appid = configs.get(0).getAppid();
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
        try {
            //获取openId
            WxMaJscode2SessionResult sessionInfo = wxService.getUserService().getSessionInfo(jsCode);
            Map<String,String> paramter = new HashMap<String,String>();
            paramter.put("openId", sessionInfo.getOpenid());

            //校验用户
            TUserDto user = userClient.checkUserExist(paramter);
            if(user != null) {
                return ResultVo.success().setResult(-1).setMessage("用户已授权").setDataSet(sessionInfo);
            }
            return ResultVo.success().setDataSet(sessionInfo);
        } catch (WxErrorException e) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"code过期");
        }
    }

    /**
     * 解密用户信息
     * @param paramter
     * @return
     */
    @ApiOperation(value = "解密用户信息", httpMethod = "POST")
    @RequestMapping(value = "/decryptionUserInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionKey", value = "sessionKey", required = true, paramType = "string"),
            @ApiImplicitParam(name = "encryptedData", value = "data加密的信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "iv", value = "iv", required = true, paramType = "string"),
            @ApiImplicitParam(name = "openId", value = "用户openId", required = true, paramType = "string"),
            @ApiImplicitParam(name = "nick", value = "用户姓名", required = true, paramType = "string"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "string"),
            @ApiImplicitParam(name = "gender", value = "性别(0：未知、1：男、2：女", required = true, paramType = "string"),
            @ApiImplicitParam(name = "avatarurl", value = "用户头像地址", required = true, paramType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = true, paramType = "string"),
            @ApiImplicitParam(name = "province", value = "省份", required = true, paramType = "string"),
            @ApiImplicitParam(name = "country", value = "国家", required = true, paramType = "string"),
            @ApiImplicitParam(name = "language", value = "语言", required = true, paramType = "string"),
            @ApiImplicitParam(name = "registerIp", value = "注册地址", required = true, paramType = "string"),

    })
    @NoRequireLogin
    public ResultVo decryptionUserInfo(@ApiIgnore @RequestBody Map<String,String> paramter) {
        //获取appId
        List<WxMaProperties.Config> configs = wxMaProperties.getConfigs();
        String appId = configs.get(0).getAppid();
        final WxMaService wxService = WxMaConfiguration.getMaService(appId);
        //校验数据
        if(StringUtil.isEmpty(paramter.get("sessionKey")) || StringUtil.isEmpty(paramter.get("encryptedData")) || StringUtil.isEmpty(paramter.get("iv"))) {
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }
        //解密数据
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(paramter.get("sessionKey"), paramter.get("encryptedData"), paramter.get("iv"));
        paramter.put("phone",phoneNoInfo.getPhoneNumber());
        paramter.put("appId",appId);
        return userClient.decryptionUserInfo(paramter);

    }


    /**
     * 用户登录
     * @param paramter
     * @return
     */
    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @PostMapping("/loginUser")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "openId", value = "用户openId", required = true, paramType = "string"),
            @ApiImplicitParam(name = "nick", value = "用户姓名", required = true, paramType = "string"),
            @ApiImplicitParam(name = "gender", value = "性别(0：未知、1：男、2：女", required = true, paramType = "string"),
            @ApiImplicitParam(name = "avatarurl", value = "用户头像地址", required = true, paramType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = true, paramType = "string"),
            @ApiImplicitParam(name = "province", value = "省份", required = true, paramType = "string"),
            @ApiImplicitParam(name = "country", value = "国家", required = true, paramType = "string"),
            @ApiImplicitParam(name = "language", value = "语言", required = true, paramType = "string"),
            @ApiImplicitParam(name = "registerIp", value = "注册地址", required = true, paramType = "string"),
    })
    @NoRequireLogin
    public ResultVo loginUser(@ApiIgnore @RequestBody Map<String,String> paramter) {
        List<WxMaProperties.Config> configs = wxMaProperties.getConfigs();
        String appId = configs.get(0).getAppid();
        String phone = paramter.get("phone");
        String code = paramter.get("code");
        if(StringUtil.isEmpty(code)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "请输入验证码");
        }
        //校验手机号-验证码是否正确
        String checkCode = redisUtil.get(SmsConstant.SMS_LOGIN_USER + phone);
        if (!StringUtil.isEmpty(checkCode)) {
            String[] telephoneCode = checkCode.split("-");
            if (telephoneCode != null && telephoneCode[0].equals(phone) && telephoneCode[1].equals(code)) {
                paramter.put("appId", appId);
                ResultVo resultVo = userClient.loginUser(paramter);
                if(resultVo.getResult() == ResultConstant.SYS_REQUIRED_SUCCESS){
                    redisUtil.remove(SmsConstant.SMS_LOGIN_USER + phone);
                }
                return resultVo;
            }
            return ResultVo.error(ResultConstant.PLEASE_ENTER_THE_CORRECT_VERIFICATION_CODE, ResultConstant.PLEASE_ENTER_THE_CORRECT_VERIFICATION_CODE_VALUE);
        } else {
            return ResultVo.error(ResultConstant.SYS_IMG_CODE_IS_OVERDUE, ResultConstant.SYS_IMG_CODE_IS_OVERDUE_VALUE);
        }
    }

    /**
     * 返回用户登录token
     * @param paramter
     * @return
     */
    @ApiOperation(value = "返回用户登录token", httpMethod = "POST")
    @PostMapping("/checkToken")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "用户openId", required = true, paramType = "string"),
    })
    @NoRequireLogin
    public ResultVo checkToken(@RequestBody Map<String,String> paramter) {
        String openId = StringUtil.trim(paramter.get("openId"));
        return userClient.checkToken(paramter);
    }

    @NoRequireLogin
    @RequestMapping(value = "/getLatelyOrder", method = RequestMethod.POST)
    @ApiOperation(value = "我的行程二维码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "string"),
    })
    public ResultVo getLatelyOrder(@RequestBody Map<String, Object> data) {
        String userId =RequestUtil.analysisToken(String.valueOf(data.get("token"))).getUserId();
        data.put("userId",userId);
        ResultVo latelyOrder = orderClient.getLatelyOrder(data);
        if(latelyOrder.getResult() == ResultConstant.SYS_REQUIRED_SUCCESS){
            TOrderDto tOrderDto = (TOrderDto) latelyOrder.getDataSet();
            QRCodeParam qrCodeParam = qRCodeResource.generate(tOrderDto,String.valueOf(data.get("token")));
            latelyOrder.setDataSet(qrCodeParam);
        }
        return ResultVo.success();
    }


    @NoRequireLogin
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @ApiOperation(value = "个人资料", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "string"),
    })
    public ResultVo getUserInfo(@RequestBody Map<String, Object> data) {
        //解析token
        String userId = RequestUtil.analysisToken(String.valueOf(data.get("token"))).getUserId();
        return  userClient.getUserInfo(userId);
    }
    /**
     * 修改用户信息
     * @param paramter
     * @return
     */
    @ApiOperation(value = "修改", httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    @ApiImplicitParams({
                @ApiImplicitParam(name = "phone", value = "用户电话号", required = true, paramType = "string"),
                @ApiImplicitParam(name = "realName", value = "真实姓名", required = true, paramType = "string"),
                @ApiImplicitParam(name = "compName", value = "公司名称", required = true, paramType = "string"),
                @ApiImplicitParam(name = "deptName", value = "部门名称", required = true, paramType = "string"),
    })
    @NoRequireLogin
    public ResultVo updateUserInfo(@RequestBody Map<String,String> paramter) {
        String phone=StringUtil.trim(paramter.get("phone"));
        String realName=StringUtil.trim(paramter.get("realName"));
        String compName=StringUtil.trim(paramter.get("compName"));
        String deptName=StringUtil.trim(paramter.get("deptName"));
        if(StringUtil.isEmpty(phone)||StringUtil.isEmpty(realName)||StringUtil.isEmpty(compName)||StringUtil.isEmpty(deptName)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR, ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }

        return userClient.updateUserInfo(paramter);
    }


}
