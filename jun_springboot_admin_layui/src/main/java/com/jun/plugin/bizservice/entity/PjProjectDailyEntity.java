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
 * 项目日报周报
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Data
@TableName("pj_project_daily")
public class PjProjectDailyEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 标题
	 */
	@TableField(value = "plan_name"  )
	private String planName;

	/**
	 * 项目名称
	 */
	@TableField(value = "ref_project_code"  )
	private String refProjectCode;

	/**
	 * 日报周报
	 */
	@TableField(value = "dict_daily_type"  )
	private String dictDailyType;

	/**
	 * 日报周报详细描述
	 */
	@TableField(value = "daily_detail"  )
	private String dailyDetail;

	/**
	 * 开始时间
	 */
	@TableField(value = "plan_time_start"  )
	private Date planTimeStart;

	/**
	 * 结束时间
	 */
	@TableField(value = "plan_time_end"  )
	private Date planTimeEnd;

	/**
	 * 是否有风险
	 */
	@TableField(value = "dict_risk"  )
	private Integer dictRisk;

	/**
	 * 是否求助
	 */
	@TableField(value = "dict_ask_help"  )
	private String dictAskHelp;

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
