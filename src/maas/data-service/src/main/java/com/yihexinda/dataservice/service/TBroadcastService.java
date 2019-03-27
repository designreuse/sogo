package com.yihexinda.dataservice.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.data.dto.TBroadcastDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 广播信息表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TBroadcastService extends IService<TBroadcastDto> {
	List<TBroadcastDto> getBroadcastList();
}
