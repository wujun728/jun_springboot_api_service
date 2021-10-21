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
 * 考勤记录
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Data
@TableName("oa_poms_workmarks_times")
public class OaPomsWorkmarksTimesEntity extends BaseEntity implements Serializable {
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
	 * 日期
	 */
	@TableField(value = "work_day"  )
	private Date workDay;

	/**
	 * 开始时间
	 */
	@TableField(value = "begin_time"  )
	private Date beginTime;

	/**
	 * 结束时间
	 */
	@TableField(value = "end_time"  )
	private Date endTime;

	/**
	 * 工作时长
	 */
	@TableField(value = "work_total_time"  )
	private BigDecimal workTotalTime;

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
