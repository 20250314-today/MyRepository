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
@TableName("user_subject")
public class UserSubject implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 科目与班级中间表id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 科目id
	 */
	private Integer subjectId;
	private Integer page;

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
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
