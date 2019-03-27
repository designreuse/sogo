package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.data.dto.TDriverCarBindDto;
import com.yihexinda.dataservice.service.TDriverCarBindService;
import com.yihexinda.dataservice.utils.RedisUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverDto;
import com.yihexinda.data.dto.TDriverWorkHourDto;
import com.yihexinda.data.dto.TWorkHourDto;
import com.yihexinda.dataservice.dao.TDriverDao;
import com.yihexinda.dataservice.service.TDriverService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.dataservice.service.TDriverWorkHourService;
import org.springframework.beans.factory.annotation.Autowired;
import com.yihexinda.dataservice.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 司机信息表 服务实现类
 * </p>
 *
 * @author pengfeng
 * @since 2018-11-28
 */
@Service
public class TDriverServiceImpl extends ServiceImpl<TDriverDao, TDriverDto> implements TDriverService {
    /**
     * dao (可以去除，使用baseMapper )
     */
    @Resource
    private  TDriverDao tDriverDao;

    /**
     *  司机工时服务
     */
    @Resource
    private TDriverWorkHourService tDriverWorkHourService;

    /**
     *  车辆绑定服务
     */
    @Resource
    private TDriverCarBindService tDriverCarBindService;

    /**
     * redis 服务
     */
    @Autowired
    private RedisUtil redisUtil;

    /**
     * pengfeng
     * 司机批量排班
     * @param driverDtoList 司机listId
     * @param scheduleId   排班id
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchScheduling(List<TDriverDto> driverDtoList, String scheduleId) {

        //新增
        List<TDriverWorkHourDto> addHourDtoList = new ArrayList<>();
        //修改
        List<TDriverWorkHourDto> updateHourDtoList = new ArrayList<>();

        for (int i = 0; i < driverDtoList.size(); i++) {
            TDriverWorkHourDto tDriverWorkHourDto = new TDriverWorkHourDto();
            tDriverWorkHourDto.setDriverId(driverDtoList.get(i).getId());
            tDriverWorkHourDto.setScheduleId(scheduleId);
            //查询条件
            QueryWrapper<TDriverWorkHourDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("driver_id", driverDtoList.get(i).getId());
            //查询司机是否有排班
            if (tDriverWorkHourService.list(queryWrapper).size() > 0) {
                updateHourDtoList.addAll(tDriverWorkHourService.list(queryWrapper));
                updateHourDtoList.forEach(driverWorkHour -> driverWorkHour.setScheduleId(scheduleId));
            } else {
                addHourDtoList.add(tDriverWorkHourDto);
            }
        }
        if (addHourDtoList.size() > 0) {
            if (!tDriverWorkHourService.saveBatch(addHourDtoList)) {
               return  false;
            }
        }
        if (updateHourDtoList.size()>0){
            if (!tDriverWorkHourService.updateBatchById(updateHourDtoList)){
                return  false;
            }
        }
        return true;
    }

    /**
     * pengfeng
     * 新增司机信息
     * @param tDriverDto 司机实体
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDriverInfo(TDriverDto tDriverDto) {
        if (super.save(tDriverDto)) {
            // 新增司机工时
            if (null != tDriverDto.getDriverWorkHourList() && tDriverDto.getDriverWorkHourList().size() != 0) {
                //新增司机排班
                if (null != tDriverDto.getScheduleId() && !"".equals(tDriverDto.getScheduleId())) {
                    tDriverDto.getDriverWorkHourList().forEach(driverWorkHour -> driverWorkHour.setScheduleId(tDriverDto.getScheduleId()));
                }
                tDriverDto.getDriverWorkHourList().forEach(driverWorkHour -> driverWorkHour.setDriverId(tDriverDto.getId()));
                //批量插入
                tDriverWorkHourService.saveBatch(tDriverDto.getDriverWorkHourList());
            }else {
                if (null != tDriverDto.getScheduleId() && !"".equals(tDriverDto.getScheduleId())) {
                    TDriverWorkHourDto tDriverWorkHourDto = new TDriverWorkHourDto();
                    tDriverWorkHourDto.setScheduleId(tDriverDto.getScheduleId());
                    tDriverWorkHourDto.setDriverId(tDriverDto.getId());
                    tDriverWorkHourService.save(tDriverWorkHourDto);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * pengfeng
     * 修改司机信息
     * @param tDriverDto 司机实体
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDriverInfo(TDriverDto tDriverDto) {
        if (super.updateById(tDriverDto)) {
            if ((null==tDriverDto.getDriverWorkHourList()||tDriverDto.getDriverWorkHourList().size() == 0 )&& ("".equals(tDriverDto.getScheduleId())||null==tDriverDto.getScheduleId())) {
                return  true;
            }
            //查询条件
            QueryWrapper<TDriverWorkHourDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("driver_id", tDriverDto.getId());
            //查询司机是否有排班,删除
            if (tDriverWorkHourService.list(queryWrapper).size() > 0) {
                List<TDriverWorkHourDto> list = tDriverWorkHourService.list(queryWrapper);
                tDriverWorkHourService.remove(queryWrapper);
            }
            // 修改司机工时
            if (null!=tDriverDto.getDriverWorkHourList()&&tDriverDto.getDriverWorkHourList().size() > 0) {
                tDriverDto.getDriverWorkHourList().forEach(driverWorkHour -> driverWorkHour.setDriverId(tDriverDto.getId()));
                if(null!=tDriverDto.getScheduleId() && !"".equals(tDriverDto.getScheduleId())){
                    tDriverDto.getDriverWorkHourList().forEach(driverWorkHour -> driverWorkHour.setScheduleId(tDriverDto.getScheduleId()));
                }
                //批量新增
                tDriverWorkHourService.saveBatch(tDriverDto.getDriverWorkHourList());
            } else {
                if (null != tDriverDto.getScheduleId() && !"".equals(tDriverDto.getScheduleId())) {
                    //插入司机模板
                    TDriverWorkHourDto tDriverWorkHourDto = new TDriverWorkHourDto();
                    tDriverWorkHourDto.setScheduleId(tDriverDto.getScheduleId());
                    tDriverWorkHourDto.setDriverId(tDriverDto.getId());
                    tDriverWorkHourService.save(tDriverWorkHourDto);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 司机信息列表
     *
     * @return 司机信息列表
     */
    @Override
    public List<TDriverDto> getDriverList(Map data) {
        List<TDriverDto> tDriverDtoList =tDriverDao.getDriverList(data);
        for (TDriverDto tDriverDto : tDriverDtoList) {
            QueryWrapper<TDriverWorkHourDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("driver_id",tDriverDto.getId());
            if (tDriverWorkHourService.list(queryWrapper).size()>0){
                //排班工时
                tDriverDto.setDriverWorkHourList(tDriverWorkHourService.list(queryWrapper));
            }
        }
        return tDriverDtoList;
        //return tDriverDao.getDriverList();
    }

    /**
     * 获取司机详细信息
     *
     * @param id 司机id
     * @return 司机详情
     */
    @Override
    public TDriverDto getDriverInfo(String id) {
        //司机信息
        TDriverDto tDriverDto= super.getById(id);
        QueryWrapper<TDriverWorkHourDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id",tDriverDto.getId());
        if (tDriverWorkHourService.list(queryWrapper).size()>0){
            //排班工时
            tDriverDto.setDriverWorkHourList(tDriverWorkHourService.list(queryWrapper));
        }
        return tDriverDto;
    }

    /**
     * 退出登录
     *
     * @param driverId 司机id
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean loginOutDriver(String driverId) {
        if (StringUtil.isNotEmpty(driverId)){
            //清空redis
            redisUtil.remove(driverId);
            QueryWrapper<TDriverCarBindDto> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("bind_status","1");
            queryWrapper.eq("driver_id",driverId);
            TDriverCarBindDto tDriverCarBindDto= tDriverCarBindService.getOne(queryWrapper);
            //司机解绑车辆
            if (null!=tDriverCarBindDto){
                tDriverCarBindDto.setBindStatus("2");
                boolean b = tDriverCarBindService.updateById(tDriverCarBindDto);
                if (b){
                    String date = DateUtils.formatDate(new Date(), DateUtils.PATTERN_yyyy_MM_dd);
                    redisUtil.remove(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE+date+ tDriverCarBindDto.getCarId());
                    redisUtil.remove(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE+date+ driverId);
                    return  true;
                }
            }else {
                return  false;
            }
        }
        return false;
    }


    /**
     * 司机退出登录
     * @author chenzeqi
     * @param data
     * @return
     */
    @Override
    public ResultVo driverLogout(Map<String, Object> data) {
        if(data == null){
            return ResultVo.error(1,"param is null");
        }
        //删除redis缓存
        String driverId = StringUtil.trim(data.get("driverId"));
        //更改司机绑定车俩状态
        int status = tDriverDao.updateDriverCatStatus(data);
        if(status>0){
            String date = DateUtils.formatDate(new Date(), DateUtils.PATTERN_yyyy_MM_dd);
            redisUtil.remove(driverId);
            String carId = redisUtil.get(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE + date + driverId);
            redisUtil.remove(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE+date+ carId);
            redisUtil.remove(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE+date+ driverId);
            return ResultVo.success();
        }
        return ResultVo.error(ResultVo.Status.SYS_REQUIRED_FAILURE);
    }
}
