package com.yihexinda.core.vo;

import com.yihexinda.core.constants.ResultConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 统一返回数据集
 * @author wenbn
 * @version 1.0
 * @date 2018/11/28 0028
 */
@ApiModel(value="结果集对象", description="结果集对象")
public class ResultVo {

    @ApiModelProperty(value = "请求状态码 0：成功")
    private int result;//请求状态 返回值

    @ApiModelProperty(value = "请求状态信息")
    private String message = ResultConstant.SYS_REQUIRED_SUCCESS_VALUE;//请求状态信息

    @ApiModelProperty(value = "token")
    private String token;//登录session返回token

    @ApiModelProperty(value = "系统当前时间")
    private Date systemTime = new Date();//系统当前时间

    @ApiModelProperty(value = "返回的结果集")
    private Object dataSet;//返回的结果集

    @ApiModelProperty(value = "返回的分页数据")
    private Object pageInfo;//返回的分页数据

    public String getToken() {
        return token;
    }

    public ResultVo setToken(String token) {
        this.token = token;
        return this;
    }

    public Date getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(Date systemTime) {
        this.systemTime = systemTime;
    }

    public int getResult() {
        return result;
    }

    public ResultVo setResult(int result) {
        this.result = result;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultVo setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getDataSet() {
        return dataSet;
    }

    public ResultVo setDataSet(Object dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public Object getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Object pageInfo) {
        this.pageInfo = pageInfo;
    }


    //请求失败
    public static ResultVo error(int result, String message) {
        return new ResultVo(result, message, new Date());
    }

    //请求成功
    public static ResultVo success(){
        return new ResultVo(ResultConstant.SYS_REQUIRED_SUCCESS, ResultConstant.SYS_REQUIRED_SUCCESS_VALUE, new Date());
    }

    public static ResultVo error(Status status) {
        return new ResultVo(status.getCode(), status.getStandardMessage(), new Date());
    }

    public ResultVo() {
        super();
    }

    public ResultVo(int result, String message,Date systemTime) {
        this.result = result;
        this.message = message;
        this.systemTime = systemTime;
    }

    public ResultVo(int result, String message, String token, Date systemTime, Object dataSet, Object pageInfo) {
        this.result = result;
        this.message = message;
        this.token = token;
        this.systemTime = systemTime;
        this.dataSet = dataSet;
        this.pageInfo = pageInfo;
    }

    public enum Status {
        //请求参数错误
        REQUIRED_PARAMETER_ERROR(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR,ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE),
        //请求数据异常
        SYS_REQUIRED_DATA_ERROR(ResultConstant.SYS_REQUIRED_DATA_ERROR,ResultConstant.SYS_REQUIRED_DATA_ERROR_VALUE),
        //二维码过期
        SYS_QRCODE_EXPIRED_ERROR(ResultConstant.SYS_QRCODE_EXPIRED_ERROR,ResultConstant.SYS_QRCODE_EXPIRED_ERROR_VALUE),
        //订单已支付
        SYS_ORDER_HAVE_PAID_ERROR(ResultConstant.SYS_ORDER_HAVE_PAID_ERROR,ResultConstant.SYS_ORDER_HAVE_PAID_ERROR_VALUE),
        //请求失败
        SYS_REQUIRED_FAILURE(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE),
        //订单数据异常
        SYS_ORDER_DATA_ERROR(ResultConstant.SYS_ORDER_DATA_ERROR,ResultConstant.SYS_ORDER_DATA_ERROR_VALUE),
        //还有未出行的订单
        SYS_PEAK_PURCHASE_LIMITATION_ERROR(ResultConstant.SYS_PEAK_PURCHASE_LIMITATION_ERROR,ResultConstant.SYS_PEAK_PURCHASE_LIMITATION_ERROR_VALUE),
        //订单示支付
        SYS_ORDER_UNPAID_ERROR(ResultConstant.SYS_ORDER_UNPAID_ERROR,ResultConstant.SYS_ORDER_UNPAID_ERROR_VALUE)
        ;

        private int code;
        private String standardMessage;

        Status(int code, String standardMessage) {
            this.code = code;
            this.standardMessage = standardMessage;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStandardMessage() {
            return standardMessage;
        }

        public void setStandardMessage(String standardMessage) {
            this.standardMessage = standardMessage;
        }
    }
}
