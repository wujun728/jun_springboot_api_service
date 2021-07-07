package com.jun.plugin.api.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 *
 * 博客
 *
 */
@TableName("tb_blog")
public class Blog implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.UUID)
	@ApiModelProperty("id")
	private String id;
	/**
	 * 标题
	 */
	@NotEmpty(message = "博客标题不能为空")
	@ApiModelProperty("标题")
	private String title;
	/**
	 * 内容
	 */
	@NotEmpty(message = "博客内容不能为空")
	@ApiModelProperty("内容")
	private String content;
	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;
	/**
	 * 用户ID
	 */
	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
