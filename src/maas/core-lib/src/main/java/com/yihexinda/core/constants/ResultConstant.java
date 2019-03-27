package com.yihexinda.core.constants;

/**
 * 全局消息常量类
 * Author:wenbn
 * Date:2018/1/11
 * Description:
 */
public class ResultConstant {

    ////////////////////////////////系统常量//////////////////////////////////

    ////////////////http请求常见状态码 start/////////////
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final String HTTP_STATUS_BAD_REQUEST_VALUE = "错误请求";

    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final String HTTP_STATUS_NOT_FOUND_VALUE = "请求的资源不可用";

    public static final int HTTP_STATUS_METHOD_NOT_ALLOWED = 405;
    public static final String HTTP_STATUS_METHOD_NOT_ALLOWED_VALUE = "不合法的请求方法";

    public static final int HTTP_STATUS_UNSUPPORTED_MEDIA_TYPE = 415;
    public static final String HTTP_STATUS_UNSUPPORTED_MEDIA_TYPE_VALUE = "内容类型不支持";

    public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;
    public static final String HTTP_STATUS_INTERNAL_SERVER_ERROR_VALUE = "内部服务错误";

    ////////////////http请求常见状态码 end///////////////

    public static final int SYS_REQUIRED_UNLOGIN_ERROR = 1001;
    public static final String SYS_REQUIRED_UNLOGIN_ERROR_VALUE = "请先登录";

    public static final int SYS_REQUIRED_LOGIN_SUCCESS = 1002;
    public static final String SYS_REQUIRED_LOGIN_SUCCESS_VALUE = "登录成功";

    public static final int SYS_REQUIRED_UNAUTHORIZED_ERROR = 1003;
    public static final String SYS_REQUIRED_UNAUTHORIZED_ERROR_VALUE = "没有访问权限";

    public static final int SYS_REQUIRED_PARAMETER_ERROR = 1004;
    public static final String SYS_REQUIRED_PARAMETER_ERROR_VALUE = "请求参数错误";

    public static final int SYS_REQUIRED_TIMEOUT_ERROR = 1005;
    public static final String SYS_REQUIRED_TIMEOUT_ERROR_VALUE = "请求超时";

    public static final int SYS_REQUIRED_SUCCESS = 0;
    public static final String SYS_REQUIRED_SUCCESS_VALUE = "请求成功";

    public static final int SYS_IMG_CODE_IS_OVERDUE = 1006;
    public static final String SYS_IMG_CODE_IS_OVERDUE_VALUE = "验证码过期";

    public static final int SYS_REQUIRED_UNOPERATION_ERROR = 1007;
    public static final String SYS_REQUIRED_UNOPERATION_ERROR_VALUE = "没有操作权限";

    public static final int SYS_IMG_CODE_ERROR = 1008;
    public static final String SYS_IMG_CODE_ERROR_VALUE = "验证码错误";

    public static final int SYS_REQUIRED_DATA_ERROR = 1009;
    public static final String SYS_REQUIRED_DATA_ERROR_VALUE = "请求数据解析异常";

    public static final int SYS_APPOINTMENT_FAILURE = 1010;
    public static final String SYS_APPOINTMENT_FAILURE_VALUE = "预约失败";

    public static final int SYS_QRCODE_EXPIRED_ERROR = 1011;
    public static final String SYS_QRCODE_EXPIRED_ERROR_VALUE = "二维码过期";

    public static final int SYS_ORDER_DATA_ERROR = 2001;
    public static final String SYS_ORDER_DATA_ERROR_VALUE = "订单数据异常";


    public static final int SYS_ORDER_HAVE_PAID_ERROR = 2002;
    public static final String SYS_ORDER_HAVE_PAID_ERROR_VALUE = "订单已支付";

    public static final int SYS_ORDER_UNPAID_ERROR = 2003;
    public static final String SYS_ORDER_UNPAID_ERROR_VALUE = "订单未支付";

    public static final int SYS_ORDER_HAS_EXPIRED_ERROR = 2004;
    public static final String SYS_ORDER_HAS_EXPIRED_ERROR_VALUE = "订单已失效";

    public static final int SYS_QCCODE_HAS_USERED_ERROR = 2005;
    public static final String SYS_QCCODE_HAS_USERED_ERROR_VALUE = "二维码已使用";

    public static final int SYS_PEAK_PURCHASE_LIMITATION_ERROR = 2006;
    public static final String SYS_PEAK_PURCHASE_LIMITATION_ERROR_VALUE = "购票失败，还有未出行的订单";

    public static final int SYS_PEAK_PURCHASE_LIMITONE_ERROR = 2007;
    public static final String SYS_PEAK_PURCHASE_LIMITONE_ERROR_VALUE = "购票失败，高峰限制一张";

    public static final int PLEASE_ENTER_THE_CORRECT_VERIFICATION_CODE = 2008;
    public static final String PLEASE_ENTER_THE_CORRECT_VERIFICATION_CODE_VALUE = "请输入正确的验证码";

    public static final int PLEASE_TRY_AGAIN_LATER = 2009;
    public static final String PLEASE_TRY_AGAIN_LATER_VALUE = "未查询到最近的车";


    public static final int SYS_REQUIRED_FAILURE = -1;
    public static final String SYS_REQUIRED_FAILURE_VALUE = "请求失败";





    ////////////////////////////////业务常量//////////////////////////////////
    public static final String BUS_MEMBER_CODE_VALUE = "m_";
    public static final String BUS_TAG_TOKEN = "token";

    public static final int BUS_MEMBER_LOGIN_TIMEOUT = 2001;
    public static final String BUS_MEMBER_LOGIN_TIMEOUT_VALUE = "token异常";


    public static final int BUS_DRIVER_IS_REST = 2002;
    public static final String BUS_DRIVER_IS_REST_VALUE = "司机已开启休息";

    public static final int BUS_USER_PASSWORD_ERROR = 6001;
    public static final String BUS_USER_PASSWORD_ERROR_VALUE = "密码错误";

    public static final int USER_NOT_REGISTERED = 7006;
    public static final String USER_NOT_REGISTERED_VALUE = "用户未注册";

}
