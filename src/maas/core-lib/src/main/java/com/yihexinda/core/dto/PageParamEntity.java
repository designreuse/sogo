package com.yihexinda.core.dto;
import java.io.Serializable;

/**
 * Copyright &copy; 2017-2020  All rights reserved.
 * <p>
 * Licensed under the
 */
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 类说明：分页查询参数模型
 * <p>
 * <p/>
 * 详细描述：.
 *
 * @author 罗书明
 * <p>
 * CreateDate: 2018-10-15
 */
@Data
public class PageParamEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 5413436619988169890L;

    /**
     * The Constant START.
     */
    private static final int START = 0;

    /**
     * The Constant LIMIT.
     */
    private static final int LIMIT = 10;

    /**
     * 当前页数.
     */
    @ApiModelProperty("当前页数")
    private int page = 0;

    /**
     * 每页条数.
     */
    @ApiModelProperty("每页条数")
    private int pageSize = LIMIT;

    /**
     * 排序字段.
     */
    @ApiModelProperty("排序字段")
    private String sortBy;

    /**
     * 排序类型：顺序asc、倒序desc.
     */
    @ApiModelProperty("排序类型：顺序asc、倒序desc")
    private PageSortType sortDir = PageSortType.desc;

    /**
     * Instantiates a new page param entity.
     */
    public PageParamEntity() {
        super();
    }


    /**
     * Gets the 起始行.
     *
     * @return the 起始行
     */
    public int getStart() {
        return page * pageSize + 1;
    }

    /**
     * Gets the 结束行.
     *
     * @return the 每页条数
     */
    public int getEnd() {
        return getStart() + pageSize - 1;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? LIMIT : pageSize;
    }
}
