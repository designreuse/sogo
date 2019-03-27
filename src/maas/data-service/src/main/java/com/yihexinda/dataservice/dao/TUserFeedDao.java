package com.yihexinda.dataservice.dao;

import com.yihexinda.data.dto.TUserFeedDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户反馈表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TUserFeedDao extends BaseMapper<TUserFeedDto> {

    /**
     * 查询用户反馈列表
     * @param map  查询参数
     * @return 反馈列表
     */
    List<TUserFeedDto> getUserFeedList(Map<String, Object> condition);

}
