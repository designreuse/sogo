package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCarDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
     * @param car 车辆信息
     * @return ResultVo
     */
    @RequestMapping(value = "/addCar",method = RequestMethod.POST)
    ResultVo addCar(@RequestBody TCarDto car);

    /**
     * wenbn
     * 删除车辆
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteCar/{id}",method = RequestMethod.POST)
    ResultVo deleteCar(@PathVariable("id") String id);

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
     * 查询车辆列表
     * @param condition 参数
     * @return ResultVo
     */
    @PostMapping(value = "/carList")
    ResultVo carList(@RequestBody Map<String,Object> condition);

    /**
     * wenbn
     * 查询车辆位置
     * @param data 参数
     * @return 车辆经纬度信息
     */
    @PostMapping(value = "/queryCarPosition")
    ResultVo queryCarPosition(@RequestBody Map<String,Object> data);

    /**
     * 查询车辆详情
     * @param carId  车辆id
     * @return ResultVo
     */
    @GetMapping(value = "/getCarId/{carId}")
    ResultVo getCarId(@PathVariable("carId") String carId);

    /**
     * 车辆列表导出（excel）
     * @return  车辆列表
     */
    @GetMapping(value = "/getCarListExcel")
    List<TCarDto> getCarListExcel();

    /**
     * 查询车辆可用总数
     * @return  总数
     */
    @GetMapping(value = "/getCarCount")
    Integer getCarCount();

    /**
     * 查询状态为可用的车辆
     * @return  总数
     */
    @GetMapping(value = "/getCarListStatus")
    ResultVo getCarListStatus();

}
