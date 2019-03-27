package com.yihexinda.dataservice.service.impl;



import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TBroadcastDto;
import com.yihexinda.dataservice.dao.TBroadcastDao;
import com.yihexinda.dataservice.service.TBroadcastService;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 广播信息表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TBroadcastServiceImpl extends ServiceImpl<TBroadcastDao, TBroadcastDto> implements TBroadcastService {
	

	@Override
	public List<TBroadcastDto> getBroadcastList() {
		return this.baseMapper.getBroadcastList();
	}

}
