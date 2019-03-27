package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCarDto;
import com.yihexinda.data.dto.TDriverCarBindDto;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.dataservice.service.TCarService;
import com.yihexinda.dataservice.service.TDriverCarBindService;
import com.yihexinda.dataservice.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;


/**
 * @version 1.0
 * @date 2018/11/30
 */
@RestController
@RequestMapping("/driverCarBind/client")
@Slf4j
public class DriverCarBindApiResource {

    /**
     * 司机绑定服务
     */
    @Autowired
    private TDriverCarBindService tDriverCarBindService;

    /**
     * 车辆服务
     */
    @Resource
    private TCarService tCarService;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 获取历史车辆列表
     *
     * @return 历史车辆列表
     */
    @GetMapping("/getList")
    public ResultVo getList() {
        return ResultVo.success().setDataSet(tDriverCarBindService.getList());
    }


    /**
     * 新增车辆绑定信息
     *
     * @param tDriverCarBindDto 司机车辆信息
     * @return ResultVo
     */
    @PostMapping("/add")
    public ResultVo add(@RequestBody TDriverCarBindDto tDriverCarBindDto) {
        String carId = tDriverCarBindDto.getCarId();
        String driverId = tDriverCarBindDto.getDriverId();
        if (StringUtil.isEmpty(driverId)||StringUtil.isEmpty(carId)) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR, ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }
        QueryWrapper<TDriverCarBindDto> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("bind_status","1");
        queryWrapper.eq("car_id", carId);
        if (null!=tDriverCarBindService.getOne(queryWrapper)){
            return ResultVo.error(10086, "该车辆已被绑定");
        }
        //解除当前司机绑定的车辆信息
        List<TDriverCarBindDto> list = tDriverCarBindService.list(new QueryWrapper<TDriverCarBindDto>()
                .select("id","driver_id","bind_status")
                .eq("bind_status", "1")
                .eq("driver_id", driverId)
        );
        Date nowTime = new Date();
        if(list != null && list.size()>0){
            list.forEach(carBind -> {
                carBind.setUpdateDate(nowTime);
                carBind.setBindStatus("2");
            });
            tDriverCarBindService.updateBatchById(list);
        }
//        tDriverCarBindService.updateStatus();

        if (tDriverCarBindService.save(tDriverCarBindDto)) {
            String date = DateUtils.formatDate(nowTime, DateUtils.PATTERN_yyyy_MM_dd);
            //将车辆与司机绑定的信息缓存
            redisUtil.set(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE+date+carId,driverId,RedisConstant.SYS_CACHE_KEY_TIME_ONE_DAY);
            redisUtil.set(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE+date+driverId,carId,RedisConstant.SYS_CACHE_KEY_TIME_ONE_DAY);
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);

    }

    /**
     * 解绑
     *
     * @param tDriverCarBindDto 绑定id
     * @return ResultVo
     */
    @PostMapping("/update")
    public ResultVo update(@RequestBody TDriverCarBindDto tDriverCarBindDto) {
        String driverId = tDriverCarBindDto.getDriverId();
        String carId = tDriverCarBindDto.getCarId();
        if (StringUtil.isEmpty(driverId)) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR, ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }
        tDriverCarBindDto = tDriverCarBindService.getDriverId(driverId);
        if (tDriverCarBindDto != null) {
            tDriverCarBindDto.setBindStatus("2");
            tDriverCarBindDto.setUpdateDate(new Date());
            if (tDriverCarBindService.updateById(tDriverCarBindDto)) {
                String date = DateUtils.formatDate(new Date(), DateUtils.PATTERN_yyyy_MM_dd);
                carId = redisUtil.get(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE + date + driverId);
                redisUtil.remove(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE+date+ carId);
                redisUtil.remove(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE+date+ driverId);
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * 获取司机历史绑定车辆
     *
     * @param condition 参数（driverId司机id）
     * @return 历史车辆绑定列表
     */
    @PostMapping("/getDriverCarBind")
    public ResultVo getDriverCarBind(@RequestBody Map<String, Object> condition) {
        if ("".equals(StringUtil.trim(condition.get("driverId")))){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"司机id不能为空");
        }
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
            String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            condition.put("aStartTime", aStartTime);
            condition.put("aEndTime", aEndTime);
        }

        ResultVo resultVo = new AbstractPageTemplate<TDriverCarBindDto>() {
            @Override
            protected List<TDriverCarBindDto> executeSql() {
                List<TDriverCarBindDto> list = tDriverCarBindService.getDriverCarBind(condition);
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
        //return ResultVo.success().setDataSet(tDriverCarBindService.getList());
    }

    /**
     * 获取司机登陆的时候所有绑定与未绑定车辆
     *
     * @return
     */
    @GetMapping(value = "/getCarAndBindStatusList")
    public ResultVo getCarAndBindStatusList() {
        List<TDriverCarBindDto> tDriverCarBindDto = tDriverCarBindService.getCarAndBindStatusList();
        return ResultVo.success().setDataSet(tDriverCarBindDto);
    }


    /**
     * 司机端查询当前绑定的车辆
     *
     * @param condition 司机id
     * @return 绑定车辆信息
     */
    @PostMapping(value = "/getBindCar")
    public ResultVo getBindCar(@RequestBody Map<String, Object> condition) {
        String driverId = StringUtil.trim(condition.get("driverId"));
        QueryWrapper<TDriverCarBindDto> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("driver_id",driverId);
        queryWrapper.eq("bind_status","1");
        TDriverCarBindDto carBind = tDriverCarBindService.getOne(queryWrapper);
        if (carBind != null) {
            return ResultVo.success().setDataSet(tCarService.getById(carBind.getCarId()));
        }
        return ResultVo.error(ResultConstant.SYS_APPOINTMENT_FAILURE,"未查询到绑定的车辆");
    }

    /**
     * 定时任务，当天结束，将所有司机车辆解绑
     */
    @Scheduled(cron = "59 59 23 * * ?")
    void updateStatus() {
        tDriverCarBindService.updateStatus();
    }


    /**
     * 司机添加绑定车辆信息
     *
     * @param condition 司机车辆信息
     * @return ResultVo
     */
    @PostMapping("/addDriver")
    public ResultVo addDriver(@RequestBody Map<String, Object> condition) {
        String driverId = StringUtil.trim(condition.get("driverId"));
        String carId = StringUtil.trim(condition.get("carId"));
        if (StringUtil.isEmpty(StringUtil.trim(driverId)) || StringUtil.isEmpty(StringUtil.trim(carId))) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        //组装数据
        TDriverCarBindDto driverCarBindDto = new TDriverCarBindDto();
        String id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        driverCarBindDto.setId(id);
        driverCarBindDto.setDriverId(driverId);
        driverCarBindDto.setCarId((String) condition.get("carId"));
        driverCarBindDto.setBindStatus("1");
        driverCarBindDto.setCreateDate(new Date());
        //用司机id取带
        driverCarBindDto.setCreateId(driverId);

        if (tDriverCarBindService.save(driverCarBindDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);

    }


    /**
     * 司机车辆解绑
     *
     * @param condition 绑定id
     * @return ResultVo
     */
    @PostMapping("/updateDriver")
    public ResultVo updateDriver(@RequestBody Map<String, Object> condition) {
        String driverId = StringUtil.trim(condition.get("driverId"));
        if (StringUtil.isEmpty(StringUtil.trim(driverId))) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("driver_id", driverId);
        TDriverCarBindDto driverCarBindDto = tDriverCarBindService.getOne(queryWrapper);
        if (driverCarBindDto != null) {
            driverCarBindDto.setBindStatus("2");
            //设置更新时间与id
            driverCarBindDto.setUpdateDate(new Date());
            driverCarBindDto.setUpdateId(driverId);
            boolean success = tDriverCarBindService.updateById(driverCarBindDto);
            if (success) {
                return ResultVo.success();
            }
        }

        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 获取司机历史绑定车辆(导出Excel)
     *
     * @param driverId 参数（driverId司机id）
     * @return 历史车辆绑定列表
     */
    @GetMapping("/getDriverCarBindExcel/{driverId}")
    public List<TDriverCarBindDto> getDriverCarBindExcel(@PathVariable String driverId) {
        Map map =new HashMap();
        map.put("driverId",driverId);
      return  tDriverCarBindService.getDriverCarBind(map);

    }
}
