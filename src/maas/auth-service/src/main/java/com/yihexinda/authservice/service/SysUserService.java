package com.yihexinda.authservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.core.Execption.BussException;

/**
 * 类说明：实现类
 *
 *
 * <p>
 * 详细描述：.
 *
 * @author luoshuming
 * <p>
 * <p>
 * CreateDate: 2018-10-19
 */
@Service
public class SysUserService {


    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(SysUserService.class);



    public SysUserDto findSysUserByUsername(String username) {
        try {
            return null;
        } catch (Exception e) {
            logger.error("findSysUserByUsername {}", e);
            throw new BussException("系统内部异常");
        }
    }

//    /**
//     * 根据登录账号或用户名来模糊查询对应的用户,并作分页处理
//     *
//     * @param pageQueryDto
//     * @return
//     */
//    public Page<SysUserNameResponseDto> findSysUserLikeUserName(SysUserPageQueryDto pageQueryDto) throws BussException {
//        logger.debug("method: findSysUserLikeUserName(String username){} -start ", pageQueryDto);
//        AssertUtils.notNullAndEmpty(pageQueryDto.getName(), "登录账号或用户名不能为空");
//        try {
//            //查询处理
//            Page<SysUserNameResponseDto> page = new Page<>();
//            List<SysUserNameResponseDto> likeUserName = null;
//            //内容总条数
//            Integer amount;
//            amount = sysUserMapper.findUserLikeUserNameAmount(pageQueryDto.getName());
//            if (amount > 0) {
//                //查询内容集合
//                likeUserName = sysUserMapper.findSysUserLikeUserName(pageQueryDto.getName(),
//                        pageQueryDto.getStart(), pageQueryDto.getEnd());
//            }
//            //封装参数
//            page.setTotal(amount.longValue()); //总内容数
//            page.setRows(likeUserName); //内容集合
//            page.setPage(pageQueryDto.getPage()); //当前页数
//            page.setPageSize(pageQueryDto.getPageSize()); //每页数量
//            return page;
//        } catch (Exception e) {
//            logger.error("findSysUserLikeUserName(String username){}", e);
//            throw new BussException("系统内部异常");
//        }
//    }
//
//    /**
//     * 根据登录账号或用户名来模糊查询对应的用户
//     *
//     * @param pageQueryDto
//     * @return
//     */
//    public List<SysUserNameResponseDto> findSysUserLikeName(SysUserPageQueryDto pageQueryDto) throws BussException {
//        logger.debug("method: findSysUserLikeName{} -start ", pageQueryDto);
//        AssertUtils.notNull(pageQueryDto);
//        AssertUtils.notNullAndEmpty(pageQueryDto.getName(), "用户名或登录账号不能为空");
//        try {
//            //查询处理
//            List<SysUserNameResponseDto> list = sysUserMapper.findSysUserLikeName(pageQueryDto.getName());
//            return list;
//        } catch (Exception e) {
//            logger.error("findSysUserLikeName{}", e);
//            throw new BussException("系统内部异常");
//        }
//    }

}
