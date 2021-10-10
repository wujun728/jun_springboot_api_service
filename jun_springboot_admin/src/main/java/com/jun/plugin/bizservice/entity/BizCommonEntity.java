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
 * 公共信息
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-09 15:25:29
 */
@Data
@TableName("biz_common")
public class BizCommonEntity extends BaseEntity implements Serializable {
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
	@TableField(value = "title"  )
	private String title;

	/**
	 * 客户描述
	 */
	@TableField(value = "desc"  )
	private String desc;

	/**
	 * 客户全称
	 */
	@TableField(value = "key"  )
	private String key;

	/**
	 * 客户性质
	 */
	@TableField(value = "value"  )
	private String value;

	/**
	 * 
	 */
	@TableField(value = "date"  )
	private Date date;

	/**
	 * 备注
	 */
	@TableField(value = "remark"  )
	private String remark;


}
