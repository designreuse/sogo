package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TDriverCarBindDto;
import com.yihexinda.dataservice.dao.TDriverCarBindDao;
import com.yihexinda.dataservice.service.TDriverCarBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 司机车辆绑定记录表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TDriverCarBindServiceImpl extends ServiceImpl<TDriverCarBindDao, TDriverCarBindDto> implements TDriverCarBindService {

    @Resource
    private  TDriverCarBindDao tDriverCarBindDao;

    @Override
    public List<TDriverCarBindDto> getList() {
        return  tDriverCarBindDao.getList();
    }

    @Override
    public TDriverCarBindDto getDriverId(String driverId) {
        return tDriverCarBindDao.getDriverId(driverId);
    }

    /**
     * 根据司机查询历史绑定列表
     *
     * @param map 查询参数
     * @return 司机历史绑定列表
     */
    @Override
    public List<TDriverCarBindDto> getDriverCarBind(Map map) {
        return  tDriverCarBindDao.getDriverCarBind(map);
    }

    /**
     * 获取司机登陆的时候所有绑定与未绑定车辆
     * @return
     */
    @Override
    public  List<TDriverCarBindDto> getCarAndBindStatusList() {
        return tDriverCarBindDao.getCarAndBindStatusList();
    }



    /**
     * 司机端查询当前绑定的车辆
     * @param driverId
     * @return
     */
    @Override
    public  TDriverCarBindDto getBindCar(String driverId) {
        return tDriverCarBindDao.getBindCar(driverId);
    }

    /**
     * 每天 23：59:59 将司机注销
     */
    @Override
    public boolean updateStatus() {
        return tDriverCarBindDao.updateStatus();
    }
}
