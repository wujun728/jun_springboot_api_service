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
 * 项目进度与风险
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Data
@TableName("pj_project_prodess_risk")
public class PjProjectProdessRiskEntity extends BaseEntity implements Serializable {
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
	 * 周报详细描述
	 */
	@TableField(value = "plan_detail"  )
	private String planDetail;

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
	@TableField(value = "is_risk"  )
	private Integer isRisk;

	/**
	 * 是否求助
	 */
	@TableField(value = "is_ask_help"  )
	private String isAskHelp;

	/**
	 * 求助后解决措施
	 */
	@TableField(value = "solution"  )
	private String solution;

	/**
	 * 风险无法解决
	 */
	@TableField(value = "cant_be_solved"  )
	private String cantBeSolved;

	/**
	 * 无法解决问题升级-求助人
	 */
	@TableField(value = "ask_for_helper"  )
	private String askForHelper;

	/**
	 * 周报文件附件
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
