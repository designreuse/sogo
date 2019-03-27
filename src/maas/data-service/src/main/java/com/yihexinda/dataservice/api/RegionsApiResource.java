package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TRegionDto;
import com.yihexinda.dataservice.service.TRegionService;
import com.yihexinda.dataservice.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/11 0011
 */
@RestController
@RequestMapping("/region/client")
public class RegionsApiResource {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TRegionService regionService;

    /**
     * 获取省市区
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/regionList")
    ResultVo regionList(){
        ResultVo vo = ResultVo.success();
        List<Map<String, Object>> regionList = null;
        String regions = redisUtil.get(RedisConstant.SYS_RESIONS_CACHE_KEY);
        if(!StringUtil.isEmpty(regions)){
            vo.setDataSet(regions);
        }
        regionList = generateRegionToTree(null,-1, Maps.newHashMap());
        redisUtil.set(RedisConstant.SYS_RESIONS_CACHE_KEY, JsonUtil.list2Json(regionList),(long)60*60*24*30);
        return vo.setDataSet(regionList);
    }

    /**
     * 递归查询所有子节点
     * @param permMap
     * @param pid
     * @return
     */
    private List<Map<String, Object>> generateRegionToTree(List<Map<String, Object>> permMap, Integer pid, Map<Object, Object> condition) {
        if (null == permMap || permMap.size() == 0) {
            permMap = regionService.listMaps();
        }
        List<Map<String, Object>> orgList = new ArrayList<>();
        if (permMap != null && permMap.size() > 0) {
            for (Map<String, Object> item : permMap) {
                //比较传入pid与当前对象pid是否相等
                if (pid.equals(StringUtil.getAsInt(StringUtil.trim(item.get("region_parent_id"))))) {
                    //将当前对象id做为pid递归调用当前方法，获取下级结果
                    List<Map<String, Object>> children = generateRegionToTree(permMap, Integer.valueOf(item.get("id").toString()),null);
                    //将子结果集存入当前对象的children字段中
                    item.put("children", children);
                    //添加当前对象到主结果集中
                    orgList.add(item);
                }
            }
        }
        return orgList;
    }

    /**
     *  区域id查询区域名称
     * @param addressId 区域地址id （1000018_1001969_1001976）
     * @return 区域地址名称（广东省深圳是市）
     */
     public String regionAddress(String addressId){
         Assert.hasText(addressId, "区域id异常");
         StringBuilder regionAddress = new StringBuilder();
         QueryWrapper<TRegionDto> queryWrapper =new QueryWrapper<>();
         queryWrapper.in("id", Arrays.asList(addressId.split("_")));
         List<TRegionDto> regionDtoList=regionService.list(queryWrapper);
         for (TRegionDto tRegionDto : regionDtoList) {
             regionAddress.append(tRegionDto.getRegionName());
         }
        return regionAddress.toString();
     }

}
