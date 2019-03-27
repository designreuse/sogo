package com.yihexinda.dataservice.api;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.HeaderVo;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.dataservice.dao.TDriverCarBindDao;
import com.yihexinda.dataservice.service.*;
import com.yihexinda.dataservice.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author yhs
 * @version 1.0
 * @date 2018/11/28 0028
 */

@RestController
@RequestMapping("/driver/client")
@Slf4j
public class DriverApiResource {

    @Autowired
    private TDriverService driverService;
    @Autowired
    private TDriverCarBindService driverCarBindService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private TDriverWorkHourService tDriverWorkHourService;
    @Resource
    private TScheduleWorkService tScheduleWorkService;
    @Resource
    private TWorkHourService tWorkHourService;
    @Resource
    private RegionsApiResource regionsApiResource;
    @Autowired
    private TRouteOffpeakService routeOffpeakService;
    @Autowired
    private TRoutePeakService routePeakService;
    @Autowired
    private TOrderService orderService;
    @Autowired
    private TLineService lineService;
    @Autowired
    private TLineDriverBindService lineDriverBindService;

    /**
     * pengFeng
     * 新增司机信息
     *
     * @param tDriverDto 司机dto
     * @return ResultVo
     */
    @PostMapping("/addDriverInfo")
    public ResultVo addDriverInfo(@RequestBody TDriverDto tDriverDto) {
        if (driverService.addDriverInfo(tDriverDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * pengFeng
     * 修改司机信息
     *
     * @param tDriverDto 司机dto
     * @return ResultVo
     */
    @PutMapping("/updateDriverInfo")
    public ResultVo updateDriverInfo(@RequestBody TDriverDto tDriverDto) {
        tDriverDto.setUpdateDate(new Date());
        if (driverService.updateDriverInfo(tDriverDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * pengFeng
     * 批量排班
     *
     * @param driverDtoList 司机list
     * @return ResultVo
     */
    @PostMapping("/batchScheduling")
    public ResultVo batchScheduling(@RequestBody Map<String, Object> driverDtoList) {

        List<TDriverDto> tDriverDtoList = JSONObject.parseArray(JsonUtil.toJson(driverDtoList.get("driverDtoList")), TDriverDto.class);

        if (driverService.batchScheduling(tDriverDtoList, String.valueOf(driverDtoList.get("scheduleId")))) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * pengFeng
     * 获取司机信息列表
     *
     * @return 司机信息列表
     */
    @PostMapping("/getDriverList")
    public ResultVo getDriverList(@RequestBody Map<String, Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
            String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            condition.put("aStartTime", aStartTime);
            condition.put("aEndTime", aEndTime);
        }

        ResultVo resultVo = new AbstractPageTemplate<TDriverDto>() {
            @Override
            protected List<TDriverDto> executeSql() {
                List<TDriverDto> list = driverService.getDriverList(condition);
                for (TDriverDto tDriverDto : list) {
                    if (StringUtil.isNotEmpty(tDriverDto.getAreaPath())) {
                        tDriverDto.setRegionAddress(regionsApiResource.regionAddress(tDriverDto.getAreaPath()));
                    }
                }
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
        //eturn ResultVo.success().setDataSet(tDriverService.getDriverList());
    }


    /**
     * pengFeng
     * 根据id获取司机信息（返回所有参数）
     *
     * @param id 司机主键id
     * @return TDriverDto 司机信息
     */
    @GetMapping("/getDriverInfo/{id}")
    public ResultVo getDriverInfo(@PathVariable String id) {
        TDriverDto tDriverDto = driverService.getDriverInfo(id);
        if (null == tDriverDto) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, ResultConstant.SYS_REQUIRED_DATA_ERROR_VALUE);
        }
        return ResultVo.success().setDataSet(tDriverDto);
    }

    /**
     * pengFeng
     * 根据id获取司机信息(不返回密码等操作)
     *
     * @param id 司机主键id
     * @return TDriverDto 司机信息
     */
    @GetMapping("/getDriver/{id}")
    public ResultVo getDriver(@PathVariable String id) {
        TDriverDto tDriverDto = driverService.getById(id);
        if (null == tDriverDto) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, ResultConstant.SYS_REQUIRED_DATA_ERROR_VALUE);
        }
        tDriverDto.setPassword("");
        return ResultVo.success().setDataSet(tDriverDto);
    }


    /**
     * ysh
     * 司机登录
     *
     * @param condition
     */
    @RequestMapping(value = "/loginDriver", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public ResultVo loginDriver(@RequestBody Map<String, Object> condition) {
        //工号，密码，车辆不能为空
       // if ("".equals(StringUtil.trim(condition.get("no"))) || "".equals(StringUtil.trim(condition.get("password"))) || "".equals(StringUtil.trim(condition.get("carId")))) {
        if ("".equals(StringUtil.trim(condition.get("no"))) || "".equals(StringUtil.trim(condition.get("password"))) ) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, ResultConstant.SYS_REQUIRED_DATA_ERROR_VALUE);
        }
        QueryWrapper<TDriverDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("no", condition.get("no"));
        queryWrapper.eq("password", new Md5().getMD5ofStr(StringUtil.trim(condition.get("password"))));
        //注释原因，因需求变动，如司机停用，需要给停用状态
       // queryWrapper.eq("status", "1");
        //查询司机
        TDriverDto driverDto = driverService.getOne(queryWrapper);
        if (driverDto != null) {
            if ("0".equals(driverDto.getStatus())){
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"该司机已停用");
            }
            return ResultVo.success().setDataSet(createToken(driverDto));
            //根据车俩id查询该车是否绑定
           /* String carId = StringUtil.trim(condition.get("carId"));
            QueryWrapper<TDriverCarBindDto> queryCarBindWrapper = new QueryWrapper<>();
            //绑定状态( 0未绑定  1已绑定  2注销 );
            queryCarBindWrapper.eq("bind_status", "1");
            queryCarBindWrapper.eq("car_id", carId);
            TDriverCarBindDto carBindDto = driverCarBindService.getOne(queryCarBindWrapper);
            //已绑定，判断为退出再次登录。
            if (carBindDto != null) {
                return ResultVo.success().setDataSet(createToken(driverDto));
            }
            //关联绑定车辆
            TDriverCarBindDto driverCarBindDto = new TDriverCarBindDto();
            driverCarBindDto.setDriverId(driverDto.getId());
            driverCarBindDto.setCarId(StringUtil.trim(condition.get("carId")));
            driverCarBindDto.setBindStatus("1");
            //用司机id取带
            driverCarBindDto.setCreateId(driverDto.getId());
            //保存到数据库
            boolean success = driverCarBindService.save(driverCarBindDto);
            if (success) {
                return ResultVo.success().setDataSet(createToken(driverDto));
            }*/

        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, "未查询到该司机信息");
    }

    /**
     * 司机退出
     *
     * @param driverId 司机id
     * @return ResultVo
     */
    @RequestMapping(value = "/loginOutDriver/{driverId}", method = RequestMethod.GET)
    public ResultVo loginOutDriver(@PathVariable String driverId) {
        if (StringUtil.isNotEmpty(driverId)) {
            if (driverService.loginOutDriver(driverId)) {
                return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


    /**
     * ysh
     * 发送短信
     *
     * @param phone 手机号
     * @return ResultVo
     */
    @RequestMapping(value = "/addSmsLog", method = RequestMethod.POST)
    public ResultVo addSmsLog(@RequestParam(value = "phone") String phone) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("telephone", phone);
        queryWrapper.eq("status", "1");
        //查询该手机号是否存在
        TDriverDto driverDto = driverService.getOne(queryWrapper);
        if (driverDto != null) {
            return ResultVo.success();
        }

        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "该手机号未注册，请检查手机号");
    }

    private ResultVo createToken(@RequestBody TDriverDto tDriverDto) {
        PayLoadVo payLoadVo = new PayLoadVo();
        HeaderVo headerVo = new HeaderVo();
        Date date = new Date();
        payLoadVo.setUserId(tDriverDto.getId());
        payLoadVo.setExp(date);
        payLoadVo.setNick(tDriverDto.getName());
        payLoadVo.setPhone(tDriverDto.getTelephone());
        payLoadVo.setLoginLogo("driverLogo");
        String content = EncryptionUtil.getBase64(JsonUtil.toJson(headerVo)).replaceAll("\r|\n", "") + "." + EncryptionUtil.getBase64(new Gson().toJson(payLoadVo)).replaceAll("\r|\n", "");
        String signature = EncryptionUtil.getSHA256StrJava(ResultConstant.BUS_TAG_TOKEN + content);
        redisUtil.set(tDriverDto.getId(), signature, (long) 60 * 60 * 24 * 7);
        return ResultVo.success().setToken(content + "." + EncryptionUtil.getBase64(signature).replaceAll("\r|\n", ""));
    }

    /**
     * ysh
     * 设置密码
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/setPasswordDriver", method = RequestMethod.POST)
    public ResultVo setPasswordDriver(@RequestBody Map<String, Object> params) {
        //获得请求参数
        String phone = StringUtil.trim(params.get("phone"));
        String password = StringUtil.trim(params.get("password"));
        //先根据phone进行条件查询
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("telephone", phone);
        TDriverDto driverDto = driverService.getOne(queryWrapper);
        if (driverDto != null) {
            //修改密码
            driverDto.setPassword(new Md5().getMD5ofStr(password));
            //更新操作
            boolean success = driverService.updateById(driverDto);
            if (success) {
                return ResultVo.success();
            }
        }

        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 查询司机历史车辆
     *
     * @param driverId 司机id
     * @return ResultVo
     */
    @RequestMapping(value = "/getDriverBindCarList/{driverId}", method = RequestMethod.GET)
    public ResultVo setPasswordDriver(@PathVariable String driverId) {
        if (StringUtil.isEmpty(driverId)) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, "司机id为空");
        }
        QueryWrapper<TDriverCarBindDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id", driverId);
        return ResultVo.success().setDataSet(driverCarBindService.list(queryWrapper));
    }


    /**
     * 查询该司机当前是处于上班时间
     *
     * @param driverId 司机id
     * @return ResultVo
     */
    @GetMapping("/indexDriver/{driverId}")
    public ResultVo indexDriver(@PathVariable("driverId") String driverId) {
        if (StringUtil.isEmpty(driverId)) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_DATA_ERROR, "司机id为空");
        }
        String type = "5";
        QueryWrapper<TDriverWorkHourDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id", driverId);
        //查询司机的排班与工时信息
        List<TDriverWorkHourDto> driverWorkHourDtoList = tDriverWorkHourService.list(queryWrapper);
        //格式化当前年月日   ，待优化
        String dateYear = DateUtils.formatDate(new Date(), DateUtils.PATTERN_yyyy_MM_dd);
        String dateHours = DateUtils.formatDate(new Date(), DateUtils.PATTERN_HH_mm_ss);
        SimpleDateFormat formatYear = new SimpleDateFormat(DateUtils.PATTERN_yyyy_MM_dd);
        SimpleDateFormat formatHours = new SimpleDateFormat(DateUtils.PATTERN_HH_mm_ss);
        Long currentTime = 1L;
        Long currentDate = 1L;
        try {
            currentTime = formatHours.parse(dateHours).getTime();
            currentDate = formatYear.parse(dateYear).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //查看是司机否有排班
        if (driverWorkHourDtoList.size() > 0) {

            //检查司机是否上班
            TDriverDto query = driverService.getById(driverId);
            //司机状态(0 停用 1启用 2休息)
            String status = query.getStatus();
            if(status.equals("2")){
                type = "5";
                return ResultVo.success().setMessage("该司机已设置休息").setDataSet(type);
            }
            String scheduleId = driverWorkHourDtoList.get(0).getScheduleId();
            if (StringUtil.isEmpty(scheduleId)) {
                type = "3";
                return ResultVo.success().setMessage("该司机没有设置排班").setDataSet(type);
            }
            //查询排班信息
            QueryWrapper<TScheduleWorkDto> scheduleWorkDtoQueryWrapper = new QueryWrapper<>();
            scheduleWorkDtoQueryWrapper.eq("status", "1");
            scheduleWorkDtoQueryWrapper.eq("id", scheduleId);
            TScheduleWorkDto tScheduleWorkDto = tScheduleWorkService.getOne(scheduleWorkDtoQueryWrapper);
            if (null == tScheduleWorkDto) {
                type = "3";
                return ResultVo.success().setMessage("该司机没有有效排班").setDataSet(type);
            }
            if (StringUtil.isNotEmpty(tScheduleWorkDto.getHolidays())) {
                //今天在休息时间
                if (tScheduleWorkDto.getHolidays().contains(dateYear)) {
                    type = "2";
                    return ResultVo.success().setMessage("该司机今天休息").setDataSet(type);
                }
            }
            //司机是否处于上班
            if (!(tScheduleWorkDto.getStartDate().getTime() < currentDate && currentDate < tScheduleWorkDto.getEndDate().getTime())) {
                type = "4";
                return ResultVo.success().setMessage("该司机今天不处于上班时间").setDataSet(type);
            }
            //查询司机个人工时， 看当前时间是否处于上班时间
            queryWrapper.eq("status", "1");
            List<TDriverWorkHourDto> tDriverWorkHourDtoList = tDriverWorkHourService.list(queryWrapper);
            for (int i = 0; i < tDriverWorkHourDtoList.size(); i++) {
                //启用状态时间
                if ("1".equals(tDriverWorkHourDtoList.get(i).getStatus())) {
                    Long startTime = tDriverWorkHourDtoList.get(i).getStartTime().getTime();
                    Long endTime = tDriverWorkHourDtoList.get(i).getEndTime().getTime();
                    if (startTime < currentTime && currentTime < endTime) {
                        type = "1";
                        return ResultVo.success().setMessage("上班状态").setDataSet(type);
                    }
                }
                if (i == tDriverWorkHourDtoList.size()) {
                    return ResultVo.success().setMessage("该司机处于休息时间,具有个人工时").setDataSet(type);
                }

            }
            QueryWrapper<TWorkHourDto> workHourDtoQueryWrapper = new QueryWrapper<>();
            workHourDtoQueryWrapper.eq("schedule_id", scheduleId);
            workHourDtoQueryWrapper.eq("status", "1");
            List<TWorkHourDto> tWorkHourDtoList = tWorkHourService.list(workHourDtoQueryWrapper);
            if (tWorkHourDtoList.size() > 0) {
                for (TWorkHourDto tWorkHourDto : tWorkHourDtoList) {
                    Long startTime = tWorkHourDto.getStartTime().getTime();
                    Long endTime = tWorkHourDto.getEndTime().getTime();
                    if (startTime < currentTime && currentTime < endTime) {
                        type = "1";
                        return ResultVo.success().setMessage("上班状态").setDataSet(type);
                    }
                }
            }
        } else {
            type = "3";
            return ResultVo.success().setMessage("该司机没有设置排班").setDataSet(type);
        }
        return ResultVo.success().setMessage("该司机处于休息时间").setDataSet(type);
    }


    /**
     * 司机退出登录
     * @author chenzeqi
     * @param data
     * @return
     */
    @RequestMapping("driverLogout")
    public ResultVo driverLogout(@RequestBody Map<String,Object> data){
        return driverService.driverLogout(data);
    }


    /**
     * 司机是否设置休息
     * @author wenbn
     * @param data
     * @return
     */
    @PostMapping("checkDriverStatus")
    ResultVo checkDriverStatus(@RequestBody Map<String, Object> data){
        String driverId = StringUtil.trim(data.get("driverId"));
        TDriverDto query = driverService.getById(driverId);
        ResultVo success = ResultVo.success();
        if(query != null){
            //司机状态(0 停用 1启用 2休息)
            String status = query.getStatus();
            success.setDataSet(status);
        }
        return success;
    }

    /**
     * 系统自动完成行程
     * @author wenbn
     * @return
     */
    @PostMapping("resetRoute")
    ResultVo resetRoute(@RequestBody Map<String, Object> data){
        Date nowTime = new Date();
        String type = StringUtil.trim(data.get("type"));
        if(StringUtil.isEmpty(type)){
            //结束高峰行程
            List<Map<String, Object>> offpeaks = routeOffpeakService.listMaps(new QueryWrapper<TRouteOffpeakDto>()
                    .select("id")
                    .eq("route_state","0")
            );
            if(offpeaks!= null && offpeaks.size() >0){
                List<TRouteOffpeakDto> routeOffPeakList = Lists.newArrayListWithCapacity(offpeaks.size());
                List<String> ids = Lists.newArrayList();
                //修改行程状态
                offpeaks.forEach(offpeak ->{
                    TRouteOffpeakDto offpeakDto = new TRouteOffpeakDto();
                    String id = StringUtil.trim(offpeak.get("id"));
                    offpeakDto.setId(id);
                    ids.add(id);
                    offpeakDto.setRouteState("1");
                    offpeakDto.setUpdateDate(nowTime);
                    routeOffPeakList.add(offpeakDto);
                });

                boolean b = routeOffpeakService.updateBatchById(routeOffPeakList);
                if(b){
                    //解除司机与车辆的绑定关系
                    List<Map<String, Object>> lineList = lineService.listMaps(new QueryWrapper<TLineDto>()
                            .select("id")
                            .eq("line_state", "1")
                    );

                    if(lineList != null && lineList.size()>0){
                        List<String> lines  = Lists.newArrayList();
                        for (Map<String, Object> line : lineList) {
                            lines.add(StringUtil.trim(line.get("id")));
                        }
                        List<Map<String, Object>> bindLineList = lineDriverBindService.listMaps(new QueryWrapper<TLineDriverBindDto>()
                                .select("driver_id")
                                .in("line_id")
                        );
                        if(bindLineList!= null && bindLineList.size()>0){
                            List<String> driverIds = Lists.newArrayList();
                            for (Map<String, Object> bindLine : bindLineList) {
                                driverIds.add(StringUtil.trim(bindLine.get("driver_id")));
                            }

                            List<Map<String, Object>> driverBindCarList = driverCarBindService.listMaps(new QueryWrapper<TDriverCarBindDto>()
                                    .select("id")
                                    .eq("bind_status","1")
                                    .in("driver_id", driverIds)
                            );
                            if(driverBindCarList!= null && driverBindCarList.size()>0){
                                List<TDriverCarBindDto> list = Lists.newArrayList();
                                for (Map<String, Object> map : driverBindCarList) {
                                    TDriverCarBindDto driverCarBindDto = new TDriverCarBindDto();
                                    driverCarBindDto.setId(StringUtil.trim(map.get("id")));
                                    driverCarBindDto.setUpdateDate(nowTime);
                                    driverCarBindDto.setBindStatus("2");
                                    list.add(driverCarBindDto);
                                }
                                driverCarBindService.updateBatchById(list);
                            }
                        }
                    }

                    log.info("修改平峰行程成功....");

                    log.info("修改订单状态");
                    List<Map<String, Object>> orderList = orderService.listMaps(new QueryWrapper<TOrderDto>()
                            .select("id", "order_status","ride_end_date")
                            .eq("order_status", "2")
                            .in("route_id", ids)
                    );
                    if(orderList!= null && orderList.size()>0){
                        List<TOrderDto> orders = Lists.newArrayList();
                        for (Map<String, Object> order : orderList) {
                            TOrderDto update = new TOrderDto();
                            update.setId(StringUtil.trim(order.get("id")));
                            String ride_end_date = StringUtil.trim(order.get("ride_end_date"));
                            if(StringUtil.isEmpty(ride_end_date)){
                                update.setRideEndDate(nowTime);
                            }
                            update.setUpdateDate(nowTime);
                            orders.add(update);
                        }
                        orderService.updateBatchById(orders);
                    }
                }else{
                    log.info("修改平峰行程失败....");
                }
            }
        }else if(type.equals("1")){
            //修改行程数据
            List<Map<String, Object>> peaks = routePeakService.listMaps(new QueryWrapper<TRoutePeakDto>()
                    .select("id")
                    .eq("route_state","0")
            );
            if(peaks!= null && peaks.size() >0){
                List<TRoutePeakDto> routePeakList = Lists.newArrayListWithCapacity(peaks.size());
                List<String> ids = Lists.newArrayList();
                //修改行程状态
                peaks.forEach(peak ->{
                    TRoutePeakDto peakDto = new TRoutePeakDto();
                    String id = StringUtil.trim(peak.get("id"));
                    peakDto.setId(id);
                    ids.add(id);
                    peakDto.setRouteState("1");
                    peakDto.setUpdateDate(nowTime);
                    routePeakList.add(peakDto);
                });

                boolean b = routePeakService.updateBatchById(routePeakList);
                if(b){
                    log.info("修改高峰行程成功....");

                    log.info("修改订单状态");
                    List<Map<String, Object>> orderList = orderService.listMaps(new QueryWrapper<TOrderDto>()
                            .select("id", "order_status","ride_end_date")
                            .eq("order_status", "2")
                            .in("route_id", ids)
                    );
                    if(orderList!= null && orderList.size()>0){
                        List<TOrderDto> orders = Lists.newArrayList();
                        for (Map<String, Object> order : orderList) {
                            TOrderDto update = new TOrderDto();
                            update.setId(StringUtil.trim(order.get("id")));
                            String ride_end_date = StringUtil.trim(order.get("ride_end_date"));
                            if(StringUtil.isEmpty(ride_end_date)){
                                update.setRideEndDate(nowTime);
                            }
                            update.setUpdateDate(nowTime);
                            orders.add(update);
                        }
                        orderService.updateBatchById(orders);
                    }
                }else{
                    log.info("修改高峰行程失败....");
                }
            }
        }
        return ResultVo.success();
    }


    /**
     * 检查手机号是否存在
     * @param phone
     * @retur
     */
    @PostMapping("checkExistsPhone")
    ResultVo checkExistsPhone(@RequestParam(value ="phone" ) String phone){
        if(StringUtil.isEmpty(phone)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        Map<String, Object> map = driverService.getMap(new QueryWrapper<TDriverDto>()
                .eq("telephone", phone)
        );
        if(map == null){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"手机号不存在");
        }
        return ResultVo.success();
    }

    /**
     * 司机查询状态(用于批量排班)
     * @param data 查询参数
     * @return ResultVo
     */
    @PostMapping("getDriverListStatus")
    public ResultVo getDriverListStatus(@RequestBody Map<String, Object> data){
        return ResultVo.success().setDataSet(driverService.getDriverList(data));
    }

    /**
     * 司机导出查询
     * @return ResultVo
     */
    @GetMapping("getDriverExcel")
    public List<TDriverDto> getDriverExcel(){
        List<TDriverDto> driverDtoList= driverService.getDriverList(null);
        for (TDriverDto tDriverDto : driverDtoList) {
            if (StringUtil.isNotEmpty(tDriverDto.getAreaPath())) {
                tDriverDto.setRegionAddress(regionsApiResource.regionAddress(tDriverDto.getAreaPath()));
            }
        }
        return driverDtoList;
    }

    /**
     *  修改司机状态
     * 修改司机个体信息（只修改主表）
     * @param tDriverDto 司机
     * @return ResultVo
     */
    @PostMapping("updateDriverStatus")
    public ResultVo updateDriverStatus(@RequestBody TDriverDto tDriverDto){
        if (StringUtil.isEmpty(tDriverDto.getId())||StringUtil.isEmpty(tDriverDto.getStatus())){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"参数缺少");
        }
        tDriverDto.setUpdateDate(new Date());
        if (driverService.updateById(tDriverDto)){
            return ResultVo.success().setDataSet(driverService.updateById(tDriverDto));
        }
        return  ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 开启早高峰
     * @param
     * @return
     */
    @PostMapping("openMorningPeak")
    ResultVo openMorningPeak(){
        redisUtil.set("SYS_PEAK_TIME_RANGE","7:30-10:00",3600*8l);
        redisUtil.set("SYS_PEAK_LINES","s3,s4",3600*8l);//高峰对应的线路
        //开启s1
        List<TLineDto> lines = Lists.newArrayList();
        TLineDto s1 = new TLineDto();
        s1.setId("s1");
        s1.setLineState("0");
        lines.add(s1);
        //开启s2
        TLineDto s2 = new TLineDto();
        s2.setId("s2");
        s2.setLineState("0");
        lines.add(s2);
        //关闭s2
        TLineDto s3 = new TLineDto();
        s3.setId("s3");
        s3.setLineState("1");
        lines.add(s3);

        TLineDto s4 = new TLineDto();
        s4.setId("s4");
        s4.setLineState("1");
        lines.add(s4);
        boolean b = lineService.updateBatchById(lines);
        log.info("开启早高峰,status = "+ b);


        //修改司机线路绑定
        List<TLineDriverBindDto> lineDrivers = Lists.newArrayList();
        //师傅1：s3
        TLineDriverBindDto driver1 = new TLineDriverBindDto();
        driver1.setId("ld1");
        driver1.setLineId("s3");
       //师傅4：s3
        TLineDriverBindDto driver4 = new TLineDriverBindDto();
        driver4.setId("ld4");
        driver4.setLineId("s3");
        lineDrivers.add(driver1);
        lineDrivers.add(driver4);
        boolean bb = lineDriverBindService.updateBatchById(lineDrivers);

        redisUtil.remove(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY);
        return ResultVo.success();
    }


    /**
     * 开启晚高峰高峰
     * @param
     * @return
     */
    @PostMapping("openEveningPeak")
    ResultVo openS1(){
        redisUtil.set("SYS_PEAK_TIME_RANGE","19:30-22:00",3600*8l);
        redisUtil.set("SYS_PEAK_LINES","s1,s2",3600*8l);//高峰对应的线路
        //开启s1
        List<TLineDto> lines = Lists.newArrayList();
        TLineDto s1 = new TLineDto();
        s1.setId("s1");
        s1.setLineState("1");
        lines.add(s1);
        //开启s2
        TLineDto s2 = new TLineDto();
        s2.setId("s2");
        s2.setLineState("1");
        lines.add(s2);
        //关闭s3
        TLineDto s3 = new TLineDto();
        s3.setId("s3");
        s3.setLineState("0");
        lines.add(s3);

        TLineDto s4 = new TLineDto();
        s4.setId("s4");
        s4.setLineState("0");
        lines.add(s4);

        boolean b = lineService.updateBatchById(lines);
        redisUtil.remove(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY);
        log.info("开启晚高峰,status = "+ b);

        //修改司机线路绑定
        List<TLineDriverBindDto> lineDrivers = Lists.newArrayList();
        //师傅1：s2
        TLineDriverBindDto driver1 = new TLineDriverBindDto();
        driver1.setId("ld1");
        driver1.setLineId("s2");
        //师傅4：s1
        TLineDriverBindDto driver4 = new TLineDriverBindDto();
        driver4.setId("ld4");
        driver4.setLineId("s1");
        lineDrivers.add(driver1);
        lineDrivers.add(driver4);
        boolean bb = lineDriverBindService.updateBatchById(lineDrivers);


        return ResultVo.success();
    }

}
