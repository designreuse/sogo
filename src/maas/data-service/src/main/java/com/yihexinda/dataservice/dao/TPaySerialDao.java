package com.yihexinda.dataservice.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TPaySerialDto;

/**
 * <p>
 * 支付流水表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TPaySerialDao extends BaseMapper<TPaySerialDto> {
    
	/**
	 * 支付统计
	 * @return
	 */
	List<Map> getPaySerialCountList();

	/**
	 * 支付统计列表
	 * @param map 查询参数
	 * @return 统计列表
	 */
	List<TPaySerialDto> getPaySerialList(Map map);

}
