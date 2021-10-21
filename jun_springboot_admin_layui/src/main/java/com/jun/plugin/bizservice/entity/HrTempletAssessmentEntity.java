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
 * 考核模板
 *
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
@Data
@TableName("hr_templet_assessment")
public class HrTempletAssessmentEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 考核模板ID
	 */
	@Id
	@TableId("id")
	private String id;

	/**
	 * 考核模板编码
	 */
	@TableField(value = "tno"  )
	private String tno;

	/**
	 * 考核模板名称
	 */
	@TableField(value = "tname"  )
	private String tname;

	/**
	 * 考核模板类型
	 */
	@TableField(value = "dict_template_type"  )
	private String dictTemplateType;

	/**
	 * 模板考核目标
	 */
	@TableField(value = "template_mubiao"  )
	private String templateMubiao;

	/**
	 * 模板考核标准
	 */
	@TableField(value = "template_rule"  )
	private String templateRule;

	/**
	 * 模板开始时间
	 */
	@TableField(value = "tbdate"  )
	private Date tbdate;

	/**
	 * 模板生效时间
	 */
	@TableField(value = "tedate"  )
	private Date tedate;

	/**
	 * 模板编制人
	 */
	@TableField(value = "tcreate" , fill = FieldFill.INSERT  )
	private String tcreate;

	/**
	 * 模板使用部门((二选一))
	 */
	@TableField(value = "tcomp"  )
	private String tcomp;

	/**
	 * 模板适用角色(二选一)
	 */
	@TableField(value = "trole"  )
	private String trole;

	/**
	 * 模板是否生效
	 */
	@TableField(value = "dict_is_volite"  )
	private BigDecimal dictIsVolite;

	/**
	 * 是否首选模板
	 */
	@TableField(value = "isdefault"  )
	private BigDecimal isdefault;

	/**
	 * 模板描述
	 */
	@TableField(value = "tinfo"  )
	private String tinfo;


}
