package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TPaySerialDto;
import com.yihexinda.dataservice.dao.TPaySerialDao;
import com.yihexinda.dataservice.service.TPaySerialService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付流水表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TPaySerialServiceImpl extends ServiceImpl<TPaySerialDao, TPaySerialDto> implements TPaySerialService {
	
	
	@Override
	public List<Map> getPaySerialCountList() {
		return this.baseMapper.getPaySerialCountList();
	}

	/**
	 * 获取支付统计列表
	 *
	 * @return 统计列表
	 */
	@Override
	public List<TPaySerialDto> getPaySerialList(Map map) {
		return this.baseMapper.getPaySerialList(map);
	}


}
