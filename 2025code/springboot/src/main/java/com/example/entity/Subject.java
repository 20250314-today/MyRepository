package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-15 21:39:15
 */

@TableName("subject")
public class Subject implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 科目ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 科目名称
	 */
	private String subjectName;

	private Integer page;
	private Integer classId;

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	/**
	 * 每页条数
	 */
	private Integer pageSize;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
