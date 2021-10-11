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
 * 政策法规
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-11 15:45:48
 */
@Data
@TableName("oa_law_info")
public class OaLawInfoEntity extends BaseEntity implements Serializable {
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
	@TableField(value = "title"  )
	private String title;

	/**
	 * 发布内容
	 */
	@TableField(value = "content"  )
	private String content;

	/**
	 * 发布信息类型
	 */
	@TableField(value = "msg_type"  )
	private String msgType;

	/**
	 * 是否草稿
	 */
	@TableField(value = "dict_is_draft"  )
	private String dictIsDraft;

	/**
	 * 发送日期
	 */
	@TableField(value = "send_date"  )
	private Date sendDate;

	/**
	 * 发布人
	 */
	@TableField(value = "send_person"  )
	private String sendPerson;

	/**
	 * 发布部门
	 */
	@TableField(value = "send_dept"  )
	private String sendDept;

	/**
	 * 发布时间
	 */
	@TableField(value = "publish_date"  )
	private Date publishDate;

	/**
	 * 失效时间
	 */
	@TableField(value = "invalid_date"  )
	private Date invalidDate;

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

	/**
	 * 
	 */
	@TableField(value = "deleted" , fill = FieldFill.INSERT  )
	private Integer deleted;


}
