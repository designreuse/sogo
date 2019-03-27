package com.yihexinda.dataservice.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yihexinda.data.dto.TBroadcastDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 广播信息表 Mapper 接口
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TBroadcastDao extends BaseMapper<TBroadcastDto> {
   /**
    *  显示广播
    * @return
    */
	public List<TBroadcastDto> getBroadcastList();
}
