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
 * 项目总结及评价
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Data
@TableName("oa_office_count")
public class OaOfficeCountEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 办公用品名称
	 */
	@TableField(value = "dict_product"  )
	private String dictProduct;

	/**
	 * 办公用品TODO
	 */
	@TableField(value = "dict_product_type"  )
	private String dictProductType;

	/**
	 * 办公用品用途
	 */
	@TableField(value = "ref_username"  )
	private String refUsername;

	/**
	 * 在项目中的角色
	 */
	@TableField(value = "ref_project_role"  )
	private String refProjectRole;

	/**
	 * 总体评级
	 */
	@TableField(value = "detail1"  )
	private String detail1;

	/**
	 * 工作总结
	 */
	@TableField(value = "detail2"  )
	private String detail2;

	/**
	 * 做的好的
	 */
	@TableField(value = "detail3"  )
	private String detail3;

	/**
	 * 做的不好的
	 */
	@TableField(value = "detail4"  )
	private String detail4;

	/**
	 * 改进措施及建议
	 */
	@TableField(value = "detail5"  )
	private String detail5;

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
