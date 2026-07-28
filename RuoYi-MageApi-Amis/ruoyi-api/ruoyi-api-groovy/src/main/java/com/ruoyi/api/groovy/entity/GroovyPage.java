package com.ruoyi.api.groovy.entity;

/**
 * 分页对象
 */

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Page is the result of  Db.paginate(......)
 */
@Data
public class GroovyPage<T> implements Serializable {

	private static final long serialVersionUID = -5395997221963176643L;

	private List<T> list;
	private int pageNumber;
	private int pageSize;
	private int totalPage;
	private int totalRow;

	/**
	 * Constructor.
	 * @param list the list of paginate result
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * @param totalPage the total page of paginate
	 * @param totalRow the total row of paginate
	 */
	public GroovyPage(List<T> list, int pageNumber, int pageSize, int totalPage, int totalRow) {
		this.list = list;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.totalRow = totalRow;
	}
	public GroovyPage() {
	}

	public List<T> getList() {
		return list;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getTotalRow() {
		return totalRow;
	}


}