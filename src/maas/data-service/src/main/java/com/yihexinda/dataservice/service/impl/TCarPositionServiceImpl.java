package com.yihexinda.dataservice.service.impl;

import com.google.common.collect.Maps;
import com.yihexinda.core.utils.DirctionAngle;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.data.dto.TCarPositionDto;
import com.yihexinda.dataservice.dao.TCarPositionDao;
import com.yihexinda.dataservice.service.TCarPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆实时位置记录表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-12-05
 */
@Service
public class TCarPositionServiceImpl extends ServiceImpl<TCarPositionDao, TCarPositionDto> implements TCarPositionService {


    /**
     * 查询车辆现在所属位置
     * @return
     */
    @Override
    public List<Map<String, Object>> selectCurCarPostion() {
        return this.baseMapper.selectCurCarPostion(new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> selectCurCarPostion(Map<String, Object> condition) {
        return this.baseMapper.selectCurCarPostion(condition);
    }

    /**
     * 根据车辆id查询车辆所属位置
     * @param condition
     * @return
     */
    @Override
    public Map<String, Object> selectCurCarPostionById(Map<String, Object> condition) {
        List<Map<String, Object>> maps = this.baseMapper.selectCurCarPostion(condition);
        if(maps==null || maps.size()==0){
            return null;
        }
        return maps.get(0);
    }

    /**
     * 计算车辆方向角
     * @return
     * List<String> vehicleIds 车辆ids
     * List<Map<String,Object>> vehicleList 返回车辆信息
     * @throws Exception
     */
    @Override
    public Map<String, Object> calculateVehicleDirectionAngle() throws Exception {
        //保存返回的结果集
        Map<String, Object> data = Maps.newHashMap();
        //查询车辆现在所属位置
        List<Map<String,Object>> vehicleList = this.selectCurCarPostion();
        List<String> vehicleIds = new ArrayList<>();
        //保存当前位置
        Map<String,Object> curLocation = null;
        for (Map<String,Object> vehicle : vehicleList) {
            //计算方向角
            double lastlongitude = StringUtil.getDouble(StringUtil.trim(vehicle.get("lastlongitude")));
            double lastlatitude = StringUtil.getDouble(StringUtil.trim(vehicle.get("lastlatitude")));
            double longitude = StringUtil.getDouble(StringUtil.trim(vehicle.get("lng")));
            double latitude = StringUtil.getDouble(StringUtil.trim(vehicle.get("lati")));
            //计算出来的方向角
            int angle = DirctionAngle.angleCalculate(lastlongitude, lastlatitude, longitude, latitude);
            //计算车上人数
            String id = StringUtil.trim(vehicle.get("id"));
            vehicleIds.add(id);

            curLocation = Maps.newHashMap();
            curLocation.put("id","id");
            curLocation.put("lng",longitude);
            curLocation.put("lati",latitude);
            vehicle.put("curLocation",curLocation);
            vehicle.remove("id");
            vehicle.remove("lng");
            vehicle.remove("lati");
            vehicle.remove("lastlongitude");
            vehicle.remove("lastlatitude");
            vehicle.remove("row");
            vehicle.put("dirAngle",angle);
            vehicle.put("vehicleID",id);
        }
        data.put("vehicleList",vehicleList);
        data.put("vehicleIds",vehicleIds);
        return data;
    }


    /**
     * 计算车辆方向角
     * @return
     * List<String> vehicleIds 车辆ids
     * List<Map<String,Object>> vehicleList 返回车辆信息
     * @throws Exception
     */
    @Override
    public Map<String, Object> calculateVehicleDirectionAngle(Map<String, Object> condition) throws Exception {
        //保存返回的结果集
        Map<String, Object> data = Maps.newHashMap();
        //查询车辆现在所属位置
        List<Map<String,Object>> vehicleList = this.selectCurCarPostion(condition);
        List<String> vehicleIds = new ArrayList<>();
        //保存当前位置
        Map<String,Object> curLocation = null;
        for (Map<String,Object> vehicle : vehicleList) {
            //计算方向角
            double lastlongitude = StringUtil.getDouble(StringUtil.trim(vehicle.get("lastlongitude")));
            double lastlatitude = StringUtil.getDouble(StringUtil.trim(vehicle.get("lastlatitude")));
            double longitude = StringUtil.getDouble(StringUtil.trim(vehicle.get("lng")));
            double latitude = StringUtil.getDouble(StringUtil.trim(vehicle.get("lati")));
            //计算出来的方向角
            int angle = DirctionAngle.angleCalculate(lastlongitude, lastlatitude, longitude, latitude);
            //计算车上人数
            String id = StringUtil.trim(vehicle.get("id"));
            vehicleIds.add(id);

            curLocation = Maps.newHashMap();
            curLocation.put("id","id");
            curLocation.put("lng",longitude);
            curLocation.put("lati",latitude);
            vehicle.put("curLocation",curLocation);
            vehicle.remove("id");
            vehicle.remove("lng");
            vehicle.remove("lati");
            vehicle.remove("lastlongitude");
            vehicle.remove("lastlatitude");
            vehicle.remove("row");
            vehicle.put("dirAngle",angle);
            vehicle.put("vehicleID",id);
        }
        data.put("vehicleList",vehicleList);
        data.put("vehicleIds",vehicleIds);
        return data;
    }
}
