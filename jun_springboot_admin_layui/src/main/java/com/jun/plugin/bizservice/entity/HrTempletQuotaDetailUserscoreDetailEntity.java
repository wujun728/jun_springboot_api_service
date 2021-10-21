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
 * 用户考核明细
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Data
@TableName("hr_templet_quota_detail_userscore_detail")
public class HrTempletQuotaDetailUserscoreDetailEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 指标编码
	 */
	@TableField(value = "qno"  )
	private String qno;

	/**
	 * 指标名称
	 */
	@TableField(value = "qname"  )
	private String qname;

	/**
	 * 指标父编码
	 */
	@TableField(value = "pid"  )
	private String pid;

	/**
	 * 指标类型
	 */
	@TableField(value = "dict_qtype"  )
	private String dictQtype;

	/**
	 * 指标Level
	 */
	@TableField(value = "qlevel"  )
	private String qlevel;

	/**
	 * 指标等级
	 */
	@TableField(value = "gradeformulA"  )
	private String gradeformulA;

	/**
	 * 指标排序
	 */
	@TableField(value = "qorder"  )
	private String qorder;

	/**
	 * 指标权重
	 */
	@TableField(value = "qweight"  )
	private BigDecimal qweight;

	/**
	 * 指标考核分类
	 */
	@TableField(value = "dict_qtype2"  )
	private String dictQtype2;

	/**
	 * 
	 */
	@TableField(value = "dclid"  )
	private BigDecimal dclid;

	/**
	 * 指标考核模板ID
	 */
	@TableField(value = "templateid"  )
	private String templateid;

	/**
	 * 指标考核模板名称
	 */
	@TableField(value = "templatename"  )
	private String templatename;

	/**
	 * 
	 */
	@TableField(value = "userid"  )
	private String userid;

	/**
	 * 
	 */
	@TableField(value = "username"  )
	private String username;

	/**
	 * 
	 */
	@TableField(value = "userscore"  )
	private BigDecimal userscore;


}
