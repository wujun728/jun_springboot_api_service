package com.jun.plugin.bizservice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jun.plugin.system.entity.BaseEntity;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 员工请假
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Data
@TableName("oa_user_leave")
public class OaUserLeaveEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 员工工号
	 */
	@TableField(value = "ref_usercoce"  )
	private String refUsercoce;

	/**
	 * 员工姓名
	 */
	@TableField(value = "ref_username"  )
	private String refUsername;

	/**
	 * 员工部门
	 */
	@TableField(value = "ref_userdept"  )
	private String refUserdept;

	/**
	 * 请假日期
	 */
	@TableField(value = "leave_date"  )
	private Date leaveDate;

	/**
	 * 请假原因
	 */
	@TableField(value = "leave_desc"  )
	private String leaveDesc;

	/**
	 * 请假小时数
	 */
	@TableField(value = "leave_hours"  )
	private BigDecimal leaveHours;

	/**
	 * 最终请假小时数
	 */
	@TableField(value = "leave_hours2"  )
	private BigDecimal leaveHours2;

	/**
	 * 请假类型
	 */
	@TableField(value = "dict_leanve_type"  )
	private String dictLeanveType;

	/**
	 * 审批状态
	 */
	@TableField(value = "dict_approve_status"  )
	private String dictApproveStatus;

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
