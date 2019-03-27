package com.yihexinda.userweb.api;

import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.Constants;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.param.QRCodeParam;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.userweb.client.OrderClient;
import com.yihexinda.userweb.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/4 0004
 */
@Api(description = "二维码接口管理")
@RestController()
@RequestMapping("api/qrcode")
@Slf4j
public class QRCodeResource {


    @Autowired
    private OrderClient orderClient;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 扫码回调接口
     * @param param
     * @return
     */
    @PostMapping("/callBack")
    @ApiOperation(value = "扫码回调接口", httpMethod = "POST")
    @NoRequireLogin
    public ResultVo callBack(@RequestBody QRCodeParam param) throws ParseException {
        String expTime = param.getExpTime();
        String oid = param.getOid();
        String token = param.getToken();
        if(StringUtil.isEmpty(expTime) || StringUtil.isEmpty(oid) || StringUtil.isEmpty(token)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        Date expDate = DateUtils.parseDate(expTime, Constants.DATE_FULL_TIME_PATTERN);
        if(expDate.getTime()<= new Date().getTime()){
            return ResultVo.error(ResultVo.Status.SYS_QRCODE_EXPIRED_ERROR);
        }
        return orderClient.checkQrCallBack(param);
    }

    /**
     * 显示二维码
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/show",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单Id", required = true, paramType = "String"),
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "String")
    })
    public ResultVo productQRCode(@RequestBody Map<String, Object> params) throws Exception {
        String token = StringUtil.trim(params.get("token"));
        String id = StringUtil.trim(params.get("id"));
        if(StringUtil.isEmpty(token) || StringUtil.isEmpty(id)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        //根据订单id查订单信息
        return orderClient.getOrderQRCode(params);
    }

    /**
     *  生成二维码
     * @param tOrderDto 订单
     * @param token token
     * @return QRCodeParam
     */
    public QRCodeParam generate(TOrderDto tOrderDto,String token) {
        QRCodeParam qrCodeParam = new QRCodeParam();
        qrCodeParam.setOid(tOrderDto.getId());
        qrCodeParam.setToken(token);
        //出行 + 1小时
        Date tripDate = new Date();
        String routeType = tOrderDto.getRouteType();
        if ("0".equals(routeType)) {
            tripDate = new Date(tOrderDto.getTripTime().getTime() + 60 * 1000 * 60);
        }
        if ("1".equals(routeType)) {
            tripDate = new Date(DateUtils.getDayEnd().getTime());
        }
        qrCodeParam.setExpTime(DateUtils.formatDate(tripDate, DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss));
        return qrCodeParam;

    }
}
