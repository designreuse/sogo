package com.yihexinda.dataservice.service;

import com.yihexinda.data.dto.TUserFeedDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户反馈表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TUserFeedService extends IService<TUserFeedDto> {

    /**
     * 查询用户反馈列表
     * @return 反馈列表
     */
    List<TUserFeedDto> getUserFeedList(Map<String, Object> condition);

}
