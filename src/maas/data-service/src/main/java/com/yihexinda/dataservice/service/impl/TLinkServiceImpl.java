package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TLinkDto;
import com.yihexinda.dataservice.dao.TLinkDao;
import com.yihexinda.dataservice.service.TLinkService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 路网由一条条的link组成，link有起点和终点坐标，
link表，中心提供 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TLinkServiceImpl extends ServiceImpl<TLinkDao, TLinkDto> implements TLinkService {


}
