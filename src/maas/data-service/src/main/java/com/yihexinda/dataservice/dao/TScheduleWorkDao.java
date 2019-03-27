package com.yihexinda.dataservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TScheduleWorkDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 排班信息表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TScheduleWorkDao extends BaseMapper<TScheduleWorkDto> {

    /**
     * 查询排班信息列表
     * @param map  查询参数
     * @return 排班信息列表
     */
    @Select({"<script>",
            "select tsw.*,tsu.username from t_schedule_work tsw",
            " left join t_sys_user tsu on ( tsw.create_id = tsu.id)",
            "WHERE 1=1",
            "<when test='status!=null'>",
            "AND  tsw.status = #{status}",
            "</when>",
            "<when test='scheduleName!=null'>",
            "AND  tsw.schedule_name like concat(concat(#{scheduleName}),'%')",
            "</when>",
            "<when test='aStartTime!=null and aEndTime!=null'>",
            "AND  tsw.create_date between #{aStartTime} and  #{aEndTime}",
            "</when>",
            "</script>"})
    List<TScheduleWorkDto> getScheduleWorkList(Map map);

}
