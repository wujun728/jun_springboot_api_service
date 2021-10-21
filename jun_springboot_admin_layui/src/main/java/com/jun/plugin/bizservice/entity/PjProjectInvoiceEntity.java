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
 * 项目开票
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
@Data
@TableName("pj_project_invoice")
public class PjProjectInvoiceEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 开票客户
	 */
	@TableField(value = "invoice_customer"  )
	private String invoiceCustomer;

	/**
	 * 开票项目
	 */
	@TableField(value = "invoice_project"  )
	private String invoiceProject;

	/**
	 * 开票类型
	 */
	@TableField(value = "invoice_type"  )
	private String invoiceType;

	/**
	 * 开票单位名称
	 */
	@TableField(value = "invoice_company_name"  )
	private String invoiceCompanyName;

	/**
	 * 纳税人识别号
	 */
	@TableField(value = "invoice_tax_no"  )
	private String invoiceTaxNo;

	/**
	 * 地址
	 */
	@TableField(value = "address"  )
	private String address;

	/**
	 * 电话
	 */
	@TableField(value = "telephone"  )
	private String telephone;

	/**
	 * 开户行
	 */
	@TableField(value = "bank"  )
	private String bank;

	/**
	 * 开户行账号
	 */
	@TableField(value = "bank_card_no"  )
	private String bankCardNo;

	/**
	 * 开票人
	 */
	@TableField(value = "invoice_man"  )
	private String invoiceMan;

	/**
	 * 开票金额
	 */
	@TableField(value = "invoice_money"  )
	private String invoiceMoney;

	/**
	 * 审批状态
	 */
	@TableField(value = "invoice_state"  )
	private String invoiceState;

	/**
	 * 流程状态
	 */
	@TableField(value = "dict_wf_state"  )
	private String dictWfState;

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

	/**
	 * 
	 */
	@TableField(value = "deleted" , fill = FieldFill.INSERT  )
	private Integer deleted;


}
