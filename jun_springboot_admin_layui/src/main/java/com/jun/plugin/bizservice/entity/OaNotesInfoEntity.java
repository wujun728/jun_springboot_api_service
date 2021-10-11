package com.jun.plugin.bizservice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jun.plugin.system.entity.BaseEntity;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 公告通知
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-03 20:39:02
 */
@Data
@TableName("oa_notes_info")
public class OaNotesInfoEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 标题
	 */
	@TableField("title")
	private String title;

	/**
	 * 发布内容
	 */
	@TableField("content")
	private String content;

	/**
	 * 发布信息类型
	 */
	@TableField("msg_type")
	private String msgType;

	/**
	 * 是否草稿
	 */
	@TableField("dict_is_draft")
	private String dictIsDraft;

	/**
	 * 发送日期
	 */
	@TableField("send_date")
	private Date sendDate;

	/**
	 * 发布人
	 */
	@TableField("send_person")
	private String sendPerson;

	/**
	 * 发布部门
	 */
	@TableField("send_dept")
	private String sendDept;

	/**
	 * 发布时间
	 */
	@TableField("publish_date")
	private Date publishDate;

	/**
	 * 失效时间
	 */
	@TableField("invalid_date")
	private Date invalidDate;


}
