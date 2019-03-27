package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TScheduleWorkDto;
import com.yihexinda.dataservice.service.TScheduleWorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author pengfeng
 * @version 1.0
 * @date 2018/12/5
 */
@RestController
@RequestMapping("/scheduleWork/client")
@Slf4j
public class ScheduleWorkApiResource {
    @Autowired
    private TScheduleWorkService tScheduleWorkService;


    /**
     * 获取排班列表
     * @param condition 参数
     * @return 排班列表
     */
    @PostMapping("/getScheduleWorkList")
    public ResultVo getScheduleWorkList(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")),1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")),10);
        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
        Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
        String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
        Timestamp aEndTime = Timestamp.valueOf(endTime);
        condition.put("aStartTime", aStartTime);
        condition.put("aEndTime", aEndTime);
        }

        ResultVo resultVo = new AbstractPageTemplate<TScheduleWorkDto>() {
            @Override
            protected List<TScheduleWorkDto> executeSql() {
                List<TScheduleWorkDto> list = tScheduleWorkService.getScheduleWorkList(condition);
                return list;
            }
        }.preparePageTemplate(pageIndex,pageSize);
        return resultVo;
       // return ResultVo.success().setDataSet(tScheduleWorkService.getScheduleWorkList());
    }



    /**
     * 新增排班信息
     *
     * @param tScheduleWorkDto 排班信息
     * @return ResultVo
     */
    @PostMapping("addScheduleWork")
    public ResultVo addScheduleWork(@RequestBody TScheduleWorkDto tScheduleWorkDto) {
        if (tScheduleWorkService.addWorkHour(tScheduleWorkDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);

    }

    /**
     * 修改排班信息
     *
     * @param tScheduleWorkDto 排班信息
     * @return ResultVo
     */
    @PutMapping("updateScheduleWork")
    public ResultVo updateScheduleWork(@RequestBody TScheduleWorkDto tScheduleWorkDto) {
        tScheduleWorkDto.setUpdateDate(new Date());
        if (tScheduleWorkService.updateWorkHour(tScheduleWorkDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 修改排版状态
     *
     * @param tScheduleWorkDto 排班状态
     * @return ResultVo
     */
    @PutMapping("updateScheduleWorkStatus")
    public ResultVo updateScheduleWorkStatus(@RequestBody TScheduleWorkDto tScheduleWorkDto) {
        if (tScheduleWorkService.updateById(tScheduleWorkDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * 查询排班列表（不分页）
     *
     * @return 排班列表
     */
    @GetMapping("/queryScheduleWorkList")
    public ResultVo queryScheduleWorkList() {
         return ResultVo.success().setDataSet(tScheduleWorkService.getScheduleWorkList(null));
    }


    /**
     * 查询排班列表（用于司机设置排班）
     *
     * @return 排班列表
     */
    @GetMapping("/queryScheduleList")
    public ResultVo queryScheduleList() {
        QueryWrapper<TScheduleWorkDto> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("status","1");
        return ResultVo.success().setDataSet(tScheduleWorkService.list(queryWrapper));
    }

}
