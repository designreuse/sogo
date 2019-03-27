package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TNodeDto;
import com.yihexinda.dataservice.dao.TNodeDao;
import com.yihexinda.dataservice.service.TNodeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Node表，和link表里的fromNode和toNode来自此表
中心提供 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TNodeServiceImpl extends ServiceImpl<TNodeDao, TNodeDto> implements TNodeService {


}
