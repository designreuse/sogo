package com.yihexinda.userweb.api;


import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.userweb.config.ConfigProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

@Api(description = "保存用户经纬度")
@RestController()
@RequestMapping("/api/userLocation")
public class SaveUserLocationFile {

    @Autowired
    private ConfigProperties configProperties;

    /**
     * 保存用户经纬度
     *
     * @param paramter map参数
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/saveUserLocation", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, paramType = "string"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, paramType = "string"),
    })
    @NoRequireLogin
    public ResultVo saveUserFile(@RequestBody Map<String, String> paramter, HttpServletRequest request) throws IOException {
        if (StringUtil.isEmpty(paramter.get("userId")) || StringUtil.isEmpty(paramter.get("longitude")) || StringUtil.isEmpty("latitude")) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }
        //创建文件夹
        File file = new File(configProperties.getSaveUserLocation());
        if (!file.exists()) {
            file.mkdir();
        }
        //输出txt文件
        String fileName = paramter.get("userId") + ".txt";
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file + "\\" + fileName, true)));
        out.write(paramter + "\r\n");
        out.close();
        return ResultVo.success();
    }


   /*  注释，获取经纬度生成txt
    @RequestMapping(value = "/savePosition", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, paramType = "string"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, paramType = "string"),
    })
    @NoRequireLogin
    public ResultVo savePosition(@RequestBody Map<String, String> paramter, HttpServletRequest request) throws IOException {
        if (StringUtil.isEmpty(paramter.get("userId")) || StringUtil.isEmpty(paramter.get("longitude")) || StringUtil.isEmpty("latitude")) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }
        Calendar date = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String name = format.format(date.getTime());

        //创建文件夹
        File file = new File("E:\\" + name);
        if(!file.exists()){
            file.mkdirs();
        }
        String content = paramter.get("longitude") + "，" + paramter.get("latitude");
        BufferedWriter out = null;
        String fileName = "001.txt";
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file+"\\"+fileName, true)));
            out.write(content+"\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResultVo.success();
    }*/

}
