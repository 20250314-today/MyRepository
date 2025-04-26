package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author ${author}
 * @email ${email}
 * @date 2022-02-12 00:24:20
 */

@TableName("user_do_homework")
public class UserDoHomework implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 作业回答ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 回答人ID
	 */
	private Integer userId;
	/**
	 * 作业ID
	 */
	private Integer homeworkId;
	/**
	 * 答案
	 */
	private String reply;
	/**
	 * 完成时间
	 */
	private Date completionTime;

	private String mode;
	private Integer page;
	private String content;

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

	public Integer getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Integer homeworkId) {
		this.homeworkId = homeworkId;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Date getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
