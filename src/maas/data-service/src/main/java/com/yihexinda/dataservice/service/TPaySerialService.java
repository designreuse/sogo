package com.yihexinda.dataservice.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.data.dto.TPaySerialDto;

/**
 * <p>
 * 支付流水表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TPaySerialService extends IService<TPaySerialDto> {

	List<Map> getPaySerialCountList();

	/**
	 * 获取支付统计列表
	 * @param map  查询参数
	 * @return 统计列表
	 */
	List<TPaySerialDto> getPaySerialList(Map map);
}
