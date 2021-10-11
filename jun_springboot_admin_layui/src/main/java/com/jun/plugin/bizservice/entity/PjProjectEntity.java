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
 * 项目信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-09 15:45:46
 */
@Data
@TableName("pj_project")
public class PjProjectEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 项目ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 项目编码
	 */
	@TableField(value = "project_code"  )
	private String projectCode;

	/**
	 * 项目名称
	 */
	@TableField(value = "project_name"  )
	private String projectName;

	/**
	 * 项目类型
	 */
	@TableField(value = "dict_project_type"  )
	private String dictProjectType;

	/**
	 * 项目类型细分
	 */
	@TableField(value = "dict_project_type_sub"  )
	private String dictProjectTypeSub;

	/**
	 * 客户编码
	 */
	@TableField(value = "ref_id_cuscode"  )
	private String refIdCuscode;

	/**
	 * 客户(委托单位)
	 */
	@TableField(value = "ref_cusname"  )
	private String refCusname;

	/**
	 * 项目计划开始时间
	 */
	@TableField(value = "project_starttime"  )
	private Date projectStarttime;

	/**
	 * 项目计划结束时间
	 */
	@TableField(value = "project_endtime"  )
	private Date projectEndtime;

	/**
	 * 被评估单位
	 */
	@TableField(value = "cusname_todo"  )
	private String cusnameTodo;

	/**
	 * 项目经理
	 */
	@TableField(value = "ref_project_manager"  )
	private String refProjectManager;

	/**
	 * 承接(合伙)人
	 */
	@TableField(value = "ref_undertake_person"  )
	private String refUndertakePerson;

	/**
	 * 承做(合伙)人
	 */
	@TableField(value = "ref_undertak_tperson_do"  )
	private String refUndertakTpersonDo;

	/**
	 * 风险评估等级
	 */
	@TableField(value = "dict_risk_assessment"  )
	private String dictRiskAssessment;

	/**
	 * 首次承接
	 */
	@TableField(value = "dict_first_undertake"  )
	private String dictFirstUndertake;

	/**
	 * 客户诉求
	 */
	@TableField(value = "customer_req"  )
	private String customerReq;

	/**
	 * 备注
	 */
	@TableField(value = "remark"  )
	private String remark;

	/**
	 * 项目进度
	 */
	@TableField(value = "project_progress"  )
	private String projectProgress;

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
	 * 删除标识
	 */
	@TableField(value = "deleted" , fill = FieldFill.INSERT  )
	private Integer deleted;


}
