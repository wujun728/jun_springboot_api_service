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
 * 考勤
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Data
@TableName("oa_user_worktimes")
public class OaUserWorktimesEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 工号
	 */
	@TableField(value = "ref_usercoce"  )
	private String refUsercoce;

	/**
	 * 姓名
	 */
	@TableField(value = "ref_username"  )
	private String refUsername;

	/**
	 * 部门
	 */
	@TableField(value = "ref_userdept"  )
	private String refUserdept;

	/**
	 * 日期
	 */
	@TableField(value = "leave_date"  )
	private Date leaveDate;

	/**
	 * 入场时间
	 */
	@TableField(value = "leave_in_time"  )
	private Date leaveInTime;

	/**
	 * 离场时间
	 */
	@TableField(value = "leave_out_time"  )
	private Date leaveOutTime;

	/**
	 * 入场地点
	 */
	@TableField(value = "leave_in_case"  )
	private String leaveInCase;

	/**
	 * 入场经纬度
	 */
	@TableField(value = "leave_in_xy"  )
	private String leaveInXy;

	/**
	 * 离场地点
	 */
	@TableField(value = "leanve_out_case"  )
	private String leanveOutCase;

	/**
	 * 离场经纬度
	 */
	@TableField(value = "leanve_out_xy"  )
	private String leanveOutXy;

	/**
	 * 异常类型
	 */
	@TableField(value = "dict_exception"  )
	private String dictException;

	/**
	 * 状态
	 */
	@TableField(value = "dict_wordtime_status"  )
	private String dictWordtimeStatus;

	/**
	 * 审批意见
	 */
	@TableField(value = "approve_desc"  )
	private String approveDesc;

	/**
	 * 请假
	 */
	@TableField(value = "approve_qingjia"  )
	private String approveQingjia;

	/**
	 * 实际出勤
	 */
	@TableField(value = "actual_attendance"  )
	private Double actualAttendance;

	/**
	 * 缺勤
	 */
	@TableField(value = "absent"  )
	private Double absent;

	/**
	 * 考勤申诉
	 */
	@TableField(value = "leanve_retroactive"  )
	private String leanveRetroactive;

	/**
	 * 补签到
	 */
	@TableField(value = "back_mark1"  )
	private Double backMark1;

	/**
	 * 审批类型
	 */
	@TableField(value = "dict_approve_type"  )
	private String dictApproveType;

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
