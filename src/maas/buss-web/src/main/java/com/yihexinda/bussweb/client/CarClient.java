package com.yihexinda.bussweb.client;

import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCarDto;
import com.yihexinda.data.dto.TCarPositionDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author wenbn
 * @date 2018/10/12.
 */
@FeignClient(value = "data-service")
@RequestMapping("/car/client")
public interface CarClient {

    @RequestMapping(method = RequestMethod.GET, value = "/getCars")
    List<TCarDto> getCars();

    /**
     * 添加车辆信息
     * @param car
     * @return
     */
    @RequestMapping(value = "/addCar",method = RequestMethod.POST)
    ResultVo addCar(@RequestBody TCarDto car);

    /**
     * wenbn
     * 删除车辆
     * @param carId
     * @return
     */
    @RequestMapping(value = "/deleteCar",method = RequestMethod.POST)
    ResultVo deleteCar(String carId);

    /**
     * wenbn
     * 修改车辆
     * @param car
     * @return
     */
    @RequestMapping(value = "/updateCar",method = RequestMethod.POST)
    ResultVo updateCar(@RequestBody TCarDto car);

    /**
     * wenbn
     * 查询车辆
     * @param
     * @return
     */
    @RequestMapping(value = "/carList" ,method = RequestMethod.GET)
    ResultVo carList();


    /**
     * 添加车辆坐标信息
     * @param carPosition
     * @return
     */
    @RequestMapping(value = "/addCarPosition",method = RequestMethod.POST)
    ResultVo addCarPosition(@RequestBody TCarPositionDto carPosition);

    /**
     * pengfeng
     * 查询未绑定车辆列表
     * @return 未绑定车辆列表
     */
    @RequestMapping(value = "/getCarList" ,method = RequestMethod.GET)
    ResultVo getCarList();

    /**
     * 修改订单状态
     * @return
     */
    @RequestMapping(value = "/updateOrderStatus",method = RequestMethod.POST)
    Object updateOrderStatus();


    /**
     * 查询车辆是否到站
     * @param carPosition 车辆实时位置信息
     * @return ResultVo
     */
    @RequestMapping(value = "/carGetDown" ,method = RequestMethod.POST)
    ResultVo carGetDown(@RequestBody TCarPositionDto carPosition);

    /**
     * 平峰实时导航
     * @return
     */
    @RequestMapping(value = "/navigation" ,method = RequestMethod.POST)
    ResultVo navigation(@RequestBody Map<String,Object> condition);

    /**
     * 高峰实时导航
     * @return
     */
    @RequestMapping(value = "/realNavigation" ,method = RequestMethod.POST)
    ResultVo realNavigation(@RequestBody Map<String, Object> condition);

    /**
     * wenbn
     * 司机确认到站
     * @param condition
     * @return
     */
    @RequestMapping(value = "/confirmArrive" ,method = RequestMethod.POST)
    ResultVo confirmArrive(@RequestBody Map<String, Object> condition);
}
