package com.yihexinda.pcweb.interceptor;

import com.google.gson.Gson;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.EncryptionUtil;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.HeaderVo;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.pcweb.utils.RedisUtil;
import com.yihexinda.pcweb.utils.SpringUtil;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/11/28 0028
 */
public class SSOInterceptor implements HandlerInterceptor {

    public RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
    @Override
    @ResponseBody
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        // 处理handler;
        if (handler instanceof HandlerMethod) {
            // 判断当前method上是否有标签;
            HandlerMethod hm = (HandlerMethod) handler;
            if (hm.getMethodAnnotation(NoRequireLogin.class) == null) {
                ResultVo resultVo = new ResultVo();
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (parameterMap == null || parameterMap.isEmpty()) {
                    MyRequestWrapper myRequestWrapper = new MyRequestWrapper((HttpServletRequest) request);
                    String body = myRequestWrapper.getBody();
                    if(StringUtil.isEmpty(body)){
                        resultVo.setResult(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT);
                        resultVo.setMessage(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT_VALUE);
                        response.getWriter().write(JsonUtil.toJson(resultVo));
                        return false;
                    }
                    Map<Object, Object> objectObjectMap = JsonUtil.parseJSON2Map(body);
                    String token = StringUtil.trim(objectObjectMap.get("token"));
                    if(StringUtil.isEmpty(token)){//token错误
                        resultVo.setResult(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT);
                        resultVo.setMessage(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT_VALUE);
                        response.getWriter().write(JsonUtil.toJson(resultVo));
                        return false;
                    }
                    String[] params = token.split("\\.");
                    if (params.length != 3) {
                        resultVo.setResult(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT);
                        resultVo.setMessage(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT_VALUE);
                        response.getWriter().write(JsonUtil.toJson(resultVo));
                        return false;
                    }
                    PayLoadVo payLoadVo = JsonUtil.jsonToObjectT(EncryptionUtil.getFromBase64(params[1]), PayLoadVo.class);
                    String signature = EncryptionUtil.getFromBase64(params[2]);
                    if (payLoadVo == null || StringUtil.isEmpty(signature)) {
                        resultVo.setResult(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT);
                        resultVo.setMessage(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT_VALUE);
                        response.getWriter().write(JsonUtil.toJson(resultVo));
                        return false;
                    }
                    String rdsSignature = redisUtil.get(payLoadVo.getUserId());
                    if (StringUtil.isEmpty(rdsSignature) || !rdsSignature.equals(signature)) {
                        resultVo.setResult(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT);
                        resultVo.setMessage(ResultConstant.BUS_MEMBER_LOGIN_TIMEOUT_VALUE);
                        response.getWriter().write(JsonUtil.toJson(resultVo));

                        //后期可以注释掉
//                        String content = EncryptionUtil.getBase64(JsonUtil.toJson(new HeaderVo())).replaceAll("\r|\n", "") + "." + EncryptionUtil.getBase64(new Gson().toJson(payLoadVo)).replaceAll("\r|\n", "");
//                        String _signature = EncryptionUtil.getSHA256StrJava(ResultConstant.BUS_TAG_TOKEN + content);
//                        redisUtil.set(payLoadVo.getUserId(),_signature,(long)60*60*24*7);

                        return false;
                    } else {
                        redisUtil.set(payLoadVo.getUserId(), rdsSignature, (long) 86400);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
