package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TCompanyInfoDto;
import com.yihexinda.dataservice.dao.TCompanyInfoDao;
import com.yihexinda.dataservice.service.TCompanyInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公司员工信息表 服务实现类
 * </p>
 *
 * @author 耿东雪
 * @since 2019-03-04
 */
@Service
public class TCompanyInfoServiceImpl extends ServiceImpl<TCompanyInfoDao, TCompanyInfoDto> implements TCompanyInfoService {

}
