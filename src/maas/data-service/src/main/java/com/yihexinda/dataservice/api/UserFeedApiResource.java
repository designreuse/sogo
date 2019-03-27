package com.yihexinda.dataservice.api;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.ExcelXUtils;
import com.yihexinda.data.dto.TDriverDto;
import com.yihexinda.dataservice.service.TDriverService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TUserFeedDto;
import com.yihexinda.dataservice.service.TUserFeedService;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhanglei
 * @version 1.0
 * @date 2018/11/28 0028
 */
@RestController
@RequestMapping("/userFeed/client")
public class UserFeedApiResource {

    /**
     * 反馈服务
     */
    @Autowired
    private TUserFeedService userFeedService;

    /**
     * 司机服务
     */
    @Autowired
    private TDriverService driverService;

    /**
     * 添加反馈
     *
     * @param userFeedDto 反馈信息
     * @return ResultVo
     */
    @PostMapping("/addUserFeed")
    public ResultVo addUserFeed(@RequestBody TUserFeedDto userFeedDto) {
        boolean save = userFeedService.save(userFeedDto);
        if (save) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 删除反馈
     *
     * @param userFeddId
     * @return
     */
    @GetMapping("/deleteUserFeed/{id}")
    public ResultVo deleteUserFeed(@PathVariable(value = "id") int userFeddId) {
        TUserFeedDto userFeedDto = userFeedService.getById(userFeddId);
        if (null != userFeedDto) {
            boolean success = userFeedService.updateById(userFeedDto);
            if (success) {
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 修改
     *
     * @param tUserFeedDto
     * @return
     */
    @PostMapping("/updateUserFeed")
    public ResultVo updateUserFeed(@RequestBody TUserFeedDto tUserFeedDto) {
        tUserFeedDto.setUpdateDate(new Date());
        boolean success = userFeedService.saveOrUpdate(tUserFeedDto);
        if (success) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * 查询反馈列表
     *
     * @param condition 参数
     * @return 反馈列表
     */
    @PostMapping("/userFeedList")
    public ResultVo userFeedList(@RequestBody Map<String, Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
        //开始时间与结束时间不为空
        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
            String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            condition.put("aStartTime", aStartTime);
            condition.put("aEndTime", aEndTime);
        }
        ResultVo resultVo = new AbstractPageTemplate<TUserFeedDto>() {
            @Override
            protected List<TUserFeedDto> executeSql() {
                List<TUserFeedDto> list = userFeedService.getUserFeedList(condition);
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
    }


    /**
     * yhs
     * 提交司机用户反馈
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/addDriverFeed", method = RequestMethod.POST)
    public ResultVo addDriverFeed(@RequestBody Map<String, Object> condition) {
        //根据司机id查询司机列表
        String driverId = StringUtil.trim(condition.get("userId"));
        String feedContent = StringUtil.trim(condition.get("feedContent"));
        TDriverDto driverDto = driverService.getById(driverId);
        //判断是否能查询到数据
        if (driverDto != null) {
            //组装司机反馈列表
            TUserFeedDto userFeed = new TUserFeedDto();
            userFeed.setUserId(driverId);
            //用户类型(0 小程序  1司机用户)
            userFeed.setUserType("1");
            userFeed.setFeedContent(feedContent);
            userFeed.setMobile(driverDto.getTelephone());
            userFeed.setCreateId(driverId);
            userFeed.setCreateDate(new Date());
            //保存反馈数据
            boolean success = userFeedService.save(userFeed);
            if (success) {
                return ResultVo.success().setMessage("提交反馈成功");
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);

    }

    /**
     * 返回导出Excel 数据
     */
    @GetMapping("/userFeedExcel")
    public List<TUserFeedDto> userFeedExcel() {
        return userFeedService.getUserFeedList(null);
    }
}
