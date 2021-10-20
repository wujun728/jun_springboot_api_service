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
 * 项目报告
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Data
@TableName("pj_project_report")
public class PjProjectReportEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 报告ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 报告名称
	 */
	@TableField(value = "report_name"  )
	private String reportName;

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
	 * 
	 */
	@TableField(value = "ref_customer_code"  )
	private String refCustomerCode;

	/**
	 * 客户名称
	 */
	@TableField(value = "ref_customer_name"  )
	private String refCustomerName;

	/**
	 * 约定书号
	 */
	@TableField(value = "contract_no"  )
	private String contractNo;

	/**
	 * 报告类型
	 */
	@TableField(value = "dict_report_type"  )
	private String dictReportType;

	/**
	 * 报告描述
	 */
	@TableField(value = "report_desc"  )
	private String reportDesc;

	/**
	 * 客户述求
	 */
	@TableField(value = "customer_req"  )
	private String customerReq;

	/**
	 * 报告详细描述
	 */
	@TableField(value = "report_detail"  )
	private String reportDetail;

	/**
	 * 报告输出时间
	 */
	@TableField(value = "report_time"  )
	private Date reportTime;

	/**
	 * 报告输出责任人
	 */
	@TableField(value = "ref_report_by"  )
	private String refReportBy;

	/**
	 * 报告完成天数
	 */
	@TableField(value = "report_finash_days"  )
	private Integer reportFinashDays;

	/**
	 * 报告完成百分比
	 */
	@TableField(value = "report_finsh_percent"  )
	private Integer reportFinshPercent;

	/**
	 * 报告状态
	 */
	@TableField(value = "dict_report_status"  )
	private String dictReportStatus;

	/**
	 * 备注
	 */
	@TableField(value = "remark"  )
	private String remark;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time" , fill = FieldFill.INSERT  )
	private Date createTime;

	/**
	 * 创建人
	 */
	@TableField(value = "create_id" , fill = FieldFill.INSERT  )
	private String createId;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time" , fill = FieldFill.INSERT_UPDATE  )
	private Date updateTime;

	/**
	 * 更新人
	 */
	@TableField(value = "update_id" , fill = FieldFill.INSERT_UPDATE  )
	private String updateId;


}
