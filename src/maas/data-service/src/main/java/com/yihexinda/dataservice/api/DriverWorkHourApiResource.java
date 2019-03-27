package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverWorkHourDto;
import com.yihexinda.dataservice.service.TDriverWorkHourService;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yhs
 * @version 1.0
 * @date 2018/11/28 0028
 */

@RestController
@RequestMapping("/driverWorkHour/client")
@Slf4j
public class DriverWorkHourApiResource {

    @Autowired
    private TDriverWorkHourService driverWorkHourService;

    /**
     * 根据司机Id查询司机工时
     * column comment
     *
     * @param paramter
     * @return
     */
    @PostMapping(value = "/getDriverWorkHour")
    public ResultVo getDriverWorkHour(@RequestBody Map<String, String> paramter) {
        String driverId = StringUtil.trim(paramter.get("driverId"));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("driver_id", driverId);
        //必须是使用的工时
        queryWrapper.eq("status", "1");
        TDriverWorkHourDto driverWorkHourDto =  driverWorkHourService.getOne(queryWrapper);
        if (driverWorkHourDto!=null) {
                return ResultVo.success().setDataSet(driverWorkHourDto);
            }

        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

}
