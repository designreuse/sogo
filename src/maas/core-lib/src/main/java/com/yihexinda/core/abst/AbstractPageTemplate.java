package com.yihexinda.core.abst;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yihexinda.core.vo.ResultVo;

import java.util.List;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/1 0001
 */
public abstract class AbstractPageTemplate<T> {

    private static ResultVo vo = new ResultVo();

    public final ResultVo preparePageTemplate(int pageIndex,int pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        List<T> list = executeSql();
        PageInfo result = new PageInfo(list);
        result.setList(null);
        vo.setDataSet(list);
        vo.setPageInfo(result);
        return vo;
    }

    //执行sql,书写核心业务
    protected abstract List<T> executeSql();

}
