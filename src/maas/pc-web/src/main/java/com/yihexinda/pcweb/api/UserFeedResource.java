package com.yihexinda.pcweb.api;


import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.ExcelXUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TUserFeedDto;
import com.yihexinda.pcweb.client.UserFeedClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/08
 */
@Api(description = "用户反馈接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class UserFeedResource {

    @Autowired
    private UserFeedClient userFeedClient;

    /**
     *  查询用户反馈列表
     * @param condition  map参数
     * @return 反馈信息列表
     */
    @ApiOperation(value = "查询用户反馈列表", httpMethod = "POST")
    @RequestMapping(value = "/userFeedList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "beginDate", value = "开始时间", required = true, paramType = "string"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true, paramType = "string")
    })
    public ResultVo userFeedList(@RequestBody Map<String,Object> condition) {
        return userFeedClient.userFeedList(condition);
    }

    /**
     *  导出反馈列表Excel
     * @param response HttpServletResponse
     */
    @ApiOperation(value = "查询用户反馈列表", httpMethod = "GET")
    @RequestMapping(value = "/userFeedExcel")
    @NoRequireLogin
    public void userFeedExcel(HttpServletResponse response) {
        try {
            String fileName = "反馈列表.xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("Content-Type:application/vnd.ms-excel");
            String[] title = {"用户名称", "手机号", "反馈内容", "反馈时间"};
            List<TUserFeedDto> tUserFeedDtos = userFeedClient.userFeedExcel();
            String[][] objects = new String[tUserFeedDtos.size()]
                    [tUserFeedDtos.get(0).toString().split(",").length];
            for (int i = 0; i < tUserFeedDtos.size(); i++) {
                TUserFeedDto tUserFeedDto = tUserFeedDtos.get(i);
                objects[i][0] = tUserFeedDto.getUserName();
                objects[i][1] = tUserFeedDto.getMobile();
                objects[i][2] = tUserFeedDto.getFeedContent();
                if ("".equals(StringUtil.trim(tUserFeedDto.getCreateDate()))){
                    objects[i][3] ="";
                }else{
                    objects[i][3] =  DateUtils.formatDate(tUserFeedDto.getCreateDate(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
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
