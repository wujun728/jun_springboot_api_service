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
 * 项目计划
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Data
@TableName("pj_project_plan")
public class PjProjectPlanEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 项目计划ID
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
	 * 项目计划标题
	 */
	@TableField(value = "plan_name"  )
	private String planName;

	/**
	 * 项目计划详细描述
	 */
	@TableField(value = "plan_detail"  )
	private String planDetail;

	/**
	 * 项目计划开始时间
	 */
	@TableField(value = "plan_time_start"  )
	private Date planTimeStart;

	/**
	 * 项目计划结束时间
	 */
	@TableField(value = "plan_time_end"  )
	private Date planTimeEnd;

	/**
	 * 工期(人天)
	 */
	@TableField(value = "plan_dates"  )
	private String planDates;

	/**
	 * 项目计划交付日期
	 */
	@TableField(value = "plan_given_time"  )
	private Date planGivenTime;

	/**
	 * 项目计划完成天数
	 */
	@TableField(value = "plan_finash_days"  )
	private Integer planFinashDays;

	/**
	 * 项目实际完成天数
	 */
	@TableField(value = "plan_finash_days2"  )
	private Integer planFinashDays2;

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
