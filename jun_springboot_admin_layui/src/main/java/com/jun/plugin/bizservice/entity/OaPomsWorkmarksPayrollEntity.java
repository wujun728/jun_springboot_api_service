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
 * 工资审核发放
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Data
@TableName("oa_poms_workmarks_payroll")
public class OaPomsWorkmarksPayrollEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 工号
	 */
	@TableField(value = "usercode"  )
	private String usercode;

	/**
	 * 用户名称
	 */
	@TableField(value = "ref_username"  )
	private String refUsername;

	/**
	 * 月份
	 */
	@TableField(value = "month"  )
	private String month;

	/**
	 * 实际出勤天数
	 */
	@TableField(value = "work_day_count"  )
	private Integer workDayCount;

	/**
	 * 当月应出勤天数
	 */
	@TableField(value = "sholdbe_work_day_count"  )
	private Integer sholdbeWorkDayCount;

	/**
	 * 工作时长(小时)
	 */
	@TableField(value = "work_total_hours"  )
	private BigDecimal workTotalHours;

	/**
	 * 应出勤时长(小时)
	 */
	@TableField(value = "shoulbe_work_hours"  )
	private BigDecimal shoulbeWorkHours;

	/**
	 * 工资
	 */
	@TableField(value = "payroll"  )
	private BigDecimal payroll;

	/**
	 * 工资审核状态
	 */
	@TableField(value = "payroll_state"  )
	private String payrollState;

	/**
	 * 工资审核意见
	 */
	@TableField(value = "payroll_state_msg"  )
	private String payrollStateMsg;

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
