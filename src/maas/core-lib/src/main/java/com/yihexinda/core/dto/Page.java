package com.yihexinda.core.dto;


import java.util.ArrayList;
import java.util.Collection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 类说明：分页查询返回模型
 * <p>
 * <p/>
 * 详细描述：.
 *
 * @param <T> the generic type
 *            <p>
 *            CreateDate: 2013-6-17
 */
@Data
@ToString
@ApiModel("分页查询返回实体")
public class Page<T> implements IPage<T> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6570192533192942163L;

    /**
     * 当前页数.
     */
    @ApiModelProperty("当前页数")
    private int page;

    /**
     * 每页条数.
     */
    @ApiModelProperty("每页条数")
    private int pageSize = 30;

    /**
     * 总条数.
     */
    @ApiModelProperty("总条数")
    private long total;

    /**
     * 数据集合.
     */
    @ApiModelProperty("数据集合")
    private Collection<T> rows;

    /**
     * 其他数据，如统计内容.
     */
    @ApiModelProperty("其他数据，如统计内容")
    private Object otherData;

    /**
     * The Constructor.
     */
    public Page() {
        super();
    }

    /**
     * The Constructor.
     *
     * @param rows     the rows
     * @param total    the total
     * @param page     the page
     * @param pageSize the pageSize
     */
    public Page(Collection<T> rows, long total, int page, int pageSize) {
        super();
        this.total = total;
        this.pageSize = pageSize;
        this.page = page;
        this.rows = rows == null ? new ArrayList<T>(0) : rows;
    }

    /**
     * The Constructor.
     *
     * @param rows            the rows
     * @param total           the total
     * @param pageParamEntity the pageParamEntity
     */
    public Page(Collection<T> rows, long total, PageParamEntity pageParamEntity) {
        super();
        this.total = total;
        this.pageSize = pageParamEntity.getPageSize();
        this.page = pageParamEntity.getPage();
        this.rows = rows == null ? new ArrayList<T>(0) : rows;
    }

}
