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
 * 项目借阅
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Data
@TableName("pj_project_borrow")
public class PjProjectBorrowEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 
	 */
	@TableField(value = "ref_project_code"  )
	private String refProjectCode;

	/**
	 * 项目名称
	 */
	@TableField(value = "ref_project_name"  )
	private String refProjectName;

	/**
	 * 
	 */
	@TableField(value = "ref_user_code"  )
	private String refUserCode;

	/**
	 * 用户名称
	 */
	@TableField(value = "ref_user_name"  )
	private String refUserName;

	/**
	 * 借阅原因
	 */
	@TableField(value = "borrow_desc"  )
	private String borrowDesc;

	/**
	 * 借阅结束日期
	 */
	@TableField(value = "end_time"  )
	private Date endTime;

	/**
	 * 状态
	 */
	@TableField(value = "dict_borrow_state"  )
	private String dictBorrowState;

	/**
	 * 备注
	 */
	@TableField(value = "remark"  )
	private String remark;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time" , fill = FieldFill.INSERT  )
	private Date createTime;

	/**
	 * 创建人
	 */
	@TableField(value = "create_id" , fill = FieldFill.INSERT  )
	private String createId;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time" , fill = FieldFill.INSERT_UPDATE  )
	private Date updateTime;

	/**
	 * 更新人
	 */
	@TableField(value = "update_id" , fill = FieldFill.INSERT_UPDATE  )
	private String updateId;


}
