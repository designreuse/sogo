package com.yihexinda.core.utils;


import com.yihexinda.core.vo.PayLoadVo;

import java.net.URLDecoder;

public class RequestUtil {

    /**
     * 解析token
     * @param token
     * @return
     */
    public static PayLoadVo analysisToken(String token){
        PayLoadVo payLoadVo = null;
        try{
            token = URLDecoder.decode(token, "UTF-8");
            String[] params = token.split("\\.");
            if(params.length != 3){
                return null;
            }
            payLoadVo = JsonUtil.jsonToObjectT(EncryptionUtil.getFromBase64(params[1]),PayLoadVo.class);
            return payLoadVo;
        }catch ( Exception e){
            return null;
        }
    }

}
