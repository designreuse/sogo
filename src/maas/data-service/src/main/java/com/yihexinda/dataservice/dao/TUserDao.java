package com.yihexinda.dataservice.dao;

import com.yihexinda.data.dto.TUserDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TUserDao extends BaseMapper<TUserDto> {
   
	/**
	    *    校验授权
	 * @param oppenid
	 * @return
	 */
	public TUserDto getOpenId(String openid);
}
