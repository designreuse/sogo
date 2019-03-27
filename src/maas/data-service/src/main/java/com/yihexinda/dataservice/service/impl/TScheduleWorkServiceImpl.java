package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TScheduleWorkDto;
import com.yihexinda.data.dto.TWorkHourDto;
import com.yihexinda.dataservice.dao.TScheduleWorkDao;
import com.yihexinda.dataservice.service.TScheduleWorkService;
import com.yihexinda.dataservice.service.TWorkHourService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 排班信息表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TScheduleWorkServiceImpl extends ServiceImpl<TScheduleWorkDao, TScheduleWorkDto> implements TScheduleWorkService {


    @Resource
    private  TScheduleWorkDao tScheduleWorkDao;

    /**
     * 工时服务
     */
    @Resource
    private TWorkHourService tWorkHourService;

    /**
     * 新增排班
     *
     * @param tScheduleWorkDto 排班信息
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addWorkHour(TScheduleWorkDto tScheduleWorkDto) {
        if (super.save(tScheduleWorkDto)) {
            if (null == tScheduleWorkDto.getWorkHourList() || (tScheduleWorkDto.getWorkHourList()).size() == 0) {
                return true;
            } else {
                tScheduleWorkDto.getWorkHourList().forEach(workHourDto -> workHourDto.setScheduleId(tScheduleWorkDto.getId()));
                if (tWorkHourService.saveBatch(tScheduleWorkDto.getWorkHourList())) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }


    /**
     * 修改排班信息
     *
     * @param tScheduleWorkDto 排班信息
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateWorkHour(TScheduleWorkDto tScheduleWorkDto) {
        if (super.updateById(tScheduleWorkDto)) {
            if (null == tScheduleWorkDto.getWorkHourList() || (tScheduleWorkDto.getWorkHourList()).size() == 0) {
                return true;
            } else {
                //查询是否有
                QueryWrapper<TWorkHourDto> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("schedule_id", tScheduleWorkDto.getId());
                if (tWorkHourService.list(queryWrapper).size() > 0) {
                    if (!tWorkHourService.remove(queryWrapper)) {
                        return false;
                    }
                }
                tScheduleWorkDto.getWorkHourList().forEach(workHourDto -> workHourDto.setScheduleId(tScheduleWorkDto.getId()));
                if (!tWorkHourService.saveBatch(tScheduleWorkDto.getWorkHourList())) {
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public List<TScheduleWorkDto> getScheduleWorkList(Map map) {
        //排班列表
        List<TScheduleWorkDto> tScheduleWorkDtoList= tScheduleWorkDao.getScheduleWorkList(map);
        for (TScheduleWorkDto tScheduleWorkDto :tScheduleWorkDtoList ) {
            QueryWrapper<TWorkHourDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("schedule_id",tScheduleWorkDto.getId());
            if (tWorkHourService.list(queryWrapper).size()>0){
                //排班工时
                tScheduleWorkDto.setWorkHourList(tWorkHourService.list(queryWrapper));
            }
        }
        return tScheduleWorkDtoList;
    }
}



