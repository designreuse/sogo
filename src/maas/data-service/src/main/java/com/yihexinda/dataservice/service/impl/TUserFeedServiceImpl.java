package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TUserFeedDto;
import com.yihexinda.dataservice.dao.TUserFeedDao;
import com.yihexinda.dataservice.service.TUserFeedService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户反馈表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TUserFeedServiceImpl extends ServiceImpl<TUserFeedDao, TUserFeedDto> implements TUserFeedService {


    /**
     * 查询用户反馈列表
     *
     * @return 反馈列表
     */
    @Override
    public List<TUserFeedDto> getUserFeedList(Map<String, Object> condition) {
        return this.baseMapper.getUserFeedList(condition);
    }
}
