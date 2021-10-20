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
 * 项目成员与结算
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Data
@TableName("pj_project_member")
public class PjProjectMemberEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 成员ID
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
	 * 成员名称
	 */
	@TableField(value = "ref_project_name"  )
	private String refProjectName;

	/**
	 * 成员名称
	 */
	@TableField(value = "ref_member_name"  )
	private String refMemberName;

	/**
	 * 成员项目角色
	 */
	@TableField(value = "member_role"  )
	private String memberRole;

	/**
	 * 成员工作内容
	 */
	@TableField(value = "member_work_content"  )
	private String memberWorkContent;

	/**
	 * 是否参与分成
	 */
	@TableField(value = "dict_yes_no"  )
	private String dictYesNo;

	/**
	 * 成员工作分成比例
	 */
	@TableField(value = "member_parts"  )
	private Integer memberParts;

	/**
	 * 成员合计投入项目工作日
	 */
	@TableField(value = "member_work_days"  )
	private Integer memberWorkDays;

	/**
	 * 分成金额
	 */
	@TableField(value = "member_parts_money"  )
	private Integer memberPartsMoney;

	/**
	 * 备注
	 */
	@TableField(value = "remark"  )
	private Double remark;

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
