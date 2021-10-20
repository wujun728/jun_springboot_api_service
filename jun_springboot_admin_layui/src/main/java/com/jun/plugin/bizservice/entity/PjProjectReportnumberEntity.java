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
 * 项目报告文号
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Data
@TableName("pj_project_reportnumber")
public class PjProjectReportnumberEntity extends BaseEntity implements Serializable {
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
	@TableField(value = "ref_reportnumber_code"  )
	private String refReportnumberCode;

	/**
	 * 项目报告
	 */
	@TableField(value = "ref_reportnumber_title"  )
	private String refReportnumberTitle;

	/**
	 * 报告文号(生成)
	 */
	@TableField(value = "reportnumber_code"  )
	private String reportnumberCode;

	/**
	 * 报告出具人
	 */
	@TableField(value = "ref_reportnumber_man"  )
	private String refReportnumberMan;

	/**
	 * 报告审核人
	 */
	@TableField(value = "ref_reportnumber_check_man"  )
	private String refReportnumberCheckMan;

	/**
	 * 签字注册会计师
	 */
	@TableField(value = "ref_signature_accountant"  )
	private String refSignatureAccountant;

	/**
	 * 报告号状态
	 */
	@TableField(value = "dict_rp_status"  )
	private String dictRpStatus;

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
