package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TCarDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆信息表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Mapper
public interface TCarDao extends BaseMapper<TCarDto> {

    /**
     * 查询车辆总座位数
     * @return ResultVo
     */
    Integer getCarCount();

}
