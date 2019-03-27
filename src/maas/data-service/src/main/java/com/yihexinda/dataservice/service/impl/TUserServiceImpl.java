package com.yihexinda.dataservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yihexinda.data.dto.TUserDto;
import com.yihexinda.dataservice.dao.TUserDao;
import com.yihexinda.dataservice.service.TUserService;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserDao, TUserDto> implements TUserService {

}
