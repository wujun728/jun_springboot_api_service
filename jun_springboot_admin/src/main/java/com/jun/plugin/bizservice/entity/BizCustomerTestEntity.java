package com.jun.plugin.bizservice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jun.plugin.system.entity.BaseEntity;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 客户信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-09-29 15:03:44
 */
@Data
@TableName("biz_customer_test")
public class BizCustomerTestEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 客户名称
	 */
	@TableField("cusname")
	private String cusname;

	/**
	 * 客户描述
	 */
	@TableField("cusdesc")
	private String cusdesc;

	/**
	 * 客户全称
	 */
	@TableField("fullname")
	private String fullname;

	/**
	 * 客户性质
	 */
	@TableField("dict_cussex")
	private String dictCussex;

	/**
	 * 注册时间
	 */
	@TableField("register_date")
	private Date registerDate;

	/**
	 * 客户类型
	 */
	@TableField("dict_custype")
	private String dictCustype;

	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;


}
