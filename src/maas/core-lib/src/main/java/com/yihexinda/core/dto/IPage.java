package com.yihexinda.core.dto;

import java.io.Serializable;
import java.util.Collection;
public interface IPage<T> extends Serializable {

	/**
	 * 方法说明：起始行.
	 *
	 * @return the start
	 */
	public int getPage();

	/**
	 * 方法说明：每页条数.
	 *
	 * @return the limit
	 */
	public int getPageSize();
	
	/**
	 * 方法说明：总记录数.
	 *
	 * @return the total
	 */
	public long getTotal();
	
	/**
	 * 方法说明：数据集合.
	 *
	 * @return the rows
	 */
	public Collection<T> getRows();
	
	/**
	 * 方法说明：其他数据，如统计内容.
	 *
	 * @return the other data
	 * @author 曾垂瑜
	 * CreateDate: 2013-6-26
	 */
	public Object getOtherData();
}
