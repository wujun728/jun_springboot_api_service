package com.jun.plugin.bizservice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jun.plugin.system.entity.BaseEntity;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 项目底稿
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Data
@TableName("pj_project_draft")
public class PjProjectDraftEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 底稿ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 底稿标题
	 */
	@TableField(value = "draft_name"  )
	private String draftName;

	/**
	 * 项目名称
	 */
	@TableField(value = "ref_project_code"  )
	private String refProjectCode;

	/**
	 * 底稿类型
	 */
	@TableField(value = "ref_draft_type"  )
	private String refDraftType;

	/**
	 * 底稿描述
	 */
	@TableField(value = "draft_desc"  )
	private String draftDesc;

	/**
	 * 客户述求
	 */
	@TableField(value = "customer_req"  )
	private String customerReq;

	/**
	 * 底稿输出时间
	 */
	@TableField(value = "draft_time"  )
	private Date draftTime;

	/**
	 * 底稿输出责任人(承做)
	 */
	@TableField(value = "draft_by"  )
	private String draftBy;

	/**
	 * 底稿客户联系人(对接)
	 */
	@TableField(value = "draft_customer_person"  )
	private String draftCustomerPerson;

	/**
	 * 底稿交付所时间
	 */
	@TableField(value = "draft_given_time"  )
	private String draftGivenTime;

	/**
	 * 底稿完成天数
	 */
	@TableField(value = "draft_finash_days"  )
	private Integer draftFinashDays;

	/**
	 * 底稿文件名称
	 */
	@TableField(value = "ref_filename"  )
	private String refFilename;

	/**
	 * 备注
	 */
	@TableField(value = "remark"  )
	private String remark;

	/**
	 * 
	 */
	@TableField(value = "create_time" , fill = FieldFill.INSERT  )
	private Date createTime;

	/**
	 * 
	 */
	@TableField(value = "create_id" , fill = FieldFill.INSERT  )
	private String createId;

	/**
	 * 
	 */
	@TableField(value = "update_time" , fill = FieldFill.INSERT_UPDATE  )
	private Date updateTime;

	/**
	 * 
	 */
	@TableField(value = "update_id" , fill = FieldFill.INSERT_UPDATE  )
	private String updateId;


}
